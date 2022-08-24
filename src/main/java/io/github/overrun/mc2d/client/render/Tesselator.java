/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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

package io.github.overrun.mc2d.client.render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 0.6.0
 */
public final class Tesselator {
    private static final int MEMORY_USE = 4 * 1024 * 1024;
    private static final int MAX_FLOATS = MEMORY_USE / 4;
    private static Tesselator instance;
    private final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_FLOATS);
    private float u, v, r, g, b;
    private int vertexCount = 0;
    private boolean hasColor, hasTexture;

    private Tesselator() {
    }

    public static Tesselator getInstance() {
        if (instance == null) {
            instance = new Tesselator();
        }
        return instance;
    }

    public Tesselator begin() {
        vertexCount = 0;
        buffer.clear();
        return this;
    }

    public Tesselator tex(float u, float v) {
        hasTexture = true;
        this.u = u;
        this.v = v;
        return this;
    }

    public Tesselator color(float r, float g, float b) {
        hasColor = true;
        this.r = r;
        this.g = g;
        this.b = b;
        return this;
    }

    public void vertex(float x, float y, float z) {
        if (hasTexture) {
            buffer.put(u).put(v);
        }
        if (hasColor) {
            buffer.put(r).put(g).put(b);
        }
        buffer.put(x).put(y).put(z);
        ++vertexCount;
    }

    public void flush(int mode) {
        buffer.flip();
        if (hasTexture) glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        if (hasColor) glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);

        if (hasColor) {
            if (hasTexture) {
                glInterleavedArrays(GL_T2F_C3F_V3F, 0, buffer);
            } else {
                glInterleavedArrays(GL_C3F_V3F, 0, buffer);
            }
        } else if (hasTexture) {
            glInterleavedArrays(GL_T2F_V3F, 0, buffer);
        } else {
            glInterleavedArrays(GL_V3F, 0, buffer);
        }

        glDrawArrays(mode, 0, vertexCount);

        if (hasTexture) glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        if (hasColor) glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
    }
}
