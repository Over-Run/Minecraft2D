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

package io.github.overrun.mc2d.client;

import io.github.overrun.mc2d.mod.ModLoader;
import io.github.overrun.mc2d.util.Identifier;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.overrun.swgl.core.gl.GLStateMgr;
import org.overrun.swgl.core.util.LogFactory9;
import org.slf4j.Logger;

import java.nio.ByteBuffer;

import static io.github.overrun.mc2d.util.ImageReader.read;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * @author squid233
 * @since 2021/01/25
 */
public final class TextureManager implements AutoCloseable {
    private static final Logger logger = LogFactory9.getLogger();
    private final Object2IntMap<Identifier> idMap = new Object2IntOpenHashMap<>(16);

    public int loadTexture(Identifier id) {
        return loadTexture(id, GL_NEAREST);
    }

    public void addTexture(Identifier id, int texId) {
        idMap.put(id, texId);
    }

    public int loadTexture(Identifier id, int mode) {
        if (idMap.containsKey(id)) {
            return idMap.getInt(id);
        }
        int texId = glGenTextures();
        GLStateMgr.bindTexture2D(texId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, mode);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mode);
        String path = "assets/" + id.getNamespace() + "/" + id.getPath();
        var img = read(path,
            id.isVanilla()
                ? ClassLoader.getSystemClassLoader()
                : ModLoader.getLoader(id.getNamespace()));
        ByteBuffer pixels;
        int w, h;
        boolean failed;
        if (img == null) {
            failed = true;
            w = h = 2;
            pixels = MemoryUtil.memAlloc(64)
                .putInt(0xfff800f8).putInt(0xff000000)
                .putInt(0xff000000).putInt(0xfff800f8)
                .flip();
        } else {
            try (var stack = MemoryStack.stackPush()) {
                var xp = stack.mallocInt(1);
                var yp = stack.mallocInt(1);
                var cp = stack.mallocInt(1);
                pixels = stbi_load_from_memory(img, xp, yp, cp, STBI_rgb_alpha);
                if (pixels == null) {
                    logger.error("Failed to load image {}: {}",
                        path,
                        stbi_failure_reason());
                    pixels = MemoryUtil.memAlloc(64)
                        .putInt(0xfff800f8).putInt(0xff000000)
                        .putInt(0xff000000).putInt(0xfff800f8)
                        .flip();
                    failed = true;
                    w = h = 2;
                } else {
                    failed = false;
                    w = xp.get(0);
                    h = yp.get(0);
                }
            }
        }

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        if (failed) MemoryUtil.memFree(pixels);
        else stbi_image_free(pixels);

        addTexture(id, texId);
        return texId;
    }

    public void bindTexture(int texId) {
        GLStateMgr.bindTexture2D(texId);
    }

    public void bindTexture(Identifier id) {
        bindTexture(loadTexture(id));
    }

    @Override
    public void close() {
        for (int id : idMap.values()) {
            glDeleteTextures(id);
        }
        idMap.clear();
    }
}
