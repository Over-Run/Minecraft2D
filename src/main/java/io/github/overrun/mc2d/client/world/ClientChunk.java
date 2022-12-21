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

package io.github.overrun.mc2d.client.world;

import io.github.overrun.mc2d.client.render.VertexLayouts;
import io.github.overrun.mc2d.world.Chunk;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.world.entity.player.PlayerEntity;
import org.overrun.swgl.core.gl.GLFixedBatch;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author squid233
 * @since 0.6.0
 */
public class ClientChunk extends Chunk {
    private static final int LAYERS = 2;
    private final World world;
    private final double x, y;
    private final int x0, y0, x1, y1;
    private final GLFixedBatch[] batches = new GLFixedBatch[LAYERS];
    private final int[] vbo = new int[LAYERS];
    private boolean isDirty = true;
    private boolean built = false;

    public ClientChunk(World world,
                       int x0, int y0,
                       int x1, int y1) {
        this.world = world;
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        x = (x0 + x1) * 0.5;
        y = (y0 + y1) * 0.5;
        glGenBuffers(vbo);
    }

    public static long pos2Long(int x, int y) {
        long l = 0L;
        l |= Integer.toUnsignedLong(y);
        l <<= 32;
        l |= Integer.toUnsignedLong(x);
        return l;
    }

    public static int longX(long pos) {
        return (int) pos;
    }

    public static int longY(long pos) {
        return (int) (pos >>> 32);
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void markDirty() {
        isDirty = true;
    }

    private void rebuild(int layer) {
        GLFixedBatch batch;
        if ((batch = batches[layer]) == null) {
            batch = new GLFixedBatch(512 * 1024, 0);
            batches[layer] = batch;
        }
        batch.begin(VertexLayouts.T2F_C3UB_V3F);
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                var b = world.getBlockStates(x, y, layer);
                if (b.shouldRender(world, x, y, layer)) {
                    b.render(batch, x, y, layer);
                }
            }
        }
        batch.end();
        glBindBuffer(GL_ARRAY_BUFFER, vbo[layer]);
        glBufferData(GL_ARRAY_BUFFER, batch.getBuffer(), GL_DYNAMIC_DRAW);
    }

    public void rebuild() {
        isDirty = false;
        built = true;
        rebuild(0);
        rebuild(1);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render(int layer) {
        if (built) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo[layer]);
            glTexCoordPointer(2, GL_FLOAT, 3 + (5 << 2), 0L);
            glColorPointer(3, GL_UNSIGNED_BYTE, 3 + (5 << 2), 2 << 2);
            glVertexPointer(3, GL_FLOAT, 3 + (5 << 2), 3 + (2 << 2));
            glDrawArrays(GL_QUADS, 0, batches[layer].getVertexCount());
        }
    }

    public double distanceSqr(PlayerEntity player) {
        return player.position.distanceSquared(x, y, 0);
    }

    public void free() {
        glDeleteBuffers(vbo);
        for (int i = 0; i < LAYERS; i++) {
            if (batches[i] != null) {
                batches[i].close();
            }
        }
    }
}
