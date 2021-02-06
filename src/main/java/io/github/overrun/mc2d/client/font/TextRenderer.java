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
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.util.Identifier;

/**
 * @author squid233
 * @since 2021/01/26
 */
public final class TextRenderer {
    private final Mc2dClient client;

    public TextRenderer(Mc2dClient client) {
        this.client = client;
    }

    public void draw(int x, int y, char c, int rgba) {
        client.getTextureManager().bindTexture(new Identifier("textures/font/utf8_" + (int) c / 625 + ".png"));
        int charIndex = (int) c % 625;
        DrawableHelper.drawTexture(x,
                y,
                charIndex % 25 * width(c),
                charIndex / 25 * height(),
                width(c),
                height(),
                width(c) >> 1,
                height() >> 1,
                400,
                400,
                rgba);
    }

    public void drawWithShadow(int x, int y, IText text) {
        draw(x, y, text, true);
    }

    public void draw(int x, int y, IText text, boolean shadow) {
        final char[] chars = text.asString().toCharArray();
        int finalX = x;
        for (char c : chars) {
            if (shadow) {
                draw(finalX + 1, y + 1, c, text.getStyle().getColor().getBgColor());
            }
            draw(finalX, y, c, text.getStyle().getColor().getFgColor());
            finalX += width(c);
        }
    }

    public void draw(int x, int y, IText text) {
        draw(x, y, text, false);
    }

    public int width(IText text) {
        int width = 0;
        for (char c : text.asString().toCharArray()) {
            width += width(c);
        }
        return width;
    }

    public int width(char c) {
        return c < 256 ? 8 : 16;
    }

    public int height() {
        return 16;
    }
}
