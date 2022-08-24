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

package io.github.overrun.mc2d.util;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/08
 */
public final class GlUtils {
    /**
     * Draw a rect.
     * <p>If {@code alpha} is {@code true}, you should
     * {@link GL11#glEnable(int) enable}
     * {@link GL11#GL_BLEND blend} before using.</p>
     *
     * @param x1    The left top coord x.
     * @param y1    The left top coord y.
     * @param x2    The right bottom coord x.
     * @param y2    The right bottom coord y.
     * @param color The color. ARGB if {@code alpha} is {@code true}, else RGB.
     * @param alpha The alpha value.
     */
    public static void drawRect(double x1, double y1, double x2, double y2, int color, boolean alpha) {
        glBegin(GL_LINE_LOOP);
        var r = color << 8 >>> 24;
        var g = color << 16 >>> 24;
        var b = color << 24 >>> 24;
        final float inv = 1f / 255f;
        if (alpha) {
            glColor4f(r * inv, g * inv, b * inv, (color >>> 24) * inv);
        } else {
            glColor3f(r * inv, g * inv, b * inv);
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

    public static void fillRect(double x1, double y1, double x2, double y2, int color, boolean alpha) {
        glBegin(GL_QUADS);
        var r = color << 8 >>> 24;
        var g = color << 16 >>> 24;
        var b = color << 24 >>> 24;
        if (alpha) {
            glColor4f(r / 255f, g / 255f, b / 255f, (color >>> 24) / 255f);
        } else {
            glColor3f(r / 255f, g / 255f, b / 255f);
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
}
