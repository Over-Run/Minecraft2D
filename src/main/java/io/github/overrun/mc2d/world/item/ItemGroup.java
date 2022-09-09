/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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
import io.github.overrun.mc2d.util.collect.DefaultedList;
import io.github.overrun.mc2d.util.registry.MappedRegistry;
import io.github.overrun.mc2d.util.registry.Registry;

/**
 * The item group.
 *
 * @author squid233
 * @since 0.6.0
 */
public class ItemGroup {
    public static final MappedRegistry<ItemGroup> ITEM_GROUP = Registry.of();
    public static final ItemGroup BUILDING_BLOCKS = register(new Identifier("building_blocks"), new ItemGroup());

    private final DefaultedList<ItemStack> stacks = new DefaultedList<>(ItemStack::ofEmpty);

    public static ItemGroup register(Identifier id, ItemGroup group) {
        return Registry.register(ITEM_GROUP, id, group);
    }

    public void addStack(ItemStack stack) {
        stacks.add(stack);
    }

    public void addStacks() {
        for (var e : Registry.ITEM) {
            var item = e.getValue();
            if (item.getGroup() == this) {
                addStack(ItemStack.of(e.getValue()));
            }
        }
    }

    public DefaultedList<ItemStack> getStacks() {
        return stacks;
    }
}
