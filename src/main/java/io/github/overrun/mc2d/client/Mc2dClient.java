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
import io.github.overrun.mc2d.world.Player;
import io.github.overrun.mc2d.client.gui.Framebuffer;
import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.client.gui.screen.TitleScreen;
import io.github.overrun.mc2d.client.world.render.WorldRenderer;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.mod.ModLoader;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;

import static io.github.overrun.mc2d.client.gui.DrawableHelper.drawTexture;
import static io.github.overrun.mc2d.util.registry.Registry.BLOCK;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/25
 */
public final class Mc2dClient implements Closeable {
    private static final Mc2dClient INSTANCE = new Mc2dClient();
    private static final IText MAX_MEMORY;
    public final TextRenderer textRenderer;
    private final TextureManager textureManager;
    public Screen screen;
    public @Nullable World world;
    public WorldRenderer worldRenderer;
    public Player player;
    public int fps;
    public boolean debugging;

    static {
        double maxMemory = Runtime.getRuntime().maxMemory() / 1048576D;
        MAX_MEMORY = new TranslatableText("Max.memory", maxMemory >= 1024 ? maxMemory / 1024D + " GB" : maxMemory + " MB");
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
            screen.init(this, Framebuffer.width, Framebuffer.height);
        }
    }

    public void renderHud() {
        if (world != null) {
            if (debugging) {
                textRenderer.draw(0, 0, Main.VERSION_TEXT);
                textRenderer.draw(0, 17, IText.of(fps + " fps"));
                textRenderer.draw(0, 34, new TranslatableText("player.position",
                    player.position.x,
                    player.position.y,
                    player.position.z));
                textRenderer.draw(0, 51, new TranslatableText("player.hand.block", player.handledBlock));
//            textRenderer.draw(0, 68, new TranslatableText("Point.block.pos", pointBlockX, pointBlockY, pointBlockZ));
//            textRenderer.draw(0, 85, new TranslatableText("Point.block", pointBlock));
                textRenderer.draw(0, 102, MAX_MEMORY);
                if (ModLoader.getModCount() > 0) {
                    textRenderer.draw(0, 119, new TranslatableText("mods.count", ModLoader.getModCount()));
                }
            }

            glPushMatrix();
            glTranslatef(Framebuffer.width - 64, 0, 0);
            Identifier id = BLOCK.getId(player.handledBlock);
            glColor3f(1, 1, 1);
            textureManager.bindTexture(new Identifier(id.getNamespace(), "textures/block/" + id.getPath() + ".png"));
            drawTexture(0, 0, 64, 64);
            glPopMatrix();
        }
    }

    public void render(float delta) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        if (world != null) {
            worldRenderer.render(Mouse.mouseX, Mouse.mouseY);
        }
        glClear(GL_DEPTH_BUFFER_BIT);
        renderHud();
        if (screen != null) {
            screen.render(Mouse.mouseX, Mouse.mouseY, delta);
        }
    }

    public void tick() {
        if (screen != null) {
            screen.tick();
        }
        if (player != null) {
            player.tick();
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
    }
}
