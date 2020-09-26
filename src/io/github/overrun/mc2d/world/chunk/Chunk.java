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

package io.github.overrun.mc2d.world.chunk;

import io.github.overrun.mc2d.block.AbstractBlock;
import io.github.overrun.mc2d.block.BlockPos;
import io.github.overrun.mc2d.block.Blocks;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class Chunk implements Serializable {
    public static final transient int WORLD_HEIGHT = 64;
    public static final transient int CHUNK_SIZE = 16;
    private static final long serialVersionUID = -6474694478994007562L;
    public final HashMap<BlockPos, AbstractBlock> blocks = new HashMap<>();

    public Chunk() {
        for (int i = 0; i < WORLD_HEIGHT; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                setBlock(j, i, Blocks.AIR);
            }
        }
    }

    public void draw(Graphics g, int x) {
        for (BlockPos pos : blocks.keySet()) {
            blocks.get(pos).setPos(pos).draw(g, blocks.get(pos).x + (x << 4));
        }
    }

    public void setBlock(BlockPos pos, AbstractBlock block) {
        blocks.put(pos, Blocks.getById(block.getRegistryName()).setPos(pos));
    }

    public void setBlock(int x, int y, AbstractBlock block) {
        setBlock(BlockPos.of(x, y), block);
    }

    public AbstractBlock getBlock(BlockPos pos) {
        try {
            return blocks.get(pos);
        } catch (Exception e) {
            blocks.put(pos, Blocks.AIR);
            return Blocks.AIR;
        }
    }

    public AbstractBlock getBlock(int x, int y) {
        return getBlock(BlockPos.of(x, y));
    }

    @Override
    public String toString() {
        return blocks.toString();
    }
}
