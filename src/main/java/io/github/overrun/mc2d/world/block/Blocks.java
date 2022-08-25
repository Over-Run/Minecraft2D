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

package io.github.overrun.mc2d.world.block;

import io.github.overrun.mc2d.util.registry.Registry;

/**
 * @author squid233
 * @since 2021/01/09
 */
public final class Blocks {
    public static final BlockType AIR = register("air", new AirBlockType());
    public static final BlockType GRASS_BLOCK = register("grass_block", new BlockType());
    public static final BlockType STONE = register("stone", new BlockType());
    public static final BlockType DIRT = register("dirt", new BlockType());
    public static final BlockType COBBLESTONE = register("cobblestone", new BlockType());
    public static final BlockType BEDROCK = register("bedrock", new BlockType());
    public static final BlockType OAK_LOG = register("oak_log", new BlockType());
    public static final BlockType OAK_LEAVES = register("oak_leaves", new LeavesBlockType());

    public static void register() {
    }

    public static BlockType register(String id, BlockType block) {
        return Registry.register(Registry.BLOCK, id, block);
    }
}

