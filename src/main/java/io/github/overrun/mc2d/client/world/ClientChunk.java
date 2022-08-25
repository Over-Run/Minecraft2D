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

import io.github.overrun.mc2d.client.render.Tesselator;
import io.github.overrun.mc2d.world.Chunk;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.world.entity.PlayerEntity;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 0.6.0
 */
public class ClientChunk extends Chunk {
    private final World world;
    private final double x, y;
    private final int x0, y0, x1, y1;
    private final int lists;
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
        lists = glGenLists(2);
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
        glNewList(lists + layer, GL_COMPILE);
        final var t = Tesselator.getInstance();
        t.begin();
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                var b = world.getBlock(x, y, layer);
                if (b.shouldRender(world, x, y, layer)) {
                    b.render(t, x, y, layer);
                }
            }
        }
        t.flush(GL_QUADS);
        glEndList();
    }

    public void rebuild() {
        isDirty = false;
        built = true;
        rebuild(0);
        rebuild(1);
    }

    public void render(int layer) {
        if (built) {
            glCallList(lists + layer);
        }
    }

    public double distanceSqr(PlayerEntity player) {
        return player.position.distanceSquared(x, y, 0);
    }

    public void free() {
        glDeleteLists(lists, 2);
    }
}