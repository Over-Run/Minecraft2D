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

package io.github.overrun.mc2d.registry;

import io.github.overrun.mc2d.block.AbstractBlock;
import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.util.Identifier;

/**
 * @author squid233
 * @date 2020/9/15
 */
public class Registry {
    public static final DeferredRegistry<AbstractBlock> BLOCK = new DeferredRegistry<>(Mc2dRegistries.BLOCKS);

    public static <T extends IRegistrable> T register(DeferredRegistry<T> registry, Identifier id, T entry) {
        return registry.register(id, () -> {
            if (entry instanceof AbstractBlock) {
                Blocks.add((AbstractBlock) entry);
            }
            return entry;
        });
    }

    public static <T extends IRegistrable> T register(DeferredRegistry<T> registry, String name, T entry) {
        return register(registry, new Identifier(name), entry);
    }
}
