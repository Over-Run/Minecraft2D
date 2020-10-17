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

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.BlockPos;
import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.util.registry.Registry;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.awt.Graphics;
import java.io.Serializable;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class Chunk implements Serializable {
    public static final transient int WORLD_HEIGHT = 64;
    public static final transient int CHUNK_SIZE = 16;
    private static final long serialVersionUID = -1705473506565599035L;
    private Object2ObjectMap<BlockPos, Block> blocks = new Object2ObjectOpenHashMap<>();

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

    public void setBlock(BlockPos pos, Block block) {
        if (block == null) {
            block = Blocks.AIR;
        }
        blocks.put(pos, Registry.BLOCK.get(block.getRegistryName()).setPos(pos));
    }

    public void setBlock(int x, int y, Block block) {
        setBlock(BlockPos.of(x, y), block);
    }

    public Block getBlock(BlockPos pos) {
        try {
            return blocks.get(pos);
        } catch (Exception e) {
            blocks.put(pos, Blocks.AIR);
            return Blocks.AIR;
        }
    }

    public Chunk setBlocks(Object2ObjectMap<BlockPos, Block> blocks) {
        this.blocks = blocks;
        return this;
    }

    public Block getBlock(int x, int y) {
        return getBlock(BlockPos.of(x, y));
    }

    public Object2ObjectMap<BlockPos, Block> getBlocks() {
        return blocks;
    }

    @Override
    public String toString() {
        return blocks.toString();
    }
}
