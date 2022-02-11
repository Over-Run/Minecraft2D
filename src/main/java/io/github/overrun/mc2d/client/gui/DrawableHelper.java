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

package io.github.overrun.mc2d.client.gui;

import io.github.overrun.mc2d.client.TextRenderer;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.util.Identifier;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/25
 */
public abstract class DrawableHelper {
    public static final Identifier OPTIONS_BACKGROUND = new Identifier("textures/gui/options_background.png");

    public static void drawCenteredText(TextRenderer renderer, IText text, int centerX, int y) {
        renderer.drawWithShadow(centerX - (renderer.width(text) >> 1), y, text);
    }

    public static void fillGradient(int xStart, int yStart, int xEnd, int yEnd, int colorStart, int colorEnd) {
        glDisable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);
        var sr = colorStart << 8 >>> 24;
        var sg = colorStart << 16 >>> 24;
        var sb = colorStart << 24 >>> 24;
        var sa = colorStart >>> 24;
        var er = colorEnd << 8 >>> 24;
        var eg = colorEnd << 16 >>> 24;
        var eb = colorEnd << 24 >>> 24;
        var ea = colorEnd >>> 24;
        var lerp = (colorStart + colorEnd) >> 1;
        var lr = (lerp << 8 >>> 24) / 255f;
        var lg = (lerp << 16 >>> 24) / 255f;
        var lb = (lerp << 24 >>> 24) / 255f;
        var la = (lerp >>> 24) / 255f;
        // Left top
        glColor4f(sr / 255f, sg / 255f, sb / 255f, sa / 255f);
        glVertex2f(xStart, yStart);
        // Left down
        glColor4f(lr, lg, lb, la);
        glVertex2f(xStart, yEnd);
        // Right down
        glColor4f(er / 255f, eg / 255f, eb / 255f, ea / 255f);
        glVertex2f(xEnd, yEnd);
        // Right top
        glColor4f(lr, lg, lb, la);
        glVertex2f(xEnd, yStart);
        glEnd();
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawTexture(double x,
                                   double y,
                                   int u,
                                   int v,
                                   int regionW,
                                   int regionH,
                                   int width,
                                   int height,
                                   int texW,
                                   int texH,
                                   int rgba) {
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);
        var r = rgba << 8 >>> 24;
        var g = rgba << 16 >>> 24;
        var b = rgba << 24 >>> 24;
        var a = rgba >>> 24;
        glColor4f(r / 255f, g / 255f, b / 255f, a / 255f);
        // Left top
        glTexCoord2f((float) u / texW, (float) v / texH);
        glVertex2d(x, y);
        // Left down
        glTexCoord2f((float) u / texW, (float) (v + regionH) / texH);
        glVertex2d(x, y + (height << 1));
        // Right down
        glTexCoord2f((float) (u + regionW) / texW, (float) (v + regionH) / texH);
        glVertex2d(x + (width << 1), y + (height << 1));
        // Right top
        glTexCoord2f((float) (u + regionW) / texW, (float) v / texH);
        glVertex2d(x + (width << 1), y);
        glEnd();
    }

    public static void drawTexture(double x,
                                   double y,
                                   int u,
                                   int v,
                                   int regionW,
                                   int regionH,
                                   int width,
                                   int height,
                                   int texW,
                                   int texH) {
        drawTexture(x, y, u, v, regionW, regionH, width, height, texW, texH, 0xffffffff);
    }

    public static void drawTexture(double x,
                                   double y,
                                   double width,
                                   double height) {
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);
        // Left top
        glTexCoord2f(0, 0);
        glVertex2d(x, y);
        // Left down
        glTexCoord2f(0, 1);
        glVertex2d(x, y + height);
        // Right down
        glTexCoord2f(1, 1);
        glVertex2d(x + width, y + height);
        // Right top
        glTexCoord2f(1, 0);
        glVertex2d(x + width, y);
        glEnd();
    }

    public static void drawTexture(double x,
                                   double y,
                                   int u,
                                   int v,
                                   int regionW,
                                   int regionH,
                                   int width,
                                   int height) {
        drawTexture(x, y, u, v, regionW, regionH, width, height, 256, 256);
    }

    public static void drawTexture(double x,
                                   double y,
                                   int u,
                                   int v,
                                   int width,
                                   int height) {
        drawTexture(x, y, u, v, width, height, width, height);
    }
}
