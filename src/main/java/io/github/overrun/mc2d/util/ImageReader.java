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

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/07
 */
public final class ImageReader {
    private static final Object2IntMap<String> ID_MAP = new Object2IntOpenHashMap<>(16);
    private static int lastId = -999999;
    private static final Logger logger = LogManager.getLogger();

    /**
     * Read an image as {@link BufferedImage}.
     *
     * @param path The image path.
     * @return Read {@link BufferedImage}.
     */
    public static BufferedImage read(String path) {
        try {
            return ImageIO.read(ClassLoader.getSystemResource(path));
        } catch (IOException e) {
            logger.catching(e);
            return new BufferedImage(0, 0, 6);
        }
    }

    public static Texture readImg(String path) {
        BufferedImage img = read(path);
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

    /**
     * Read an image as {@link GLFWImage.Buffer}.
     * <p>You should call {@link GLFWImage.Buffer#free() free()} or
     * {@link GLFWImage.Buffer#close() close()} before use.</p>
     *
     * @param path The image path.
     * @return An image as {@link GLFWImage.Buffer}.
     */
    public static GLFWImage.Buffer readGlfwImg(String path) {
        try (GLFWImage image = GLFWImage.malloc()) {
            Texture buf = readImg(path);
            image.set(buf.getWidth(), buf.getHeight(), buf.getBuffer());
            GLFWImage.Buffer images = GLFWImage.malloc(1);
            images.put(0, image);
            return images;
        }
    }

    public static int loadTexture(String path, int mode) {
        if (ID_MAP.containsKey(path)) {
            return ID_MAP.getInt(path);
        } else {
            IntBuffer ib = BufferUtils.createIntBuffer(1);
            glGenTextures(ib);
            int id = ib.get(0);
            bindTexture(id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, mode);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mode);
            GLFWImage.Buffer buf = readGlfwImg(path);
            int w = buf.width();
            int h = buf.height();
            GlUtils.generateMipmap(GL_TEXTURE_2D, w, h, buf.pixels(w * h << 2));
            ID_MAP.put(path, id);
            return id;
        }
    }

    public static void bindTexture(int id) {
        if (lastId != id) {
            glBindTexture(GL_TEXTURE_2D, id);
            lastId = id;
        }
    }

    /**
     * Execute some statements with an image.
     * <p>Will auto close the buffer.</p>
     *
     * @param img      The image buffer.
     * @param consumer The operator with {@code img}.
     * @see ImageReader#operateWithGlfwImg(String, Consumer)
     */
    public static void operateWithGlfwImg(GLFWImage.Buffer img, Consumer<GLFWImage.Buffer> consumer) {
        consumer.accept(img);
        img.free();
    }

    public static void operateWithGlfwImg(String path, Consumer<GLFWImage.Buffer> consumer) {
        operateWithGlfwImg(readGlfwImg(path), consumer);
    }
}
