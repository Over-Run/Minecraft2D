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

import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.world.block.BlockType;
import io.github.overrun.mc2d.world.block.Blocks;
import io.github.overrun.mc2d.util.registry.Registry;

/**
 * @author squid233
 * @since 2021/01/27
 */
public final class Items {
    public static final ItemType AIR = register(0, "air", ItemSettings.of(), Blocks.AIR);
    public static final ItemType STONE = register(1, "stone", ItemSettings.of().group(ItemGroup.BUILDING_BLOCKS), Blocks.STONE);
    public static final ItemType GRASS_BLOCK = register(2, "grass_block", ItemSettings.of().group(ItemGroup.BUILDING_BLOCKS), Blocks.GRASS_BLOCK);
    public static final ItemType DIRT = register(3, "dirt", ItemSettings.of().group(ItemGroup.BUILDING_BLOCKS), Blocks.DIRT);
    public static final ItemType COBBLESTONE = register(4, "cobblestone", ItemSettings.of().group(ItemGroup.BUILDING_BLOCKS), Blocks.COBBLESTONE);
    public static final ItemType BEDROCK = register(5, "bedrock", ItemSettings.of().group(ItemGroup.BUILDING_BLOCKS), Blocks.BEDROCK);
    public static final ItemType OAK_LOG = register(6, "oak_log", ItemSettings.of().group(ItemGroup.BUILDING_BLOCKS), Blocks.OAK_LOG);
    public static final ItemType OAK_LEAVES = register(12, "oak_leaves", ItemSettings.of().group(ItemGroup.BUILDING_BLOCKS), Blocks.OAK_LEAVES);
    public static final ItemType OAK_PLANKS = register(18, "oak_planks", ItemSettings.of().group(ItemGroup.BUILDING_BLOCKS), Blocks.OAK_PLANKS);

    public static void register() {
    }

    private static ItemType register(int rawId, String id, ItemType item) {
        return Registry.ITEM.set(rawId, new Identifier(id), item);
    }

    private static ItemType register(int rawId, String id, ItemSettings settings, BlockType block) {
        return register(rawId, id, new BlockItemType(settings, block));
    }
}
