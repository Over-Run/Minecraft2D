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

import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.level.World;
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.screen.CreativeTabScreen;
import io.github.overrun.mc2d.util.GlfwUtils;
import io.github.overrun.mc2d.util.ImageReader;
import io.github.overrun.mc2d.util.TextureDrawer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Objects;

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
    public static boolean openingGroup;
    private static final Logger logger = LogManager.getLogger();
    private final Player player = new Player();
    private World world;
    private long window;
    private int width = 896;
    private int height = 512;
    private int mouseX;
    private int mouseY;

    private void run() {
        logger.info("Loading for game Minecraft2D {}", VERSION);
        init();
        resize(width, height);
        glClearColor(.4f, .6f, .9f, .1f);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render();
            if (!openingGroup) {
                player.move();
            }
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
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        window = glfwCreateWindow(width, height, "Minecraft2D " + VERSION, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        ImageReader.withGlfwImg("icon.png",
                imgs -> glfwSetWindowIcon(window, imgs));
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                if (key == GLFW_KEY_ESCAPE) {
                    if (openingGroup) {
                        openingGroup = false;
                    } else {
                        world.save();
                        glfwSetWindowShouldClose(window, true);
                    }
                }
                if (key == (int) Options.get(Options.KEY_CREATIVE_TAB,
                        "E").toUpperCase().toCharArray()[0]) {
                    openingGroup = !openingGroup;
                    if (!openingGroup) {
                        GlfwUtils.setDefaultCursor();
                    }
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
            }
        });
        glfwSetWindowCloseCallback(window, window -> world.save());
        glfwSetWindowSizeCallback(window, (window, width, height) -> resize(width, height));
        glfwSetCursorPosCallback(window, (window, x, y) -> {
            mouseX = (int) Math.floor(x);
            mouseY = (int) Math.floor(y);
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
        world = new World(player, 64, 64);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GlfwUtils.setDefaultCursor();
    }

    private void render() {
        world.render(mouseX, mouseY, width, height);
        if (openingGroup) {
            CreativeTabScreen.render(mouseX, mouseY, height, player);
        } else {
            glPushMatrix();
            glTranslatef(width, height, 0);
            TextureDrawer.begin(ImageReader.loadTexture(
                    Blocks.RAW_ID_BLOCKS.get(player.handledBlock).toString() + ".png"))
                    .color4f(1, 1, 1, 1)
                    .tex2dVertex2d(0, 0, -64, 0)
                    .tex2dVertex2d(0, 1, -64, -64)
                    .tex2dVertex2d(1, 1, 0, -64)
                    .tex2dVertex2d(1, 0, 0, 0)
                    .end();
            glPopMatrix();
        }
    }

    private void resize(int width, int height) {
        int w = Math.max(width, 1);
        int h = Math.max(height, 1);
        CreativeTabScreen.init(w, h);
        this.width = w;
        this.height = h;
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, w, 0, h, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glViewport(0, 0, w, h);
    }

    public static void main(String[] args) {
        Options.init();
        new Main().run();
    }
}
