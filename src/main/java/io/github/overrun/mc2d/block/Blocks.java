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

package io.github.overrun.mc2d.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;

/**
 * @author squid233
 * @since 2021/01/09
 */
public final class Blocks {
    public static final BiMap<String, Block> BLOCKS = HashBiMap.create(3);
    public static final Byte2ObjectMap<Block> RAW_ID_BLOCKS = new Byte2ObjectLinkedOpenHashMap<>(3);
    public static final int BLOCK_SIZE = 32;
    public static final Block AIR = register("air", 0);
    public static final Block GRASS_BLOCK = register("grass_block", 1);
    public static final Block DIRT = register("dirt", 2);

    public static Block register(String id, int rawId) {
        Block b = new Block(rawId);
        BLOCKS.putIfAbsent(id, b);
        RAW_ID_BLOCKS.putIfAbsent(b.getRawId(), b);
        return b;
    }
}

