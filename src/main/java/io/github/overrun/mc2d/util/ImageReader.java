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

import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryStack;
import org.overrun.swgl.core.io.IFileProvider;
import org.overrun.swgl.core.util.LogFactory9;
import org.slf4j.Logger;

import java.nio.ByteBuffer;
import java.util.Objects;

import static org.lwjgl.stb.STBImage.STBI_rgb_alpha;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

/**
 * @author squid233
 * @since 2021/01/07
 */
public final class ImageReader {
    private static final Logger logger = LogFactory9.getLogger();

    @Nullable
    public static ByteBuffer read(String path, ClassLoader loader) {
        try (var is = Objects.requireNonNullElse(
            loader,
            ClassLoader.getSystemClassLoader()
        ).getResourceAsStream(path)) {
            if (is == null) {
                return null;
            }
            return IFileProvider.of(loader).res2BBWithRE(path, 8192);
        } catch (Throwable t) {
            logger.error("Catching reading image buffer", t);
            return null;
        }
    }

    /**
     * Read an image as {@link ByteBuffer}.
     *
     * @param path The image path.
     * @return Read {@link ByteBuffer}.
     */
    @Nullable
    public static ByteBuffer read(String path) {
        return read(path, ClassLoader.getSystemClassLoader());
    }

    public static NativeImage readImg(String path) {
        try (var stack = MemoryStack.stackPush()) {
            var img = read(path);
            if (img == null) {
                return new NativeImage(2, 2,
                    stack.malloc(16)
                        .putInt(0xfff800f8)
                        .putInt(0xff000000)
                        .putInt(0xff000000)
                        .putInt(0xfff800f8)
                        .flip(), false);
            }
            var xp = stack.mallocInt(1);
            var yp = stack.mallocInt(1);
            var cp = stack.mallocInt(1);
            var buf = stbi_load_from_memory(img, xp, yp, cp, STBI_rgb_alpha);
            return new NativeImage(xp.get(0), yp.get(0), buf, true);
        }
    }
}
