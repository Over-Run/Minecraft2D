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
import io.github.overrun.mc2d.client.Mouse;
import io.github.overrun.mc2d.client.Window;
import io.github.overrun.mc2d.event.KeyCallback;
import io.github.overrun.mc2d.item.Items;
import io.github.overrun.mc2d.mod.ModLoader;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.util.ImageReader;
import io.github.overrun.mc2d.util.Language;
import io.github.overrun.mc2d.util.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.Closeable;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.memUTF8;

/**
 * @author squid233
 * @since 2021/01/07
 */
public final class Main implements Runnable, Closeable {
    // todo mods screen

    public static final String VERSION = "0.5.0";
    public static final IText VERSION_TEXT = new LiteralText("Minecraft2D " + VERSION);
    private static final Logger logger = LogManager.getLogger(Main.class.getName());
    private static int oldX, oldY, oldWidth, oldHeight;
    private final Mc2dClient client = Mc2dClient.getInstance();

    @Override
    public void run() {
        logger.info("Loading for game Minecraft2D {}", VERSION);
        init();
        try {
            Class.forName(Items.class.getName());
        } catch (ClassNotFoundException e) {
            logger.catching(e);
        }
        ModLoader.loadMods();
        Language.init();
        Language.currentLang = Options.get("lang", "en_us");
        logger.info("Backend library: LWJGL version {}", Version.getVersion());
        glClearColor(.4f, .6f, .9f, 1);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        while (!Window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            client.render();
            client.tick();
            Window.swapBuffers();
            glfwPollEvents();
        }
    }

    private void init() {
        GLFWErrorCallback.create((error, description) -> {
            logger.error("########## GL ERROR ##########");
            logger.error("{}: {}", error, memUTF8(description));
        });
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        Window.create(896, 512, VERSION_TEXT.asString());
        Window.check();
        ImageReader.withGlfwImg("assets/mc2d/icon.png", Window::setIcon);
        Window.setKeyCallback((window, key, scancode, action, mods) -> {
            KeyCallback.post(window, key, scancode, action, mods);
            if (action == GLFW_PRESS) {
                client.screen.keyPressed(key, scancode, mods);
                if (key == GLFW_KEY_F11) {
                    GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                    if (glfwGetWindowMonitor(window) == 0) {
                        try (MemoryStack stack = MemoryStack.stackPush()) {
                            IntBuffer pX = stack.mallocInt(1);
                            IntBuffer pY = stack.mallocInt(1);
                            glfwGetWindowPos(window, pX, pY);
                            oldX = pX.get(0);
                            oldY = pY.get(0);
                        }
                        oldWidth = Window.width;
                        oldHeight = Window.height;
                        if (vidMode != null) {
                            glfwSetWindowMonitor(window,
                                    glfwGetPrimaryMonitor(),
                                    0,
                                    0,
                                    vidMode.width(),
                                    vidMode.height(),
                                    vidMode.refreshRate());
                        }
                    } else {
                        glfwSetWindowMonitor(window, 0, oldX, oldY, oldWidth, oldHeight, 0);
                    }
                }
            }
        });
        Window.setMouseButtonCallback((window, button, action, mods) -> {
            if (action == GLFW_PRESS) {
                client.screen.mousePressed(Mouse.mouseX, Mouse.mouseY, button);
            }
        });
        Window.setCloseCallback(window -> {
            if (client.world != null) {
                client.world.save();
            }
        });
        Window.setSizeCallback((window, width, height) -> resize(width, height));
        Window.setCursorPosCallback((window, x, y) -> {
            Mouse.mouseX = (int) Math.floor(x);
            Mouse.mouseY = (int) Math.floor(y);
        });
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            Window.getSize(pWidth, pHeight);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidMode != null) {
                Window.setPos(
                        vidMode.width() - pWidth.get(0) >> 1,
                        vidMode.height() - pHeight.get(0) >> 1
                );
            }
        }
        Window.makeContextCurrent();
        GL.createCapabilities();
        glfwSwapInterval(1);
        Window.show();
    }

    private void resize(int width, int height) {
        int w = Math.max(width, 1);
        int h = Math.max(height, 1);
        if (client.screen == null) {
            client.openScreen(null);
        }
        Window.width = w;
        Window.height = h;
        client.screen.init(client, width, height);
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
        Window.destroy();
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
