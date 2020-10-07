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

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.collection.DefaultedList;
import io.github.overrun.mc2d.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * @author squid233
 * @since 2020/10/01
 */
public class Item implements ItemConvertible {
    public static final transient HashMap<Block, Item> BLOCK_ITEMS = new HashMap<>(6);
    protected final ItemGroup group;

    public Item(Settings settings) {
        group = settings.group;
    }

    public static Item getItemByBlock(Block block) {
        return BLOCK_ITEMS.get(block);
    }

    @Nullable
    public ItemGroup getGroup() {
        return group;
    }

    protected boolean isIn(ItemGroup group) {
        return getGroup() != null && (group == getGroup());
    }

    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        System.out.println(isIn(group));
        if (isIn(group)) {
            stacks.add(new ItemStack(this));
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public Item asItem() {
        return this;
    }

    public Identifier getRegistryName() {
        return Registry.ITEM.getId(this);
    }

    @Override
    public String toString() {
        return getRegistryName().toString();
    }

    public static class Settings {
        private ItemGroup group;

        public Settings group(ItemGroup group) {
            this.group = group;
            return this;
        }
    }
}
