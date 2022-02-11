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

package io.github.overrun.mc2d.client.gui.screen;

import io.github.overrun.mc2d.Main;
import io.github.overrun.mc2d.Player;
import io.github.overrun.mc2d.client.gui.screen.world.LoadingWorldScreen;
import io.github.overrun.mc2d.client.gui.widget.ButtonWidget;
import io.github.overrun.mc2d.client.world.render.WorldRenderer;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.TranslatableText;
import io.github.overrun.mc2d.util.Identifier;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.glColor4f;

/**
 * @author squid233
 * @since 2021/01/25
 */
public final class TitleScreen extends Screen {
    public static final Identifier LOGO = new Identifier("textures/gui/logo.png");

    public TitleScreen() {
        super(IText.EMPTY);
    }

    @Override
    protected void init() {
        super.init();
        addButton(new ButtonWidget(width - 400 >> 1,
            (height - 40 >> 1) - 50,
            200,
            20,
            new TranslatableText("Play"),
            b -> {
                client.player = new Player();
                client.world = new World(client.player, 64, 64);
                client.worldRenderer = new WorldRenderer(client.world);
                client.openScreen(new LoadingWorldScreen());
            }));
        addButton(new ButtonWidget(width - 400 >> 1,
            height - 40 >> 1,
            200,
            20,
            new TranslatableText("Mods"),
            b -> {
            })).active = false;
        addButton(new ButtonWidget(width - 400 >> 1,
            (height - 40 >> 1) + 50,
            200,
            20,
            new TranslatableText("Exit.Game"),
            b -> glfwSetWindowShouldClose(glfwGetCurrentContext(), true)));
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        renderBackground();
        super.render(mouseX, mouseY, delta);
        glColor4f(1, 1, 1, 1);
        client.getTextureManager().bindTexture(LOGO);
        drawTexture((width >> 1) - 202, 15, 404, 84);
        textRenderer.draw(0, height - 16, Main.VERSION_TEXT);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
