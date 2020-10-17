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
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.util.MathHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author squid233
 * @since 2020/10/16
 */
public abstract class Screen {
    public static final Color DEFAULT_BACKGROUND = new Color(64, 64, 64, 128);
    protected final ObjectList<ScreenComp> child = new ObjectArrayList<>();
    protected final ObjectList<ButtonWidget> buttons = new ObjectArrayList<>();

    protected static void drawOptionsBg(Graphics g) {
        for (int i = 0, height = MathHelper.ceilDivision(Minecraft2D.getHeight(), 16); i < height; i++) {
            for (int j = 0, width = MathHelper.ceilDivision(Minecraft2D.getWidth(), 16); j < width; j++) {
                ScreenUtil.drawImage(g, Images.OPTIONS_BG, j << 4, i << 4, 16);
            }
        }
    }

    protected static void drawDefaultBackground(Graphics g) {
        ScreenUtil.drawBg(g, DEFAULT_BACKGROUND);
    }

    public void render(Graphics g) {
        for (ScreenComp comp : child) {
            if (comp instanceof ButtonWidget) {
                ButtonWidget button = (ButtonWidget) comp;
                IText text = button.getText();
                int width = button.getWidth();
                ScreenUtil.drawImage(g, button.getTexture(),
                        button.getX(), button.getY(),
                        width, button.getHeight());
                ScreenUtil.drawText(g, button.getX() + (width >> 1) - (text.getDisplayLength() >> 1), button.getY() + 20, text);
            } else if (comp instanceof ComboBox) {
                ((ComboBox) comp).render(g);
            }
        }
    }

    public void onMouseReleased(MouseEvent e) {
        for (int i = child.size() - 1; i >= 0; i--) {
            ScreenComp comp = child.get(i);
            if (comp instanceof ButtonWidget) {
                ButtonWidget button = (ButtonWidget) comp;
                if (
                        e.getX() > button.getX() + 8
                                && e.getX() < button.getX() + button.getWidth() + 8
                                && e.getY() > button.getY() + 30
                                && e.getY() < button.getY() + 30 + button.getHeight()
                ) {
                    button.getAction().onPress(button);
                    break;
                }
            } else if (comp instanceof ComboBox) {
                ComboBox box = (ComboBox) comp;
                for (int j = 0; j < box.getTexts().size(); j++) {
                }
            }
        }
    }

    public void onMouseWheelMoved(MouseWheelEvent e) {}

    public void onMouseMoved(Graphics g) {
        // For each all buttons to bottom from upper
        for (int i = buttons.size() - 1; i >= 0; i--) {
            ButtonWidget button = buttons.get(i);
            int x = Minecraft2D.getMouseX();
            int y = Minecraft2D.getMouseY();
            button.isHover = x > button.getX() + 8
                    && x < button.getX() + button.getWidth() + 8
                    && y > button.getY() + 30
                    && y < button.getY() + 30 + button.getHeight();
        }
    }

    protected ButtonWidget addButton(ButtonWidget button) {
        buttons.add(button);
        return (ButtonWidget) addChild(button);
    }

    protected ScreenComp addChild(ScreenComp comp) {
        child.add(comp);
        return comp;
    }
}
