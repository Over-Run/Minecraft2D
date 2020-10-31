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

package io.github.overrun.mc2d.item;

import io.github.overrun.mc2d.Minecraft2D;
import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.item.Item.Settings;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.registry.Registry;

/**
 * @author squid233
 * @since 2020/10/03
 */
public class Items {
    public static Item AIR;
    public static Item STONE;
    public static Item GRASS_BLOCK;
    public static Item DIRT;
    public static Item COBBLESTONE;
    public static Item OAK_PLANKS;
    public static Item OAK_SAPLING;
    public static Item BEDROCK;
    public static Item SAND;

    private static boolean init;

    public static void init() {
        if (!init) {
            AIR = register(Blocks.AIR, new Item(new Settings()));
            STONE = register(Blocks.STONE, ItemGroup.BUILDING_BLOCKS);
            GRASS_BLOCK = register(Blocks.GRASS_BLOCK, ItemGroup.BUILDING_BLOCKS);
            DIRT = register(Blocks.DIRT, ItemGroup.BUILDING_BLOCKS);
            COBBLESTONE = register(Blocks.COBBLESTONE, ItemGroup.BUILDING_BLOCKS);
            OAK_PLANKS = register(Blocks.OAK_PLANKS, ItemGroup.BUILDING_BLOCKS);
            OAK_SAPLING = register(Blocks.OAK_SAPLING, ItemGroup.BUILDING_BLOCKS);
            BEDROCK = register(Blocks.BEDROCK, ItemGroup.BUILDING_BLOCKS);
            SAND = register(Blocks.SAND, ItemGroup.BUILDING_BLOCKS);
        }
    }

    private static Item register(Identifier id, Item item) {
        if (item instanceof ItemBlock) {
            ((ItemBlock) item).appendBlocks(Item.BLOCK_ITEMS, item);
        }
        Minecraft2D.LOGGER.debug("Registered item: {}", Registry.register(Registry.ITEM, id, item));
        return item;
    }

    private static Item register(Block block, Item item) {
        return register(Registry.BLOCK.getId(block), item);
    }

    private static Item register(ItemBlock item) {
        return register(item.getBlock(), item);
    }

    private static Item register(Block block, ItemGroup group) {
        return register(new ItemBlock(block, new Settings().group(group)));
    }
}
