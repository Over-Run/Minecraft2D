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

import io.github.overrun.mc2d.Main;
import io.github.overrun.mc2d.client.gui.Framebuffer;
import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.client.gui.screen.TitleScreen;
import io.github.overrun.mc2d.client.gui.screen.ingame.CreativeTabScreen;
import io.github.overrun.mc2d.client.gui.screen.ingame.PauseScreen;
import io.github.overrun.mc2d.client.world.render.WorldRenderer;
import io.github.overrun.mc2d.mod.ModLoader;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.TranslatableText;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.Options;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.world.entity.HumanEntity;
import io.github.overrun.mc2d.world.entity.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import static io.github.overrun.mc2d.client.gui.DrawableHelper.drawTexture;
import static io.github.overrun.mc2d.util.registry.Registry.BLOCK;
import static io.github.overrun.mc2d.world.block.Blocks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/25
 */
public final class Mc2dClient implements AutoCloseable {
    private static final Mc2dClient INSTANCE = new Mc2dClient();
    private static final IText MAX_MEMORY;
    public final TextRenderer textRenderer;
    private final TextureManager textureManager;
    public Screen screen = null;
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
        textRenderer = new TextRenderer(this);
        textureManager = new TextureManager();
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
                (int) Math.ceil(Framebuffer.width * invGuiScale),
                (int) Math.ceil(Framebuffer.height * invGuiScale));
        }
    }

    public void onResize(int width, int height) {
        if (screen == null) {
            openScreen(null);
        }
        Framebuffer.setSize(width, height);
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
                textRenderer.draw(0, 0, Main.VERSION_TEXT);
                textRenderer.draw(0, textRenderer.drawHeight() + 1, IText.of(fps + " fps"));
                textRenderer.draw(0, textRenderer.drawHeight() * 2 + 1, new TranslatableText("text.debug.player.position",
                    player.position.x,
                    player.position.y,
                    player.position.z));
                textRenderer.draw(width - textRenderer.drawWidth(MAX_MEMORY), 0, MAX_MEMORY);
                if (ModLoader.getModCount() > 0) {
                    textRenderer.draw(0, 119, new TranslatableText("text.debug.mod_count", ModLoader.getModCount()));
                }
            } else {
                glPushMatrix();
                glTranslatef(width - 32, 0, 0);
                Identifier id = BLOCK.getId(player.mainHand);
                glColor3f(1, 1, 1);
                textureManager.bindTexture(new Identifier(id.getNamespace(), "textures/block/" + id.getPath() + ".png"));
                drawTexture(0, 0, 32, 32);
                glPopMatrix();
            }
        }
    }

    public void setupCamera(float delta) {
        double x = Framebuffer.width * .5;
        double y = Framebuffer.height * .5;
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
                case GLFW_KEY_1 -> player.mainHand = GRASS_BLOCK;
                case GLFW_KEY_2 -> player.mainHand = STONE;
                case GLFW_KEY_3 -> player.mainHand = DIRT;
                case GLFW_KEY_4 -> player.mainHand = COBBLESTONE;
                case GLFW_KEY_5 -> player.mainHand = BEDROCK;
                case GLFW_KEY_6 -> player.mainHand = OAK_LOG;
                case GLFW_KEY_7 -> player.mainHand = OAK_LEAVES;
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

    public void render(float delta) {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Framebuffer.width, 0, Framebuffer.height, 100, -100);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        if (world != null) {
            float worldDelta = (float) world.timer.partialTick;
            setupCamera(worldDelta);
            worldRenderer.render(worldDelta, Mouse.mouseX, Framebuffer.height - Mouse.mouseY);
        }
        final double sw = Framebuffer.width * invGuiScale;
        final double sh = Framebuffer.height * invGuiScale;
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, sw, sh, 0, 100, -100);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glClear(GL_DEPTH_BUFFER_BIT);
        renderHud((int) Math.ceil(sw), (int) Math.ceil(sh));
        if (screen != null) {
            screen.render((int) (Mouse.mouseX * invGuiScale),
                (int) (Mouse.mouseY * invGuiScale),
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
        return INSTANCE;
    }

    @Override
    public void close() {
        textureManager.close();
        PlayerEntity.model.close();
        HumanEntity.model.close();
    }
}
