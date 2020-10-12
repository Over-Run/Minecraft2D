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

import java.util.Objects;

/**
 * @author squid233
 * @since 2020/10/02
 */
public final class ItemStack {
    public static final ItemStack EMPTY = new ItemStack(null);

    private final Item item;
    private int count;

    public ItemStack(ItemConvertible item, int count) {
        this.item = item == null ? null : item.asItem();
        this.count = count;
    }

    public ItemStack(ItemConvertible item) {
        this(item, 1);
    }

    public void grow() {
        setCount(count + 1);
    }

    public void shrink() {
        setCount(count - 1);
    }

    public boolean isEmpty() {
        if (this == EMPTY) {
            return true;
        } else if (getItem() != null && getItem() != Items.AIR) {
            return count <= 0;
        } else {
            return true;
        }
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Item getItem() {
        return item != null ? item : Items.AIR;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemStack itemStack = (ItemStack) o;
        return getCount() == itemStack.getCount() &&
                Objects.equals(getItem(), itemStack.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem(), getCount());
    }

    @Override
    public String toString() {
        return getItem() + " * " + getCount();
    }
}
