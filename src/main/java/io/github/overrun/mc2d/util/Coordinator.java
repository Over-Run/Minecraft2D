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

package io.github.overrun.mc2d.util;

import io.github.overrun.mc2d.client.Mc2dClient;
import org.intellij.lang.annotations.MagicConstant;

import java.awt.Point;

/**
 * <div style="font-family:jetbrains mono,consolas,monospace">
 * |-----------------|<br>
 * | U_L | U_M | U_R |<br>
 * | M_L | M_M | M_R |<br>
 * | D_L | D_M | D_R |<br>
 * |-----------------|</div>
 * @author squid233
 * @since 2020/11/24
 */
public final class Coordinator {
    public static final int U_L = 0;
    public static final int U_M = 1;
    public static final int U_R = 2;
    public static final int M_L = 3;
    public static final int M_M = 4;
    public static final int M_R = 5;
    public static final int D_L = 6;
    public static final int D_M = 7;
    public static final int D_R = 8;
    public static final int CENTER = M_M;

    public static Point transformation(
            int x, int y,
            @MagicConstant(valuesFromClass = Coordinator.class) int type) {
        Point pp;
        final Mc2dClient c = Mc2dClient.getInstance();
        if (type == U_L) {
            pp = new Point(x + 8, y + 30);
        } else if (type == U_M) {
            pp = new Point((c.getWidth() >> 1) + x, y + 30);
        } else if (type == U_R) {
            pp = new Point(c.getWidth() - 8 - x, y + 30);
        } else if (type == M_L) {
            pp = new Point(x + 8, (c.getHeight() - 22 >> 1) + y);
        } else if (type == M_M) {
            pp = new Point((c.getWidth() - 22 >> 1) + x,
                    (c.getHeight() >> 1) + y);
        } else if (type == M_R) {
            pp = new Point(c.getWidth() - 22 - 8 - x,
                    (c.getHeight() >> 1) + y);
        } else if (type == D_L) {
            pp = new Point(x + 8, c.getHeight() - 8 - y);
        } else if (type == D_M) {
            pp = new Point((c.getWidth() >> 1) + x,
                    c.getHeight() - 8 - y);
        } else if (type == D_R) {
            pp = new Point(c.getWidth() - 8 - x,
                    c.getHeight() - 8 - y);
        } else {
            pp = new Point();
        }
        return pp;
    }

    public static boolean isUp(int type) {
        return type >= U_L && type <= U_R;
    }

    public static boolean isCenter(int type) {
        return type >= M_L && type <= M_R;
    }

    public static boolean isDown(int type) {
        return type >= D_R && type <= D_R;
    }
}
