/*
 * MIT License
 *
 * Copyright (c) 2020 Over-Run
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

package io.github.overrun.mc2d.screen;

import io.github.overrun.mc2d.Minecraft2D;
import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.util.Highlight;
import io.github.overrun.mc2d.util.MathHelper;
import io.github.overrun.mc2d.util.ResourceLocation;
import io.github.overrun.mc2d.util.collection.DefaultedList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * @author squid233
 * @since 2020/10/01
 */
public abstract class ScreenHandler implements Screen {
    public static final Color DEFAULT_BACKGROUND = new Color(64, 64, 64, 128);
    public static final Color SLOT_HIGHLIGHT = new Color(255, 255, 255, 160);

    private final ObjectList<ButtonWidget> buttons = new ObjectArrayList<>();
    protected DefaultedList<Slot> slots = new DefaultedList<>();

    protected static void drawOptionsBg(Graphics g) {
        for (int i = 0, height = MathHelper.ceilDivision(Minecraft2D.getHeight(), 16); i < height; i++) {
            for (int j = 0, width = MathHelper.ceilDivision(Minecraft2D.getWidth(), 16); j < width; j++) {
                Screen.drawImage(g, Images.OPTIONS_BG, j << 4, i << 4, 16);
            }
        }
    }

    protected static void drawDefaultBackground(Graphics g) {
        Screen.drawBg(g, DEFAULT_BACKGROUND);
    }

    public void render(Graphics g) {
        for (ButtonWidget button : buttons) {
            IText text = button.getText();
            int width = button.getWidth();
            Screen.drawImage(g, button.getTexture(), button.getX(), button.getY(), width, button.getHeight());
            Screen.drawText(g, button.getX() + (width >> 1) - (text.getDisplayLength() >> 1), button.getY() + 20, text);
        }
    }

    public void onMousePressed(MouseEvent e) {
        for (int i = buttons.size() - 1; i >= 0; i--) {
            ButtonWidget button = buttons.get(i);
            if (
                    e.getX() > button.getX() + 8
                    && e.getX() < button.getX() + button.getWidth() + 8
                    && e.getY() > button.getY() + 30
                    && e.getY() < button.getY() + 30 + button.getHeight()
            ) {
                button.getAction().onPress(button);
                break;
            }
        }
    }

    public void onMouseMoved(Graphics g) {
        // For each all buttons to bottom from upper
        for (int i = buttons.size() - 1; i >= 0; i--) {
            ButtonWidget button = buttons.get(i);
            int x = Minecraft2D.getMouseX();
            int y = Minecraft2D.getMouseY();
            if (
                    x > button.getX() + 8
                    && x < button.getX() + button.getWidth() + 8
                    && y > button.getY() + 30
                    && y < button.getY() + 30 + button.getHeight()
            ) {
                Highlight.highlight(g, button.getX() + 8, button.getY() + 30, button.getWidth(), button.getHeight(), Color.WHITE);
                break;
            }
        }
    }

    protected void addButton(ButtonWidget button) {
        buttons.add(button);
    }

    protected void addSlot(Slot slot) {
        slots.add(slot);
    }

    /**
     * can open this screen handler
     *
     * @return can use
     */
    public boolean canUse() {
        return true;
    }

    /**
     * return this screen handler's texture
     *
     * @return identifier
     */
    public abstract ResourceLocation getTexture();
}
