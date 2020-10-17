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
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.Style;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.util.function.Consumer;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class ScreenUtil {
    /**
     * draw image to screen
     *
     * @param g graphics
     * @param img image
     * @param x pos x
     * @param y pos y
     */
    public static void drawImage(Graphics g, Image img, int x, int y) {
        g.drawImage(img, x + 8, y + 30, null);
    }

    /**
     * draw image to screen
     *
     * @param g graphics
     * @param img image
     * @param x pos x
     * @param y pos y
     * @param width img width
     * @param height img height
     */
    public static void drawImage(Graphics g, Image img, int x, int y, int width, int height) {
        g.drawImage(img, x + 8, y + 30, width, height, null);
    }

    public static void drawImage(Graphics g, Image img, int x, int y, int size) {
        drawImage(g, img, x, y, size, size);
    }

    /**
     * draw rect to screen
     *
     * @param g graphics
     * @param x pos x
     * @param y pos y
     * @param width rect width
     * @param height rect height
     * @param color color
     */
    public static void drawRect(Graphics g, int x, int y, int width, int height, Color color) {
        operationWithColor(g, color, (gg) -> gg.fillRect(x, y, width, height));
    }

    public static void drawBg(Graphics g, Color color) {
        drawRect(g, 0, 0, Minecraft2D.getWidth(), Minecraft2D.getHeight(), color);
    }

    public static void drawText(Graphics g, int x, int y, IText text, int size) {
        Font font = g.getFont();
        for (String nm : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            boolean available = nm != null && (nm.contains("minecraft") || nm.contains("宋体") || nm.contains("serif"));
            if (available) {
                Style style = text.getStyle();
                if (style.isBold()) {
                    if (style.isItalic()) {
                        g.setFont(new Font(nm, Font.BOLD | Font.ITALIC, size));
                    } else {
                        g.setFont(new Font(nm, Font.BOLD, size));
                    }
                } else if (style.isItalic()) {
                    if (style.isBold()) {
                        g.setFont(new Font(nm, Font.ITALIC | Font.BOLD, size));
                    } else {
                        g.setFont(new Font(nm, Font.ITALIC, size));
                    }
                } else {
                    g.setFont(new Font(nm, Font.PLAIN, size));
                }
                break;
            }
        }
        operationWithColor(g, text.getStyle().getColor(), (gg) -> gg.drawString(text.asFormattedString(), x + 8, y + 30));
        g.setFont(font);
    }

    /**
     * Draw text to screen.
     *
     * @param g graphics
     * @param x pos x
     * @param y pos y
     * @param text The text. It can {@link io.github.overrun.mc2d.text.LiteralText LiteralText} now.
     */
    public static void drawText(Graphics g, int x, int y, IText text) {
        drawText(g, x, y, text, 16);
    }

    public static void drawText(Graphics g, int y, IText text) {
        drawText(g, (Minecraft2D.getWidth() >> 1) - (text.getDisplayLength() >> 1) - 8, y, text);
    }

    public static void operationWithColor(Graphics g, Color c, Consumer<Graphics> consumer) {
        Color cc = g.getColor();
        g.setColor(c);
        consumer.accept(g);
        g.setColor(cc);
    }
}
