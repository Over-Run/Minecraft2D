/*
 * MIT License
 *
 * Copyright (c) 2020 Over-Run
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

package io.github.overrun.mc2d.world;

import io.github.overrun.mc2d.game.Camera;
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.world.chunk.Chunk;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class StorageBlock implements Serializable {
    public static final transient int INITIAL_CAPACITY = 16;
    private static final long serialVersionUID = -259645709641585372L;
    public final ArrayList<Chunk> chunks = new ArrayList<>(INITIAL_CAPACITY);

    public StorageBlock() {
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            chunks.add(i, new Chunk());
        }
    }

    public void draw(Graphics g) {
        for (int i = Camera.view; i < Camera.view + Options.getI(Options.VIEW_DISTANCE, 1); i++) {
            getChunk(i).draw(g, i - Camera.view);
        }
    }

    public Chunk getChunk(int x) {
        try {
            return chunks.get(x);
        } catch (Exception e) {
            for (int i = chunks.size(); i < x + 1; i++) {
                chunks.add(new Chunk());
            }
           return chunks.get(x);
        }
    }

    @Override
    public String toString() {
        return chunks.toString();
    }
}
