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

package io.github.overrun.mc2d;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.Mouse;
import io.github.overrun.mc2d.client.Window;
import io.github.overrun.mc2d.client.gui.Framebuffer;
import io.github.overrun.mc2d.client.model.BlockModelMgr;
import io.github.overrun.mc2d.event.KeyCallback;
import io.github.overrun.mc2d.mod.ModLoader;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.util.ImageReader;
import io.github.overrun.mc2d.util.Language;
import io.github.overrun.mc2d.util.Options;
import io.github.overrun.mc2d.world.block.Blocks;
import io.github.overrun.mc2d.world.item.Items;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.overrun.swgl.core.gl.GLBlendFunc;
import org.overrun.swgl.core.gl.GLClear;
import org.overrun.swgl.core.gl.GLStateMgr;
import org.overrun.swgl.core.util.LogFactory9;
import org.overrun.swgl.core.util.timing.Timer;
import org.slf4j.Logger;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author squid233
 * @since 2021/01/07
 */
public final class Main implements Runnable, AutoCloseable {
    // todo: mods and options screen

    public static final String VERSION = "0.6.0";
    public static final IText VERSION_TEXT = IText.of("Minecraft2D " + VERSION);
    private static final Logger logger = LogFactory9.getLogger();
    private static int oldX, oldY, oldWidth, oldHeight;
    private final Mc2dClient client = Mc2dClient.getInstance();
    private final Timer timer = new Timer(20);

    @Override
    public void run() {
        logger.info("Loading for game Minecraft2D {}", VERSION);
        init();
        Blocks.register();
        Items.register();
        ModLoader.loadMods();
        BlockModelMgr.loadAtlas();
        Language.init();
        Language.currentLang = Options.get("lang", "en_us");
        logger.info("Backend library: LWJGL version {}", Version.getVersion());
        GLClear.clearColor(.4f, .6f, .9f, 1);
        GLStateMgr.enableTexture2D();
        GLStateMgr.enableBlend();
        GLStateMgr.blendFunc(GLBlendFunc.SRC_ALPHA, GLBlendFunc.ONE_MINUS_SRC_ALPHA);
        long lastTime = System.currentTimeMillis();
        int frames = 0;
        while (!Window.shouldClose()) {
            timer.update();
            for (int i = 0; i < timer.ticks; i++) {
                client.tick();
            }
            client.update();
            client.render((float) timer.partialTick);
            Window.swapBuffers();
            glfwPollEvents();
            ++frames;
            while (System.currentTimeMillis() >= lastTime + 1000) {
                client.fps = frames;
                lastTime += 1000;
                frames = 0;
            }
        }
    }

    private void init() {
        GLFWErrorCallback.create((error, description) ->
            logger.error("GLFW Error {}: {}", error, GLFWErrorCallback.getDescription(description))
        ).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        Window.create(896, 512, VERSION_TEXT.asString());
        Window.check();
        try (var img = ImageReader.readImg("assets/mc2d/icon.png");
             var buf = GLFWImage.malloc(1)) {
            Window.setIcon(buf.width(img.width()).height(img.height()).pixels(img.buffer()));
        }
        Window.setKeyCallback((window, key, scancode, action, mods) -> {
            KeyCallback.post(window, key, scancode, action, mods);
            if (action == GLFW_PRESS) {
                if (key == GLFW_KEY_F11) {
                    var vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                    if (glfwGetWindowMonitor(window) == 0) {
                        try (MemoryStack stack = MemoryStack.stackPush()) {
                            IntBuffer pX = stack.mallocInt(1);
                            IntBuffer pY = stack.mallocInt(1);
                            glfwGetWindowPos(window, pX, pY);
                            oldX = pX.get(0);
                            oldY = pY.get(0);
                        }
                        oldWidth = Framebuffer.width;
                        oldHeight = Framebuffer.height;
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

                client.onKeyPress(key, scancode, mods);
            }
        });
        Window.setMouseButtonCallback((window, button, action, mods) -> {
            if (action == GLFW_PRESS) {
                if (client.screen != null) {
                    client.screen.mousePressed(Mouse.mouseX, Mouse.mouseY, button);
                }
            }
        });
        Window.setCloseCallback(window -> {
            if (client.world != null) {
                client.world.save(client.player);
            }
        });
        Window.setSizeCallback((window, width, height) -> client.onResize(width, height));
        Window.setFramebufferSizeCallback((window, width, height) -> Framebuffer.setSize(width, height));
        Window.setCursorPosCallback((window, x, y) -> {
            Mouse.mouseX = (int) Math.floor(x);
            Mouse.mouseY = (int) Math.floor(y);
        });
        try (var stack = MemoryStack.stackPush()) {
            var pWidth = stack.mallocInt(1);
            var pHeight = stack.mallocInt(1);
            Window.getSize(pWidth, pHeight);
            var vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidMode != null) {
                Window.setPos(
                    vidMode.width() - pWidth.get(0) >> 1,
                    vidMode.height() - pHeight.get(0) >> 1
                );
            }
        }
        Window.makeContextCurrent();
        GL.createCapabilities();
        GLStateMgr.init();
        glfwSwapInterval(Boolean.parseBoolean(System.getProperty("mc2d.vsync", "true")) ? 1 : 0);
        client.onResize(896, 512);
        Window.show();
    }

    @Override
    public void close() {
        client.close();
        logger.info("Stopping!");
        Window.destroy();
        glfwTerminate();
        var cb = glfwSetErrorCallback(null);
        if (cb != null) {
            cb.free();
        }
    }

    public static void main(String[] args) {
        Options.init();
        try (Main main = new Main()) {
            main.run();
        }
    }
}
