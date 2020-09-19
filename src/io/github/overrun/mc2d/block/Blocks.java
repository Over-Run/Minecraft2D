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
import io.github.overrun.mc2d.registry.Mc2dRegistries;
import io.github.overrun.mc2d.registry.Registry;
import io.github.overrun.mc2d.util.Identifier;

import java.util.ArrayList;

/**
 * @author squid233
 * @date 2020/9/15
 */
public class Blocks {
    private static final ArrayList<AbstractBlock> BLOCKS = new ArrayList<>(5);

    private static Block register(String name, Block block) {
        Minecraft2D.LOGGER.debug("Registered block: " + Registry.register(Registry.BLOCK, name, block));
        return block;
    }

    public static void add(AbstractBlock block) {
        if (!BLOCKS.contains(block)) {
            BLOCKS.add(block);
        }
    }

    public static final BlockAir AIR;
    public static final Block DIRT;
    public static final Block GRASS_BLOCK;
    public static final Block COBBLESTONE;
    public static final Block BEDROCK;
    public static final Block STONE;

    public static AbstractBlock getById(Identifier id) {
        return Mc2dRegistries.BLOCKS.get(id.toString());
    }

    public static AbstractBlock getByRawId(int rawId) {
        try {
            return BLOCKS.get(rawId);
        } catch (Exception e) {
            return AIR;
        }
    }

    static {
        AIR = (BlockAir) register("air", new BlockAir(new Block.Settings()));
        DIRT = register("dirt", new Block(new Block.Settings()));
        GRASS_BLOCK = register("grass_block", new Block(new Block.Settings()));
        COBBLESTONE = register("cobblestone", new Block(new Block.Settings()));
        BEDROCK = register("bedrock", new Block(new Block.Settings()));
        STONE = register("stone", new Block(new Block.Settings()));
    }
}
