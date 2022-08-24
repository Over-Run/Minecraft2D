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

import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.util.Identifier;
import org.overrun.swgl.core.gl.GLStateMgr;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/26
 */
public final class TextRenderer {
    private static final Identifier UNIFONT_0 = new Identifier("textures/font/unifont_0.png");
    private final Mc2dClient client;

    public TextRenderer(Mc2dClient client) {
        this.client = client;
    }

    public void draw(int x, int y, char c, int rgba) {
        var r = rgba << 8 >>> 24;
        var g = rgba << 16 >>> 24;
        var b = rgba << 24 >>> 24;
        var a = rgba >>> 24;
        float inv = 1.0f / 255.0f;
        int width = width();
        int height = height();
        int u = c % 256 * width;
        int v = c / 256 * height;
        int texW = 4096;
        int texH = 4096;
        glColor4f(r * inv, g * inv, b * inv, a * inv);
        // Left top
        glTexCoord2f((float) u / texW, (float) v / texH);
        glVertex2d(x, y);
        // Left down
        glTexCoord2f((float) u / texW, (float) (v + height) / texH);
        glVertex2d(x, y + height);
        // Right down
        glTexCoord2f((float) (u + width) / texW, (float) (v + height) / texH);
        glVertex2d(x + width, y + height);
        // Right top
        glTexCoord2f((float) (u + width) / texW, (float) v / texH);
        glVertex2d(x + width, y);
    }

    public void drawWithShadow(int x, int y, IText text) {
        draw(x, y, text, true);
    }

    public void draw(int x, int y, IText text, boolean shadow) {
        final char[] chars = text.asString().toCharArray();
        int currX = x;
        client.getTextureManager().bindTexture(UNIFONT_0);
        GLStateMgr.enableTexture2D();
        glBegin(GL_QUADS);
        for (char c : chars) {
            if (shadow) {
                draw(currX + 1, y + 1, c, text.getStyle().getColor().bgColor());
            }
            draw(currX, y, c, text.getStyle().getColor().fgColor());
            currX += width(c);
        }
        glEnd();
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
        if (c > 31 && c < 256) return 8;
        return 16;
    }

    public int width() {
        return 16;
    }

    public int height() {
        return 16;
    }
}
