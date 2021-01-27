/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Over-Run
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

package io.github.overrun.mc2d.client.font;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.gui.DrawableHelper;
import io.github.overrun.mc2d.text.TextColor;
import io.github.overrun.mc2d.util.Identifier;

/**
 * @author squid233
 * @since 2021/01/26
 */
public final class TextRenderer {
    public static final Identifier ASCII_FONT_ID = new Identifier("textures/font/ascii.png");
    private static final AsciiFont ASCII_FONT = new AsciiFont();
    private final Mc2dClient client;

    public TextRenderer(Mc2dClient client) {
        this.client = client;
    }

    public void draw(int x, int y, char c, int rgba) {
        if (ASCII_FONT.contains(c)) {
            client.getTextureManager().bindTexture(ASCII_FONT_ID);
            DrawableHelper.drawTexture(x,
                    y,
                    (int) c * width(c),
                    0,
                    width(c),
                    height(c),
                    width(c),
                    height(c),
                    ASCII_FONT.textureWidth(),
                    ASCII_FONT.textureHeight(),
                    rgba);
        }
    }

    public void draw(int x, int y, String text) {
        draw(x, y, text, TextColor.WHITE);
    }

    public void draw(int x, int y, String text, int rgba) {
        char[] chars = text.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            draw(x + i * width(chars[i]) * 2, y, chars[i], rgba);
        }
    }

    public int width(char c) {
        int width = 0;
        if (ASCII_FONT.contains(c)) {
            width = ASCII_FONT.width();
        }
        return width;
    }

    public int height(char c) {
        int height = 0;
        if (ASCII_FONT.contains(c)) {
            height = ASCII_FONT.height();
        }
        return height;
    }
}
