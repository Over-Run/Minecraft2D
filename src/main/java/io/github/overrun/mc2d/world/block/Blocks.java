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
    public static final Block AIR = register("air", new AirBlock());
    public static final Block GRASS_BLOCK = register("grass_block", new Block());
    public static final Block STONE = register("stone", new Block());
    public static final Block DIRT = register("dirt", new Block());
    public static final Block COBBLESTONE = register("cobblestone", new Block());
    public static final Block BEDROCK = register("bedrock", new Block());

    public static void register() {
    }

    public static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, id, block);
    }
}

