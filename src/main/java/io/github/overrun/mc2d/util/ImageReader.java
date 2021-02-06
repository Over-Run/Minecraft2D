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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author squid233
 * @since 2021/01/07
 */
public final class ImageReader {
    private static final Logger logger = LogManager.getLogger(ImageReader.class.getName());

    public static BufferedImage read(String path, ClassLoader loader) {
        try {
            return ImageIO.read(
                    Objects.requireNonNull(
                            Objects.requireNonNullElse(
                                    loader,
                                    ClassLoader.getSystemClassLoader()
                            ).getResource(path)
                    )
            );
        } catch (Throwable t) {
            logger.catching(t);
            return null;
        }
    }

    /**
     * Read an image as {@link BufferedImage}.
     *
     * @param path The image path.
     * @return Read {@link BufferedImage}.
     */
    public static BufferedImage read(String path) {
        return read(path, ClassLoader.getSystemClassLoader());
    }

    public static Texture readImg(String path) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            BufferedImage img = read(path);
            if (img == null) {
                return new Texture(2, 2, Utils.putInt(stack.malloc(16),
                        new int[]{
                                0xfff800f8, 0xff000000,
                                0xff000000, 0xfff800f8
                        }).flip());
            } else {
                int width = img.getWidth();
                int height = img.getHeight();
                ByteBuffer buf = BufferUtils.createByteBuffer(width * height << 2);
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        ColorModel cm = img.getColorModel();
                        Object o = img.getRaster().getDataElements(j, i, null);
                        buf.putInt(cm.getAlpha(o) << 24 | cm.getBlue(o) << 16 | cm.getGreen(o) << 8 | cm.getRed(o));
                    }
                }
                buf.flip();
                return new Texture(width, height, buf);
            }
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
        try (final GLFWImage image = GLFWImage.malloc()) {
            final Texture buf = readImg(path);
            return GLFWImage.malloc(1).put(0, image.set(buf.getWidth(), buf.getHeight(), buf.getBuffer()));
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
