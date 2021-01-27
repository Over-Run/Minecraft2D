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

package io.github.overrun.mc2d.util;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author squid233
 * @since 2021/01/08
 */
public final class GlUtils {
    public static void generateMipmap(int target,
                                      int components,
                                      int width,
                                      int height,
                                      int format,
                                      int type,
                                      ByteBuffer pixels) {
        // If support to generate mipmap, use
        if (GL.getCapabilities().glGenerateMipmap != NULL) {
            glGenerateMipmap(target);
        }
        else {
            glTexImage2D(target, 0, components, width, height, 0, format, type, pixels);
        }
    }

    /**
     * Draw a rect.
     * <p>If {@code alpha} is {@code true}, you should
     * {@link GL11#glEnable(int) enable}
     * {@link GL11#GL_BLEND blend} before using.</p>
     *
     * @param x1 The left top coord x.
     * @param y1 The left top coord y.
     * @param x2 The right bottom coord x.
     * @param y2 The right bottom coord y.
     * @param color The color. RGBA if {@code alpha} is {@code true}, else RGB.
     * @param alpha The alpha value.
     */
    public static void drawRect(double x1, double y1, double x2, double y2, int color, boolean alpha) {
        glBegin(GL_LINE_STRIP);
        Color c = new Color(color, alpha);
        if (alpha) {
            glColor4f(c);
        } else {
            glColor3f(c);
        }
        // Left top
        glVertex2d(x1, y1 + 1);
        // Left down
        glVertex2d(x1 + 1, y2);
        // Right down
        glVertex2d(x2 - 1, y2);
        // Right up
        glVertex2d(x2, y1 + 1);
        // Origin point
        glVertex2d(x1, y1 + 1);
        glEnd();
    }

    public static void fillRect(double x1, double y1, double x2, double y2, int color, boolean alpha) {
        glBegin(GL_QUADS);
        Color c = new Color(color, alpha);
        if (alpha) {
            glColor4f(c);
        } else {
            glColor3f(c);
        }
        // Left top
        glVertex2d(x1, y1);
        // Left down
        glVertex2d(x1, y2);
        // Right down
        glVertex2d(x2, y2);
        // Right up
        glVertex2d(x2, y1);
        glEnd();
    }

    public static void glColor4f(Color c) {
        GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
    }

    public static void glColor3f(Color c) {
        GL11.glColor3f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
    }
}
