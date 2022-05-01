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

import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.overrun.swgl.core.util.LogFactory9;
import org.slf4j.Logger;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.function.Consumer;

import static org.lwjgl.stb.STBImage.STBI_rgb_alpha;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

/**
 * @author squid233
 * @since 2021/01/07
 */
public final class ImageReader {
    private static final Logger logger = LogFactory9.getLogger();

    public static byte[] read(String path, ClassLoader loader) {
        try (var is = Objects.requireNonNullElse(
            loader,
            ClassLoader.getSystemClassLoader()
        ).getResourceAsStream(path)) {
            if (is == null) {
                return null;
            }
            return is.readAllBytes();
        } catch (Throwable t) {
            logger.error("Catching", t);
            return null;
        }
    }

    /**
     * Read an image as {@link BufferedImage}.
     *
     * @param path The image path.
     * @return Read {@link BufferedImage}.
     */
    public static byte[] read(String path) {
        return read(path, ClassLoader.getSystemClassLoader());
    }

    public static Texture readImg(String path) {
        try (var stack = MemoryStack.stackPush()) {
            byte[] img = read(path);
            if (img == null) {
                return new Texture(2, 2,
                    stack.calloc(16)
                        .putInt(0xfff800f8)
                        .putInt(0xff000000)
                        .putInt(0xff000000)
                        .putInt(0xfff800f8)
                        .flip(), false);
            }
            var bb = MemoryUtil.memCalloc(img.length);
            bb.put(img).flip();
            var xp = stack.callocInt(1);
            var yp = stack.callocInt(1);
            var cp = stack.callocInt(1);
            var buf = stbi_load_from_memory(bb, xp, yp, cp, STBI_rgb_alpha);
            return new Texture(xp.get(0), yp.get(0), buf, true);
        }
    }

    /**
     * Read an image as {@link GLFWImage.Buffer}.
     * <p>You should call {@link GLFWImage.Buffer#free() free()} or
     * {@link GLFWImage.Buffer#close() close()} after use.</p>
     *
     * @param path The image path.
     * @return An image as {@link GLFWImage.Buffer}.
     */
    public static GLFWImage.Buffer readGlfwImg(String path) {
        try (final var buf = readImg(path)) {
            return GLFWImage.calloc(1).width(buf.getWidth()).height(buf.getHeight()).pixels(buf.getBuffer());
        }
    }

    /**
     * Execute some statements with an image.
     * <p>Will auto close the buffer.</p>
     *
     * @param img      The image buffer.
     * @param consumer The operator with {@code img}.
     * @see ImageReader#withGlfwImg(String, Consumer)
     */
    public static void withGlfwImg(GLFWImage.Buffer img, Consumer<GLFWImage.Buffer> consumer) {
        try (img) {
            consumer.accept(img);
        }
    }

    public static void withGlfwImg(String path, Consumer<GLFWImage.Buffer> consumer) {
        withGlfwImg(readGlfwImg(path), consumer);
    }
}
