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

import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.text.IText;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 * @author squid233
 * @since 2020/09/15
 */
public interface Screen {
    /**
     * get screen width
     *
     * @return width
     */
    int getWidth();

    /**
     * get screen height
     *
     * @return height
     */
    int getHeight();

    /**
     * draw image to screen
     *
     * @param g graphics
     * @param img image
     * @param x pos x
     * @param y pos y
     */
    default void drawImage(Graphics g, Image img, int x, int y) {
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
    default void drawImage(Graphics g, Image img, int x, int y, int width, int height) {
        g.drawImage(img, x + 8, y + 30, width, height, null);
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
    default void drawRect(Graphics g, int x, int y, int width, int height, Color color) {
        Color c = g.getColor();
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(c);
    }

    /**
     * draw background
     *
     * @param g graphics
     * @param color color
     */
    default void drawBackground(Graphics g, Color color) {
        drawRect(g, 0, 0, getWidth(), getHeight(), color);
    }

    /**
     * draw text to screen
     *
     * @param g graphics
     * @param x pos x
     * @param y pos y
     * @param text text
     */
    default void drawText(Graphics g, int x, int y, IText text) {
        char[] chars = text.asFormattedString().toCharArray();
        int pos = 0;
        for (char c : chars) {
            drawImage(g, Images.getAsciiInMap(c), x + pos, y);
            pos += Images.ASCII_IMAGE_WIDTH.get(c);
        }
    }
}
