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
import org.overrun.swgl.core.gl.GLStateMgr;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/25
 */
public abstract class DrawableHelper {
    public static final Identifier OPTIONS_BACKGROUND = new Identifier("textures/gui/options_background.png");

    public static void drawCenteredText(TextRenderer renderer, IText text, int centerX, int y) {
        renderer.drawWithShadow(centerX - renderer.drawWidth(text) / 2, y, text);
    }

    public static void fillGradient(int xStart, int yStart, int xEnd, int yEnd, int colorStart, int colorEnd) {
        GLStateMgr.disableTexture2D();
        glBegin(GL_QUADS);
        float inv = 1.0f / 255.0f;
        var sr = colorStart << 8 >>> 24;
        var sg = colorStart << 16 >>> 24;
        var sb = colorStart << 24 >>> 24;
        var sa = colorStart >>> 24;
        var er = colorEnd << 8 >>> 24;
        var eg = colorEnd << 16 >>> 24;
        var eb = colorEnd << 24 >>> 24;
        var ea = colorEnd >>> 24;
        var lerp = (colorStart + colorEnd) >> 1;
        var lr = (lerp << 8 >>> 24) * inv;
        var lg = (lerp << 16 >>> 24) * inv;
        var lb = (lerp << 24 >>> 24) * inv;
        var la = (lerp >>> 24) * inv;
        // Left top
        glColor4f(sr * inv, sg * inv, sb * inv, sa * inv);
        glVertex2f(xStart, yStart);
        // Left down
        glColor4f(lr, lg, lb, la);
        glVertex2f(xStart, yEnd);
        // Right down
        glColor4f(er * inv, eg * inv, eb * inv, ea * inv);
        glVertex2f(xEnd, yEnd);
        // Right top
        glColor4f(lr, lg, lb, la);
        glVertex2f(xEnd, yStart);
        glEnd();
        GLStateMgr.enableTexture2D();
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
        GLStateMgr.enableTexture2D();
        glBegin(GL_QUADS);
        var r = rgba << 8 >>> 24;
        var g = rgba << 16 >>> 24;
        var b = rgba << 24 >>> 24;
        var a = rgba >>> 24;
        float inv = 1.0f / 255.0f;
        glColor4f(r * inv, g * inv, b * inv, a * inv);
        // Left top
        glTexCoord2f((float) u / texW, (float) v / texH);
        glVertex2d(x, y);
        // Left down
        glTexCoord2f((float) u / texW, (float) (v + regionH) / texH);
        glVertex2d(x, y + height);
        // Right down
        glTexCoord2f((float) (u + regionW) / texW, (float) (v + regionH) / texH);
        glVertex2d(x + width, y + height);
        // Right top
        glTexCoord2f((float) (u + regionW) / texW, (float) v / texH);
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
                                   int height,
                                   int texW,
                                   int texH) {
        drawTexture(x, y, u, v, regionW, regionH, width, height, texW, texH, 0xffffffff);
    }

    public static void drawTextureFlip(double x,
                                       double y,
                                       double width,
                                       double height) {
        GLStateMgr.enableTexture2D();
        glBegin(GL_QUADS);
        // Left top
        glTexCoord2f(0, 0);
        glVertex2d(x, y + height);
        // Left down
        glTexCoord2f(0, 1);
        glVertex2d(x, y);
        // Right down
        glTexCoord2f(1, 1);
        glVertex2d(x + width, y);
        // Right top
        glTexCoord2f(1, 0);
        glVertex2d(x + width, y + height);
        glEnd();
    }

    public static void drawTexture(double x,
                                   double y,
                                   double width,
                                   double height) {
        GLStateMgr.enableTexture2D();
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
