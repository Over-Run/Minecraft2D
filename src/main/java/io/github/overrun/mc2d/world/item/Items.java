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

package io.github.overrun.mc2d.world.item;

import io.github.overrun.mc2d.world.block.BlockType;
import io.github.overrun.mc2d.world.block.Blocks;
import io.github.overrun.mc2d.util.registry.Registry;

/**
 * @author squid233
 * @since 2021/01/27
 */
public final class Items {
    public static final ItemType AIR = register("air", Blocks.AIR);
    public static final ItemType GRASS_BLOCK = register("grass_block", Blocks.GRASS_BLOCK);
    public static final ItemType STONE = register("stone", Blocks.STONE);
    public static final ItemType DIRT = register("dirt", Blocks.DIRT);
    public static final ItemType COBBLESTONE = register("cobblestone", Blocks.COBBLESTONE);
    public static final ItemType BEDROCK = register("bedrock", Blocks.BEDROCK);
    public static final ItemType OAK_LOG = register("oak_log", Blocks.OAK_LOG);
    public static final ItemType OAK_LEAVES = register("oak_leaves", Blocks.OAK_LEAVES);

    public static void register() {
    }

    public static ItemType register(String id, ItemType item) {
        return Registry.register(Registry.ITEM, id, item);
    }

    public static ItemType register(String id, BlockType block) {
        return register(id, new BlockItemType(block));
    }
}