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

import io.github.overrun.mc2d.level.World;
import io.github.overrun.mc2d.util.ImageReader;
import io.github.overrun.mc2d.util.ModelManager;
import io.github.overrun.mc2d.util.WindowUtils;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.awt.Dimension;
import java.awt.Point;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Objects;
import java.util.Properties;

import static io.github.overrun.mc2d.block.Blocks.DIRT;
import static io.github.overrun.mc2d.block.Blocks.GRASS_BLOCK;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author squid233
 * @since 2021/01/07
 */
public final class Main {
    private static final Logger logger = LogManager.getLogger();
    private static final Byte2ObjectMap<Object2IntMap<String>> BLOCK_CUSTOM_MODELS = new Byte2ObjectArrayMap<>(1);
    private static final Main INSTANCE = new Main();
    private World world;
    private long window;

    private void run() {
        logger.info("Loading for game Minecraft2D 0.3.0");
        init();
        GL.createCapabilities();
        resizeGl(896, 512);
        glClearColor(.4f, .6f, .9f, .1f);
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        logger.info("Stopping!");
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        window = glfwCreateWindow(896, 512, "Minecraft2D 0.3.0", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        ImageReader.operateWithGlfwImg("icon.png",
                img -> glfwSetWindowIcon(window, img));
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                world.save();
                glfwSetWindowShouldClose(window, true);
            }
            if (key == GLFW_KEY_ENTER) {
                world.save();
            }
            if (key == GLFW_KEY_1) {
                Player.handledBlock = 1;
            }
            if (key == GLFW_KEY_2) {
                Player.handledBlock = 2;
            }
        });
        glfwSetWindowCloseCallback(window, window -> world.save());
        glfwSetWindowSizeCallback(window, (window, width, height) -> resizeGl(width, height));
        glfwSetMouseButtonCallback(window, (w, b, a, m) -> {
            Point p = WindowUtils.getMousePos(window);
            if (a == GLFW_PRESS) {
                world.mousePressed(b, p.x, p.y);
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
        world = new World(64, 64);
        // generate the terrain
        for (int j = 0; j < world.getWidth(); j++) {
            world.setBlock(j, 5, GRASS_BLOCK.getRawId());
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < world.getWidth(); j++) {
                world.setBlock(j, i, DIRT.getRawId());
            }
        }
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void render() {
        Point p = WindowUtils.getMousePos(window);
        world.render(p.x, p.y);
        Dimension wSize = WindowUtils.getWindowSize(window);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        Properties model = new Properties(3);
        if (!BLOCK_CUSTOM_MODELS.containsKey(Player.handledBlock)) {
            try (InputStream is = ClassLoader.getSystemResourceAsStream(
                    ModelManager.getModelPath(Player.handledBlock) + ".txt")) {
                model.load(is);
                Object2IntMap<String> map = new Object2IntArrayMap<>(3);
                String s = String.valueOf(model.getOrDefault("top", ModelManager.getModelPath(Player.handledBlock)));
                map.put("top", ImageReader.loadTexture(s.contains(".png") ? s : s + ".png", GL_NEAREST));
                s = String.valueOf(model.getOrDefault("left", ModelManager.getModelPath(Player.handledBlock)));
                map.put("left", ImageReader.loadTexture(s.contains(".png") ? s : s + ".png", GL_NEAREST));
                s = String.valueOf(model.getOrDefault("right", ModelManager.getModelPath(Player.handledBlock)));
                map.put("right", ImageReader.loadTexture(s.contains(".png") ? s : s + ".png", GL_NEAREST));
                BLOCK_CUSTOM_MODELS.put(Player.handledBlock, map);
            } catch (Throwable ignored) { }
        }
        Object2IntMap<String> map = BLOCK_CUSTOM_MODELS.get(Player.handledBlock);
        ImageReader.bindTexture(map != null
                ? map.getOrDefault("top", ModelManager.getModelId(Player.handledBlock))
                : ModelManager.getModelId(Player.handledBlock));
        glBegin(GL_QUADS);
        glColor3f(1, 1, 1);
        glTexCoord2f(0, 1); glVertex2f(wSize.width - 69,  wSize.height - 69);
        glTexCoord2f(1, 1); glVertex2f(wSize.width - 5,   wSize.height - 37);
        glTexCoord2f(1, 0); glVertex2f(wSize.width - 69,  wSize.height - 5);
        glTexCoord2f(0, 0); glVertex2f(wSize.width - 133, wSize.height - 37);
        glEnd();
        ImageReader.bindTexture(map != null
                ? map.getOrDefault("left", ModelManager.getModelId(Player.handledBlock))
                : ModelManager.getModelId(Player.handledBlock));
        glBegin(GL_QUADS);
        glTexCoord2f(1, 0); glVertex2f(wSize.width - 69,  wSize.height - 69);
        glTexCoord2f(0, 0); glVertex2f(wSize.width - 133, wSize.height - 37);
        glTexCoord2f(0, 1); glVertex2f(wSize.width - 133, wSize.height - 101);
        glTexCoord2f(1, 1); glVertex2f(wSize.width - 69,  wSize.height - 133);
        glEnd();
        ImageReader.bindTexture(map != null
                ? map.getOrDefault("right", ModelManager.getModelId(Player.handledBlock))
                : ModelManager.getModelId(Player.handledBlock));
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(wSize.width - 69,  wSize.height - 69);
        glTexCoord2f(0, 1); glVertex2f(wSize.width - 69,  wSize.height - 133);
        glTexCoord2f(1, 1); glVertex2f(wSize.width - 5,   wSize.height - 101);
        glTexCoord2f(1, 0); glVertex2f(wSize.width - 5,   wSize.height - 37);
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
    }

    private void resizeGl(int width, int height) {
        if (width != 0 && height != 0) {
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, width, 0, height, 1, -1);
            glMatrixMode(GL_MODELVIEW);
            glViewport(0, 0, width, height);
        }
    }

    public long getWindow() {
        return window;
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        Main.getInstance().run();
    }
}
