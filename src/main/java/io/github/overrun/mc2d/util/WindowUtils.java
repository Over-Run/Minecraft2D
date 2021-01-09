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

import org.lwjgl.glfw.GLFW;

import java.awt.Dimension;
import java.awt.Point;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.BufferUtils.createDoubleBuffer;
import static org.lwjgl.BufferUtils.createIntBuffer;

/**
 * @author squid233
 * @since 2021/01/08
 */
public final class WindowUtils {
    /**
     * You may convert the position.
     *
     * @param window The window handler.
     * @return The mouse position relative top and left.
     */
    public static Point getMousePos(long window) {
        DoubleBuffer x = createDoubleBuffer(1), y = createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(window, x, y);
        return new Point((int) Math.floor(x.get(0)), (int) Math.floor(y.get(0)));
    }

    public static Dimension getWindowSize(long window) {
        IntBuffer w = createIntBuffer(1), h = createIntBuffer(1);
        GLFW.glfwGetWindowSize(window, w, h);
        return new Dimension(w.get(0), h.get(0));
    }
}
