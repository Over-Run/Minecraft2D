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

package io.github.overrun.mc2d.client;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author squid233
 * @since 2021/01/31
 */
@ApiStatus.Internal
public final class Window {
    /** The window handler */
    public static long handler;
    public static int width;
    public static int height;

    public static void create(int width, int height, String title) {
        handler = glfwCreateWindow(width, height, title, 0, 0);
    }

    @Contract(pure = true)
    public static void check() {
        if (handler == 0) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }
    }

    public static boolean shouldClose() {
        return glfwWindowShouldClose(handler);
    }

    public static void swapBuffers() {
        glfwSwapBuffers(handler);
    }

    public static void setIcon(@Nullable GLFWImage.Buffer images) {
        glfwSetWindowIcon(handler, images);
    }

    @Nullable
    public static GLFWKeyCallback setKeyCallback(@Nullable GLFWKeyCallbackI callback) {
        return glfwSetKeyCallback(handler, callback);
    }

    @Nullable
    public static GLFWMouseButtonCallback setMouseButtonCallback(@Nullable GLFWMouseButtonCallbackI callback) {
        return glfwSetMouseButtonCallback(handler, callback);
    }

    @Nullable
    public static GLFWWindowCloseCallback setCloseCallback(@Nullable GLFWWindowCloseCallbackI callback) {
        return glfwSetWindowCloseCallback(handler, callback);
    }

    @Nullable
    public static GLFWWindowSizeCallback setSizeCallback(@Nullable GLFWWindowSizeCallbackI callback) {
        return glfwSetWindowSizeCallback(handler, callback);
    }

    @Nullable
    public static GLFWCursorPosCallback setCursorPosCallback(@Nullable GLFWCursorPosCallbackI callback) {
        return glfwSetCursorPosCallback(handler, callback);
    }

    @Nullable
    public static GLFWFramebufferSizeCallback setFramebufferSizeCallback(@Nullable GLFWFramebufferSizeCallbackI callback) {
        return glfwSetFramebufferSizeCallback(handler, callback);
    }

    public static void getSize(@Nullable IntBuffer width, @Nullable IntBuffer height) {
        glfwGetWindowSize(handler, width, height);
    }

    public static void setPos(int x, int y) {
        glfwSetWindowPos(handler, x, y);
    }

    public static void makeContextCurrent() {
        glfwMakeContextCurrent(handler);
    }

    public static void show() {
        glfwShowWindow(handler);
    }

    public static void freeCallbacks() {
        glfwFreeCallbacks(handler);
    }

    public static void destroy() {
        freeCallbacks();
        glfwDestroyWindow(handler);
    }
}
