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

package io.github.overrun.mc2d.client.gui;

import io.github.overrun.mc2d.client.font.TextRenderer;
import io.github.overrun.mc2d.util.GlUtils;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/25
 */
public abstract class DrawableHelper {
    public static void drawCenteredText(TextRenderer renderer, String text, int centerX, int y, int rgba) {
        renderer.draw(centerX - (text.length() << 3), y, text, rgba);
    }

    public static void fillGradient(int xStart, int yStart, int xEnd, int yEnd, int colorStart, int colorEnd) {
        glDisable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);
        Color lt = new Color(colorStart, true);
        Color rt = new Color((colorStart + colorEnd) >> 1, true);
        Color rd = new Color(colorEnd, true);
        // Left top
        GlUtils.glColor4f(lt);
        glVertex2f(xStart, yStart);
        // Left down
        GlUtils.glColor4f(rt);
        glVertex2f(xStart, yEnd);
        // Right down
        GlUtils.glColor4f(rd);
        glVertex2f(xEnd, yEnd);
        // Right top
        GlUtils.glColor4f(rt);
        glVertex2f(xEnd, yStart);
        glEnd();
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawTexture(int x,
                                   int y,
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
        GlUtils.glColor4f(new Color(rgba, true));
        // Left top
        glTexCoord2f((float) u / texW, (float) v / texH);
        glVertex2f(x, y);
        // Left down
        glTexCoord2f((float) u / texW, (float) (v + regionH) / texH);
        glVertex2f(x, y + (height << 1));
        // Right down
        glTexCoord2f((float) (u + regionW) / texW, (float) (v + regionH) / texH);
        glVertex2f(x + (width << 1), y + (height << 1));
        // Right top
        glTexCoord2f((float) (u + regionW) / texW, (float) v / texH);
        glVertex2f(x + (width << 1), y);
        glEnd();
    }

    public static void drawTexture(int x,
                                   int y,
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
        glColor4f(1, 1, 1, 1);
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

    public static void drawTexture(int x,
                                   int y,
                                   int u,
                                   int v,
                                   int regionW,
                                   int regionH,
                                   int width,
                                   int height) {
        drawTexture(x, y, u, v, regionW, regionH, width, height, 256, 256);
    }

    public static void drawTexture(int x,
                                   int y,
                                   int u,
                                   int v,
                                   int width,
                                   int height) {
        drawTexture(x, y, u, v, width, height, width, height);
    }
}
