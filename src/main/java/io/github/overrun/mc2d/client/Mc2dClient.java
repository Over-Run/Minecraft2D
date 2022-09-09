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
import io.github.overrun.mc2d.client.gui.screen.ingame.ItemGroupsScreen;
import io.github.overrun.mc2d.client.gui.screen.ingame.PauseScreen;
import io.github.overrun.mc2d.client.gui.widget.AbstractButtonWidget;
import io.github.overrun.mc2d.client.model.BlockModelMgr;
import io.github.overrun.mc2d.client.render.ItemRenderer;
import io.github.overrun.mc2d.client.world.render.WorldRenderer;
import io.github.overrun.mc2d.event.KeyCallback;
import io.github.overrun.mc2d.mod.ModLoader;
import io.github.overrun.mc2d.screen.ItemGroupsScreenHandler;
import io.github.overrun.mc2d.screen.inv.ItemGroupsInventory;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.util.Language;
import io.github.overrun.mc2d.util.Options;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.world.block.Blocks;
import io.github.overrun.mc2d.world.entity.EntityTypes;
import io.github.overrun.mc2d.world.entity.HumanEntity;
import io.github.overrun.mc2d.world.entity.player.PlayerEntity;
import io.github.overrun.mc2d.world.item.BlockItemType;
import io.github.overrun.mc2d.world.item.ItemGroup;
import io.github.overrun.mc2d.world.item.ItemStack;
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

import java.io.IOException;

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
    public static final IText VERSION_TEXT = IText.literal("Minecraft2D " + GameVersion.versionString());
    private static final IText JAVA_TXT = IText.formatTranslatable("text.debug.java");
    private static final long maxMemory;
    private static final String maxMemoryStr;
    private static final IText MEM_TXT = IText.formatTranslatable("text.debug.mem");
    //    private static final IText CPU_TXT = IText.formatTranslatable("text.debug.cpu");
    private static final IText DISPLAY_0_TXT = IText.formatTranslatable("text.debug.display_0");
    private static final IText DISPLAY_1_TXT = IText.formatTranslatable("text.debug.display_1");
    private static final IText DISPLAY_2_TXT = IText.formatTranslatable("text.debug.display_2");
    private static int oldX, oldY, oldWidth, oldHeight;
    public final String glVendor, glRenderer, glVersion;
    public final TextRenderer textRenderer;
    private final TextureManager textureManager;
    public final ItemRenderer itemRenderer;
    public final Options options;
    public Window window;
    public Mouse mouse;
    public Screen screen = null;
    public final Timer timer = new Timer(20);
    public @Nullable World world = null;
    public WorldRenderer worldRenderer = null;
    public PlayerEntity player = null;
    public int fps = 0;
    public boolean debugging = false;
    public double guiScale, invGuiScale;

    static {
        maxMemory = Runtime.getRuntime().maxMemory();
        maxMemoryStr = maxMemory == Long.MAX_VALUE ? "no limit" : (maxMemory / 1048576) + "MB";
    }

    private Mc2dClient() {
        options = new Options();

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
                } else if (world != null) {
                    if (button == GLFW_MOUSE_BUTTON_MIDDLE &&
                        !worldRenderer.hitResult.miss &&
                        !worldRenderer.hitResult.block.isAir()) {
                        int id = Slot.HOT_BAR_ID0 + player.hotBarNum;
                        var invStack = player.inventory.getStack(id);
                        // has item on main-hand
                        if (!invStack.isEmpty()) {
                            int invSelect;
                            if ((invSelect = player.inventory.indexOf(worldRenderer.hitResult.block)) > -1) {
                                if (invSelect >= Slot.CONTAINER_ID0) {
                                    // swap
                                    player.inventory.setStack(id, player.inventory.getStack(invSelect).copy());
                                    player.inventory.removeStack(invSelect);
                                } else {
                                    player.hotBarNum = invSelect - Slot.HOT_BAR_ID0;
                                }
                            } else /* is creative mode */ {
                                for (int i = 0; i < 10; i++) {
                                    if (player.inventory.getStack(Slot.HOT_BAR_ID0 + i).isEmpty()) {
                                        player.inventory.setStack(Slot.HOT_BAR_ID0 + i, ItemStack.of(worldRenderer.hitResult.block));
                                        player.hotBarNum = i;
                                        break;
                                    }
                                }
                            }
                        } else {
                            int invSelect;
                            if ((invSelect = player.inventory.indexOf(worldRenderer.hitResult.block)) > -1) {
                                if (invSelect >= Slot.CONTAINER_ID0) {
                                    // swap
                                    player.inventory.setStack(id, player.inventory.getStack(invSelect).copy());
                                    player.inventory.removeStack(invSelect);
                                } else {
                                    player.hotBarNum = invSelect - Slot.HOT_BAR_ID0;
                                }
                            } else {
                                player.inventory.setStack(id, ItemStack.of(worldRenderer.hitResult.block));
                            }
                        }
                    }
                }
            }
        });
        glfwSetWindowCloseCallback(window.getHandle(), handle -> {
            if (world != null) {
                world.save();
            }
        });
        window.setFBResizeCb((handle, width, height) -> onResize(width, height));
        window.setScrollCb((handle, xo, yo) -> {
            if (world != null && player != null && screen == null) {
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
        glVendor = glGetString(GL_VENDOR);
        glRenderer = glGetString(GL_RENDERER);
        glVersion = glGetString(GL_VERSION);
        glfwSwapInterval(Boolean.parseBoolean(System.getProperty("mc2d.vsync", "true")) ? 1 : 0);
        window.show();

        textRenderer = new TextRenderer(this);
        textureManager = new TextureManager();
        itemRenderer = new ItemRenderer(this);

        guiScale = options.getD(Options.GUI_SCALE, 2.0);
        invGuiScale = 1 / guiScale;
    }

    public void init() {
        onResize(window.getWidth(), window.getHeight());

        Blocks.register();
        Items.register();
        for (var e : ItemGroup.ITEM_GROUP) {
            e.getValue().addStacks();
        }
        EntityTypes.register();
        ModLoader.loadMods();
        BlockModelMgr.loadAtlas();
        Language.init();
        Language.currentLang = options.get("lang", "en_us");
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
            screen.onOpen();
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

    private void renderHotBar(int width, int height) {
        textureManager.bindTexture(AbstractButtonWidget.WIDGETS_LOCATION);
        glPushMatrix();
        double hotBarX = (width - 202) / 2.;
        glTranslated(hotBarX, height, 0);
        drawTexture(0, -23, 0, 0, 202, 22);
        drawTexture(player.hotBarNum * 20, -24, 1, 22, 22, 24);
        for (int i = 0; i < 10; i++) {
            var stack = player.inventory.getStack(Slot.HOT_BAR_ID0 + i);
            if (!(stack.getItem() instanceof BlockItemType)) continue;
            itemRenderer.renderItemStack(textRenderer, stack, 3 + i * 20, -20);
        }
        glPopMatrix();
    }

    public void renderHud(int width, int height) {
        if (world != null) {
            if (debugging) {
                final int lineH = textRenderer.drawHeight() + 1;
                textRenderer.draw(0, 0, VERSION_TEXT);
                textRenderer.draw(0, lineH, IText.literal(fps + " fps"));
                textRenderer.draw(0, lineH * 3, IText.formatTranslatable("text.debug.player.position"),
                    player.position.x,
                    player.position.y,
                    player.position.z);
                textRenderer.draw(0, lineH * 4, IText.formatTranslatable("text.debug.player.block_position"),
                    (int) Math.floor(player.position.x),
                    (int) Math.floor(player.position.y),
                    (int) Math.floor(player.position.z));
                if (ModLoader.getModCount() > 0) {
                    textRenderer.draw(0, lineH * 6, IText.formatTranslatable("text.debug.mod_count"), ModLoader.getModCount());
                }

                glPushMatrix();
                glTranslatef(width, 0, 0);
                textRenderer.drawAtRight(0, 0,
                    JAVA_TXT,
                    false,
                    Runtime.version());
                long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                textRenderer.drawAtRight(0, textRenderer.drawHeight() + 1,
                    MEM_TXT,
                    false,
                    usedMem * 100 / maxMemory, usedMem / 1048576, maxMemoryStr);

//                textRenderer.drawAtRight(0, lineH * 3, CPU_TXT, false);

                textRenderer.drawAtRight(0, lineH * 5,
                    DISPLAY_0_TXT,
                    false,
                    window.getWidth(), window.getHeight(), glVendor);
                textRenderer.drawAtRight(0, lineH * 6,
                    DISPLAY_1_TXT,
                    false,
                    glRenderer);
                textRenderer.drawAtRight(0, lineH * 7,
                    DISPLAY_2_TXT,
                    false,
                    glVersion);

                if (!worldRenderer.hitResult.miss) {
                    textRenderer.drawAtRight(0, lineH * 9,
                        IText.formatTranslatable("text.debug.target_block"),
                        false,
                        worldRenderer.hitResult.x,
                        worldRenderer.hitResult.y,
                        worldRenderer.hitResult.z);
                    textRenderer.drawAtRight(0, lineH * 10,
                        IText.literal(worldRenderer.hitResult.block.getId().toString()),
                        false
                    );
                }
                glPopMatrix();
            }
            renderHotBar(width, height);
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
                case GLFW_KEY_ENTER -> world.save();
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
                    var entity = world.spawnEntity(EntityTypes.HUMAN);
                    entity.teleport(player.position.x(), player.position.y(), player.position.z());
                    entity.prevPos.set(entity.position);
                }
            }
            if (key == options.getI(Options.KEY_ITEM_GROUP, GLFW_KEY_E)) {
                openScreen(new ItemGroupsScreen(new ItemGroupsScreenHandler(player.inventory, new ItemGroupsInventory()), player.inventory, null));
            }
        }
    }

    public void update() {
        if (world != null) {
            //todo:remove
            if (screen == null) {
                if (Keyboard.isKeyPress(GLFW_KEY_G)) {
                    var entity = world.spawnEntity(EntityTypes.HUMAN);
                    entity.teleport(player.position.x(), player.position.y(), player.position.z());
                    entity.prevPos.set(entity.position);
                }
            }
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
        logger.info("Stopping!");

        if (world != null) {
            world.save();
        }
        textureManager.close();
        PlayerEntity.model.close();
        HumanEntity.model.close();

        for (var instances : ModLoader.getMods().values()) {
            try {
                instances.classLoader().close();
            } catch (IOException e) {
                logger.error("Catching closing mod", e);
            }
        }

        window.destroy();
        glfwTerminate();
        var cb = glfwSetErrorCallback(null);
        if (cb != null) {
            cb.free();
        }
    }
}
