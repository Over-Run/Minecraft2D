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

package io.github.overrun.mc2d;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.WindowEventHandler;
import io.github.overrun.mc2d.event.KeyCallback;
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.util.ImageReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.Closeable;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memUTF8;

/**
 * @author squid233
 * @since 2021/01/07
 */
public final class Main implements Runnable, Closeable {
    public static final String VERSION = "0.4.0";
    private static final Logger logger = LogManager.getLogger();
    private static final List<WindowEventHandler> WINDOW_EVENT_HANDLERS = new ArrayList<>(1);
    private final Mc2dClient client = registerWindowEventHandler(Mc2dClient.getInstance());
    private long window;

    public <T extends WindowEventHandler> T registerWindowEventHandler(T handler) {
        WINDOW_EVENT_HANDLERS.add(handler);
        return handler;
    }

    @Override
    public void run() {
        logger.info("Loading for game Minecraft2D {}", VERSION);
        init();
        glClearColor(.4f, .6f, .9f, .1f);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            client.render();
            client.tick();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void init() {
        GLFWErrorCallback.create((error, description) -> {
            String desc = memUTF8(description);
            logger.error("########## GL ERROR ##########");
            logger.error("{}: {}", error, desc);
        });
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        window = glfwCreateWindow(896, 512, "Minecraft2D " + VERSION, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        ImageReader.withGlfwImg("assets/mc2d/icon.png",
                imgs -> glfwSetWindowIcon(window, imgs));
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            KeyCallback.post(new KeyCallback.Context(window, key, scancode, action, mods));
            if (action == GLFW_PRESS) {
                client.screen.keyPressed(key, scancode, mods);
            }
        });
        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if (action == GLFW_PRESS) {
                client.screen.mouseClicked(client.mouseX, client.mouseY, button);
            }
        });
        glfwSetWindowCloseCallback(window, window -> {
            if (client.world != null) {
                client.world.save();
            }
        });
        glfwSetWindowSizeCallback(window, (window, width, height) -> resize(width, height));
        glfwSetCursorPosCallback(window, (window, x, y) -> {
            for (WindowEventHandler handler : WINDOW_EVENT_HANDLERS) {
                handler.onMouseMove((int) Math.floor(x), (int) Math.floor(y));
            }
        });
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidMode != null) {
                glfwSetWindowPos(
                        window,
                        (vidMode.width() - pWidth.get(0)) >> 1,
                        (vidMode.height() - pHeight.get(0)) >> 1
                );
            }
        }
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        client.player = new Player();
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void resize(int width, int height) {
        int w = Math.max(width, 1);
        int h = Math.max(height, 1);
        if (client.screen == null) {
            client.openScreen(null);
        }
        for (WindowEventHandler handler : WINDOW_EVENT_HANDLERS) {
            handler.onResize(w, h);
        }
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, w, h, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glViewport(0, 0, w, h);
    }

    @Override
    public void close() {
        client.close();
        logger.info("Stopping!");
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        GLFWErrorCallback gec = glfwSetErrorCallback(null);
        if (gec != null) {
            gec.free();
        }
    }

    public static void main(String[] args) {
        Options.init();
        try (Main main = new Main()) {
            main.run();
        }
    }
}
