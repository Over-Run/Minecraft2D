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

import org.lwjgl.system.MemoryUtil;
import org.overrun.swgl.core.gl.GLBatch;
import org.overrun.swgl.core.model.BuiltinVertexLayouts;

import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 0.6.0
 */
public final class Tesselator {
    private static Tesselator instance;
    private final GLBatch batch = new GLBatch(false);
    private int primitives = GL_TRIANGLES;

    private Tesselator() {
    }

    public static Tesselator getInstance() {
        if (instance == null) {
            instance = new Tesselator();
        }
        return instance;
    }

    public GLBatch getBatch() {
        return batch;
    }

    public Tesselator begin(int primitives) {
        batch.begin(BuiltinVertexLayouts.T2F_C3F_V3F());
        this.primitives = primitives;
        return this;
    }

    public Tesselator performBatch(Consumer<GLBatch> consumer) {
        consumer.accept(getBatch());
        return this;
    }

    public void flush() {
        batch.end();
        glEnableClientState(GL_VERTEX_ARRAY);
        if (batch.hasColor()) glEnableClientState(GL_COLOR_ARRAY);
        if (batch.hasTexture()) glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glInterleavedArrays(GL_T2F_C3F_V3F, 0, batch.getBuffer());
        batch.getIndexBuffer().ifPresentOrElse(
            buffer -> glDrawElements(GL_UNSIGNED_INT, batch.getIndexCount(), GL_UNSIGNED_INT, MemoryUtil.memAddress(buffer)),
            () -> glDrawArrays(primitives, 0, batch.getVertexCount())
        );

        glDisableClientState(GL_VERTEX_ARRAY);
        if (batch.hasColor()) glDisableClientState(GL_COLOR_ARRAY);
        if (batch.hasTexture()) glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }
}
