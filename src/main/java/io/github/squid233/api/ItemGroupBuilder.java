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

package io.github.squid233.api;

import io.github.overrun.mc2d.item.ItemGroup;
import io.github.overrun.mc2d.item.ItemStack;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.collection.DefaultedList;

import java.util.function.Consumer;

/**
 * @author squid233
 * @since 2020/10/03
 */
public class ItemGroupBuilder {
    private final Identifier id;
    private ItemStack icon = ItemStack.EMPTY;
    private Consumer<DefaultedList<ItemStack>> stacks;

    private ItemGroupBuilder(Identifier id) {
        this.id = id;
    }

    public static ItemGroupBuilder create(Identifier id) {
        return new ItemGroupBuilder(id);
    }

    public ItemGroupBuilder icon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public ItemGroupBuilder appendItems(Consumer<DefaultedList<ItemStack>> stacks) {
        if (this.stacks == null) {
            this.stacks = stacks;
        }
        return this;
    }

    public static ItemGroup build(Identifier id, ItemStack icon) {
        return new ItemGroupBuilder(id).icon(icon).build();
    }

    public ItemGroup build() {
        ItemGroup.expandGroups();
        return new ItemGroup(ItemGroup.getGroups().length - 1, id) {
            @Override
            public ItemStack createIcon() {
                return icon;
            }

            @Override
            public void appendStacks(DefaultedList<ItemStack> stacks) {
                if (ItemGroupBuilder.this.stacks != null) {
                    ItemGroupBuilder.this.stacks.accept(stacks);
                } else {
                    super.appendStacks(stacks);
                }
            }
        };
    }
}
