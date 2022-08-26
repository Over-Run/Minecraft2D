/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Overrun Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.overrun.mc2d.client;

import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.client.gui.screen.TitleScreen;
import io.github.overrun.mc2d.client.gui.screen.ingame.CreativeTabScreen;
import io.github.overrun.mc2d.client.gui.screen.ingame.PauseScreen;
import io.github.overrun.mc2d.client.gui.widget.AbstractButtonWidget;
import io.github.overrun.mc2d.client.model.BlockModelMgr;
import io.github.overrun.mc2d.client.world.render.WorldRenderer;
import io.github.overrun.mc2d.event.KeyCallback;
import io.github.overrun.mc2d.mod.ModLoader;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.TranslatableText;
import io.github.overrun.mc2d.util.Language;
import io.github.overrun.mc2d.util.Options;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.world.block.Blocks;
import io.github.overrun.mc2d.world.entity.HumanEntity;
import io.github.overrun.mc2d.world.entity.PlayerEntity;
import io.github.overrun.mc2d.world.item.BlockItemType;
import io.github.overrun.mc2d.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.overrun.swgl.core.gl.GLBlendFunc;
import org.overrun.swgl.core.gl.GLClear;
import org.overrun.swgl.core.gl.GLStateMgr;
import org.overrun.swgl.core.io.IFileProvider;
import org.overrun.swgl.core.io.Mouse;
import org.overrun.swgl.core.io.Window;
import org.overrun.swgl.core.util.LogFactory9;
import org.overrun.swgl.core.util.timing.Timer;
import org.slf4j.Logger;

import static io.github.overrun.mc2d.client.gui.DrawableHelper.drawTexture;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/25
 */
public final class Mc2dClient implements Runnable, AutoCloseable {
    private static final Logger logger = LogFactory9.getLogger();
    private static Mc2dClient instance;
    public static final String VERSION = "0.6.0";
    public static final IText VERSION_TEXT = IText.of("Minecraft2D " + VERSION);
    private static final IText MAX_MEMORY;
    private static int oldX, oldY, oldWidth, oldHeight;
    public final TextRenderer textRenderer;
    private final TextureManager textureManager;
    public Window window;
    public Mouse mouse;
    public Screen screen = null;
    public final Timer timer = new Timer(20);
    public @Nullable World world = null;
    public WorldRenderer worldRenderer = null;
    public PlayerEntity player = null;
    public int fps = 0;
    public boolean debugging = false;
    public double guiScale = Options.getD(Options.GUI_SCALE, 2.0);
    public double invGuiScale = 1 / guiScale;

    static {
        double maxMemory = Runtime.getRuntime().maxMemory() / 1048576D;
        MAX_MEMORY = new TranslatableText("text.debug.max_memory", maxMemory >= 1024 ? maxMemory / 1024D + " GB" : maxMemory + " MB");
    }

    private Mc2dClient() {
        GLFWErrorCallback.create((error, description) ->
            logger.error("GLFW Error {}: {}", error, GLFWErrorCallback.getDescription(description))
        ).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        window = new Window();
        window.createHandle(896, 512, VERSION_TEXT.asString(), handle -> {
            throw new IllegalStateException("Failed to create the GLFW window");
        });
        window.setIcon(IFileProvider.ofCaller(), "assets/mc2d/icon.png");
        mouse = new Mouse((x, y, xd, yd) -> {
        });
        mouse.registerToWindow(window);
        window.setKeyCb((handle, key, scancode, action, mods) -> {
            KeyCallback.post(handle, key, scancode, action, mods);
            if (action == GLFW_PRESS) {
                if (key == GLFW_KEY_F11) {
                    var vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                    if (glfwGetWindowMonitor(handle) == 0) {
                        try (var stack = MemoryStack.stackPush()) {
                            var pX = stack.mallocInt(1);
                            var pY = stack.mallocInt(1);
                            glfwGetWindowPos(handle, pX, pY);
                            oldX = pX.get(0);
                            oldY = pY.get(0);
                        }
                        oldWidth = window.getWidth();
                        oldHeight = window.getHeight();
                        if (vidMode != null) {
                            glfwSetWindowMonitor(handle,
                                glfwGetPrimaryMonitor(),
                                0,
                                0,
                                vidMode.width(),
                                vidMode.height(),
                                vidMode.refreshRate());
                        }
                    } else {
                        glfwSetWindowMonitor(handle, 0, oldX, oldY, oldWidth, oldHeight, 0);
                    }
                }

                onKeyPress(key, scancode, mods);
            }
        });
        window.setMouseBtnCb((handle, button, action, mods) -> {
            if (action == GLFW_PRESS) {
                if (screen != null) {
                    screen.mousePressed(mouse.getIntLastX(), mouse.getIntLastY(), button);
                }
            }
        });
        glfwSetWindowCloseCallback(window.getHandle(), handle -> {
            if (world != null) {
                world.save(player);
            }
        });
        window.setFBResizeCb((handle, width, height) -> onResize(width, height));
        window.setScrollCb((handle, xo, yo) -> {
            if (world != null && player != null) {
                player.hotBarNum -= (int) yo;
                if (player.hotBarNum < 0) {
                    player.hotBarNum = 9;
                } else if (player.hotBarNum > 9) {
                    player.hotBarNum = 0;
                }
            }
        });
        var vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode != null) {
            window.moveToCenter(vidMode.width(), vidMode.height());
        }
        window.makeContextCurr();
        GL.createCapabilities();
        GLStateMgr.init();
        glfwSwapInterval(Boolean.parseBoolean(System.getProperty("mc2d.vsync", "true")) ? 1 : 0);
        window.show();

        textRenderer = new TextRenderer(this);
        textureManager = new TextureManager();
    }

    public void init() {
        onResize(window.getWidth(), window.getHeight());

        Blocks.register();
        Items.register();
        ModLoader.loadMods();
        BlockModelMgr.loadAtlas();
        Language.init();
        Language.currentLang = Options.get("lang", "en_us");
        logger.info("Backend library: LWJGL version {}", Version.getVersion());
        GLClear.clearColor(.4f, .6f, .9f, 1);
        GLStateMgr.enableTexture2D();
        GLStateMgr.enableBlend();
        GLStateMgr.blendFunc(GLBlendFunc.SRC_ALPHA, GLBlendFunc.ONE_MINUS_SRC_ALPHA);
    }

    public void openScreen(@Nullable Screen screen) {
        if (this.screen != null) {
            this.screen.removed(screen);
        }
        if (screen == null && world == null) {
            screen = new TitleScreen();
        }
        this.screen = screen;
        if (screen != null) {
            screen.init(this,
                (int) Math.ceil(window.getWidth() * invGuiScale),
                (int) Math.ceil(window.getHeight() * invGuiScale));
        }
    }

    public void onResize(int width, int height) {
        if (screen == null) {
            openScreen(null);
        }
        if (screen != null) {
            screen.init(this,
                (int) Math.ceil(width * invGuiScale),
                (int) Math.ceil(height * invGuiScale));
        }
        glViewport(0, 0, width, height);
    }

    public void renderHud(int width, int height) {
        if (world != null) {
            if (debugging) {
                textRenderer.draw(0, 0, VERSION_TEXT);
                textRenderer.draw(0, textRenderer.drawHeight() + 1, IText.of(fps + " fps"));
                textRenderer.draw(0, textRenderer.drawHeight() * 2 + 1, new TranslatableText("text.debug.player.position",
                    player.position.x,
                    player.position.y,
                    player.position.z));
                textRenderer.draw(width - textRenderer.drawWidth(MAX_MEMORY), 0, MAX_MEMORY);
                if (ModLoader.getModCount() > 0) {
                    textRenderer.draw(0, 119, new TranslatableText("text.debug.mod_count", ModLoader.getModCount()));
                }
            }
            textureManager.bindTexture(AbstractButtonWidget.WIDGETS_LOCATION);
            glPushMatrix();
            double hotBarX = (width - 202) / 2.;
            glTranslated(hotBarX, height, 0);
            drawTexture(0, -22, 0, 0, 202, 22);
            drawTexture(player.hotBarNum * 20, -24, 1, 22, 22, 24);
            textureManager.bindTexture(BlockModelMgr.BLOCK_ATLAS);
            for (int i = 0; i < 10; i++) {
                var stack = player.hotBar[i];
                if (stack.isEmpty() || !(stack.getItem().asItem() instanceof BlockItemType blockItemType)) continue;
                var tex = BlockModelMgr.blockTexture(blockItemType.getBlock().getTexture());
                int u0 = BlockModelMgr.getBlockAtlas().getU0(tex);
                int v0 = BlockModelMgr.getBlockAtlas().getV0(tex);
                // todo: atlas::width(tex)
                drawTexture(3 + i * 20, -19,
                    u0, v0,
                    BlockModelMgr.getBlockAtlas().getU1(tex) - u0,
                    BlockModelMgr.getBlockAtlas().getV1(tex) - v0,
                    16, 16,
                    BlockModelMgr.getBlockAtlas().width(),
                    BlockModelMgr.getBlockAtlas().height());
            }
            glPopMatrix();
        }
    }

    public void setupCamera(float delta) {
        double x = window.getWidth() * .5;
        double y = window.getHeight() * .5;
        glTranslated(x, y, 0);
        glScalef(32, 32, 1);
        player.prevPos.lerp(player.position, delta, player.lerpPos);
        glTranslated(
            -player.lerpPos.x(),
            -player.lerpPos.y(),
            0
        );
    }

    public void onKeyPress(int key, int scancode, int mods) {
        if (screen != null) {
            screen.keyPressed(key, scancode, mods);
        } else if (world != null) {
            switch (key) {
                case GLFW_KEY_ESCAPE -> {
                    openScreen(new PauseScreen(null, world.timer.timescale));
                    world.timer.timescale = 0.0;
                }
                case GLFW_KEY_ENTER -> world.save(player);
                case GLFW_KEY_1 -> player.hotBarNum = 0;
                case GLFW_KEY_2 -> player.hotBarNum = 1;
                case GLFW_KEY_3 -> player.hotBarNum = 2;
                case GLFW_KEY_4 -> player.hotBarNum = 3;
                case GLFW_KEY_5 -> player.hotBarNum = 4;
                case GLFW_KEY_6 -> player.hotBarNum = 5;
                case GLFW_KEY_7 -> player.hotBarNum = 6;
                case GLFW_KEY_8 -> player.hotBarNum = 7;
                case GLFW_KEY_9 -> player.hotBarNum = 8;
                case GLFW_KEY_0 -> player.hotBarNum = 9;
                case GLFW_KEY_F3 -> debugging = !debugging;
                // z || , || .
                case GLFW_KEY_Z, GLFW_KEY_COMMA, GLFW_KEY_PERIOD -> {
                    --world.pickZ;
                    if (world.pickZ < 0) {
                        world.pickZ = 1;
                    } else if (world.pickZ > 1) {
                        world.pickZ = 0;
                    }
                }
                case GLFW_KEY_G -> {
                    var entity = new HumanEntity(world);
                    entity.setPosition(player.position.x(), player.position.y(), player.position.z());
                    entity.prevPos.set(entity.position);
                    world.spawnEntity(entity);
                }
            }
            if (key == Options.getI(Options.KEY_CREATIVE_TAB, GLFW_KEY_E)) {
                openScreen(new CreativeTabScreen(player, null));
            }
        }
    }

    public void update() {
        if (world != null) {
            world.update();

            if (screen == null) {
                worldRenderer.processHit();
            }
        }
    }

    @Override
    public void run() {
        double lastTime = glfwGetTime();
        int frames = 0;
        while (!window.shouldClose()) {
            timer.update();
            for (int i = 0; i < timer.ticks; i++) {
                tick();
            }
            update();
            render((float) timer.partialTick);
            window.swapBuffers();
            glfwPollEvents();
            ++frames;
            while (glfwGetTime() >= lastTime + 1.) {
                fps = frames;
                lastTime += 1.;
                frames = 0;
            }
        }
    }

    public void render(float delta) {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, window.getWidth(), 0, window.getHeight(), 100, -100);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        if (world != null) {
            float worldDelta = (float) world.timer.partialTick;
            setupCamera(worldDelta);
            worldRenderer.render(worldDelta, mouse.getIntLastX(), window.getHeight() - mouse.getIntLastY());
        }
        final double sw = window.getWidth() * invGuiScale;
        final double sh = window.getHeight() * invGuiScale;
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, sw, sh, 0, 100, -100);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glClear(GL_DEPTH_BUFFER_BIT);
        renderHud((int) Math.ceil(sw), (int) Math.ceil(sh));
        if (screen != null) {
            screen.render((int) (mouse.getLastX() * invGuiScale),
                (int) (mouse.getLastY() * invGuiScale),
                delta);
        }
    }

    public void tick() {
        if (screen != null) {
            screen.tick();
        }
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public static Mc2dClient getInstance() {
        if (instance == null) {
            instance = new Mc2dClient();
        }
        return instance;
    }

    @Override
    public void close() {
        textureManager.close();
        PlayerEntity.model.close();
        HumanEntity.model.close();

        logger.info("Stopping!");
        window.destroy();
        glfwTerminate();
        var cb = glfwSetErrorCallback(null);
        if (cb != null) {
            cb.free();
        }
    }
}
