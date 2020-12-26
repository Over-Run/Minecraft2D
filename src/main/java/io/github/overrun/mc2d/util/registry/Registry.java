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

package io.github.overrun.mc2d.util.registry;

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.util.Identifier;

/**
 * Register an object to entry table.<br>
 * The registry api has 2 methods to register the entries:<br>
 * <ol><li>{@code Registry.register(Registry.BLOCK, "mc2d:air", Blocks.AIR);}</li>
 * <li>{@code Registry.BLOCK.register("mc2d:air", Blocks.AIR);}</li></ol>
 *
 * @author squid233
 * @since 2020/10/06
 */
public final class Registry {
    public static final DefaultedRegistry<Block> BLOCK = new DefaultedRegistry<>(Blocks.AIR);

    public static <T> T register(BaseRegistry<T> registry, Identifier id, T entry) {
        return registry.register(id, entry);
    }

    public static <T> T register(BaseRegistry<T> registry, String id, T entry) {
        return registry.register(id, entry);
    }
}
