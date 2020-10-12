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

package io.github.overrun.mc2d.block;

import io.github.overrun.mc2d.Minecraft2D;
import io.github.overrun.mc2d.block.AbstractBlock.Settings;
import io.github.overrun.mc2d.util.registry.Registry;


/**
 * @author squid233
 * @since 2020/09/15
 */
public class Blocks {
    public static final Settings EMPTY_SETTINGS = new Settings();
    public static final Block AIR;
    public static final Block STONE;
    public static final Block GRASS_BLOCK;
    public static final Block DIRT;
    public static final Block COBBLESTONE;
    public static final Block OAK_PLANKS;
    public static final Block OAK_SAPLING;
    public static final Block BEDROCK;
    public static final Block SAND;

    static {
        AIR = register("air", new BlockAir(EMPTY_SETTINGS));
        STONE = register("stone", new Block(EMPTY_SETTINGS));
        GRASS_BLOCK = register("grass_block", new Block(EMPTY_SETTINGS));
        DIRT = register("dirt", new Block(EMPTY_SETTINGS));
        COBBLESTONE = register("cobblestone", new Block(EMPTY_SETTINGS));
        BEDROCK = register("bedrock", new Block(EMPTY_SETTINGS));
        SAND = register("sand", new Block(EMPTY_SETTINGS));
        OAK_PLANKS = register("oak_planks", new Block(EMPTY_SETTINGS));
        OAK_SAPLING = register("oak_sapling", new Block(EMPTY_SETTINGS));
    }

    private static Block register(String name, Block block) {
        Minecraft2D.LOGGER.debug("Registered block: " + Registry.register(Registry.BLOCK, name, block));
        return block;
    }
}
