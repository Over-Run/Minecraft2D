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

import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.util.Identifier;
import io.github.squid233.api.ItemGroupBuilder;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author squid233
 * @since 2020/10/03
 */
public abstract class ItemGroup {
    public static final ItemGroup BUILDING_BLOCKS = ItemGroupBuilder.build(new Identifier("building_block"), new ItemStack(Blocks.STONE));

    private final Identifier id;
    private final ArrayList<ItemStack> stacks = new ArrayList<>();

    public ItemGroup(Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    /**
     * The icon will be display.
     *
     * @return An item stack.
     */
    public abstract ItemStack createIcon();

    public ArrayList<ItemStack> getStacks() {
        return stacks;
    }

    public int getRows() {
        return stacks.size() % 9 == 0 ? stacks.size() : stacks.size() / 9 + 1;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ItemGroup && getId().equals(((ItemGroup) obj).getId());
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public void appendStacks() {
        Iterator<Item> it = Registry.ITEM.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            item.appendStacks(this, stacks);
        }
    }
}
