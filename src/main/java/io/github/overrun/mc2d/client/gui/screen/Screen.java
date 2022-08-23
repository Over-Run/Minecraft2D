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

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.TextRenderer;
import io.github.overrun.mc2d.client.gui.AbstractParentElement;
import io.github.overrun.mc2d.client.gui.Drawable;
import io.github.overrun.mc2d.client.gui.Element;
import io.github.overrun.mc2d.client.gui.widget.AbstractButtonWidget;
import io.github.overrun.mc2d.text.IText;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glColor4f;

/**
 * @author squid233
 * @since 2021/01/25
 */
public abstract class Screen extends AbstractParentElement implements TickableElement, Drawable {
    protected final List<Element> children = new ArrayList<>();
    public final IText title;
    protected Mc2dClient client;
    protected TextRenderer textRenderer;
    public int width;
    public int height;
    protected final List<AbstractButtonWidget> buttons = new ArrayList<>();

    protected Screen(IText title) {
        this.title = title;
    }

    public void init(Mc2dClient client, int width, int height) {
        this.client = client;
        textRenderer = client.textRenderer;
        this.width = width;
        this.height = height;
        buttons.clear();
        children.clear();
        init();
    }

    protected void init() {
    }

    @Override
    public void tick() {
    }

    public void removed(Screen newScreen) {
    }

    public void renderBackground() {
        if (client.world != null) {
            fillGradient(0, 0, width, height, 0xc0101010, 0xd0101010);
        } else {
            renderBackgroundTexture();
        }
    }

    public void renderBackgroundTexture() {
        glColor4f(1, 1, 1, 1);
        client.getTextureManager().bindTexture(OPTIONS_BACKGROUND);
        for (int i = 0; i < height; i += 32) {
            for (int j = 0; j < width; j += 32) {
                drawTexture(j, i, 32, 32);
            }
        }
    }

    public void onClose() {
    }

    public boolean shouldCloseOnEsc() {
        return true;
    }

    protected <T extends AbstractButtonWidget> T addButton(T button) {
        buttons.add(button);
        return addChild(button);
    }

    protected <T extends Element> T addChild(T child) {
        children.add(child);
        return child;
    }

    @Override
    public List<? extends Element> children() {
        return children;
    }

    @Override
    public boolean keyPressed(int key, int scancode, int mods) {
        if (key == GLFW.GLFW_KEY_ESCAPE && shouldCloseOnEsc()) {
            onClose();
            return true;
        }
        return super.keyPressed(key, scancode, mods);
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int button) {
        for (AbstractButtonWidget b : buttons) {
            if (b.isMouseOver(mouseX, mouseY) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                b.onClick(mouseX, mouseY);
            }
        }
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        for (AbstractButtonWidget button : buttons) {
            button.render(mouseX, mouseY, delta);
        }
    }
}
