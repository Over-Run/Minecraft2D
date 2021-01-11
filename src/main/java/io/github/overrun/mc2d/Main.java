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
import io.github.overrun.mc2d.util.TextureDrawer;
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

import static io.github.overrun.mc2d.block.Blocks.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author squid233
 * @since 2021/01/07
 */
public final class Main {
    public static final String VERSION = "0.4.0";
    private static final Logger logger = LogManager.getLogger();
    private static final Byte2ObjectMap<Object2IntMap<String>> BLOCK_CUSTOM_MODELS = new Byte2ObjectArrayMap<>(1);
    private static final Main INSTANCE = new Main();
    private final Player player = new Player();
    private World world;
    private long window;

    private void run() {
        logger.info("Loading for game Minecraft2D {}", VERSION);
        init();
        GL.createCapabilities();
        resizeGl(896, 512);
        glClearColor(.4f, .6f, .9f, .1f);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            Dimension wSize = WindowUtils.getWindowSize(window);
            player.render(wSize.width, wSize.height);
            render();
            player.move(window, world);
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
        window = glfwCreateWindow(896, 512, "Minecraft2D " + VERSION, NULL, NULL);
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
                player.handledBlock = 1;
            }
            if (key == GLFW_KEY_2) {
                player.handledBlock = 2;
            }
            if (key == GLFW_KEY_3) {
                player.handledBlock = 3;
            }
            if (key == GLFW_KEY_4) {
                player.handledBlock = 4;
            }
        });
        glfwSetWindowCloseCallback(window, window -> world.save());
        glfwSetWindowSizeCallback(window, (window, width, height) -> resizeGl(width, height));
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
        world = new World(player, 64, 64);
        // generate the terrain
        for (int i = 0; i < world.getWidth(); i++) {
            world.setBlock(i, 0, BEDROCK);
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < world.getWidth(); j++) {
                world.setBlock(j, i + 1, COBBLESTONE);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < world.getWidth(); j++) {
                world.setBlock(j, i + 3, DIRT);
            }
        }
        for (int i = 0; i < world.getWidth(); i++) {
            world.setBlock(i, 5, GRASS_BLOCK);
        }
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void render() {
        Point p = WindowUtils.getMousePos(window);
        world.render(p.x, p.y);
        Dimension wSize = WindowUtils.getWindowSize(window);
        Properties model = new Properties(3);
        if (!BLOCK_CUSTOM_MODELS.containsKey(player.handledBlock)) {
            try (InputStream is = ClassLoader.getSystemResourceAsStream(
                    ModelManager.getModelPath(player.handledBlock) + ".txt")) {
                model.load(is);
                Object2IntMap<String> map = new Object2IntArrayMap<>(3);
                putModel(map, model, "top");
                putModel(map, model, "left");
                putModel(map, model, "right");
                BLOCK_CUSTOM_MODELS.put(player.handledBlock, map);
            } catch (Throwable ignored) { }
        }
        Object2IntMap<String> map = BLOCK_CUSTOM_MODELS.get(player.handledBlock);
        TextureDrawer.begin(getModelId(map, "top"))
                .color3f(1, 1, 1)
                .tex2dVertex2d(1, 0, 13, wSize.height - 21)
                .tex2dVertex2d(0, 0, 5, wSize.height - 13)
                //todo
                .tex2dVertex2d(1, 0, wSize.width - 69, wSize.height - 69)
                .tex2dVertex2d(0, 0, wSize.width - 5, wSize.height - 37)
                .tex2dVertex2d(0, 1, wSize.width - 69, wSize.height - 5)
                .tex2dVertex2d(1, 1, wSize.width - 133, wSize.height - 37)
                .bind(getModelId(map, "left"))
                .tex2dVertex2d(1, 0, wSize.width - 69, wSize.height - 69)
                .tex2dVertex2d(0, 0, wSize.width - 133, wSize.height - 37)
                .tex2dVertex2d(0, 1, wSize.width - 133, wSize.height - 101)
                .tex2dVertex2d(1, 1, wSize.width - 69, wSize.height - 133)
                .bind(getModelId(map, "right"))
                .tex2dVertex2d(0, 0, wSize.width - 69, wSize.height - 69)
                .tex2dVertex2d(0, 1, wSize.width - 69, wSize.height - 133)
                .tex2dVertex2d(1, 1, wSize.width - 5, wSize.height - 101)
                .tex2dVertex2d(1, 0, wSize.width - 5, wSize.height - 37)
                .end();
    }

    private void putModel(Object2IntMap<String> map, Properties model, String key) {
        String s = String.valueOf(model.getOrDefault(key, ModelManager.getModelPath(player.handledBlock)));
        map.put(key, ImageReader.loadTexture(s.contains(".png") ? s : s + ".png", GL_NEAREST));
    }

    private int getModelId(Object2IntMap<String> map, String key) {
        int id = ModelManager.getModelId(player.handledBlock);
        return map != null ? map.getOrDefault(key, id) : id;
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
