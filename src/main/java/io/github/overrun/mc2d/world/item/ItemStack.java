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

import org.joml.Math;

import java.util.Objects;

/**
 * @author squid233
 * @since 0.6.0
 */
public final class ItemStack {
    private ItemType item;
    private int count;
    private int maxCount = 999;

    private ItemStack(ItemConvertible item, int count) {
        this.item = item.asItem();
        this.count = count;
    }

    public static ItemStack of(ItemConvertible item, int count) {
        return new ItemStack(item, count);
    }

    public static ItemStack of(ItemConvertible item) {
        return of(item, 1);
    }

    public static ItemStack ofEmpty() {
        return of(Items.AIR, 0);
    }

    public ItemStack copy(int count) {
        var stack = of(getItem(), count);
        stack.setMaxCount(getMaxCount());
        stack.setCount(stack.getCount());
        return stack;
    }

    public ItemStack copy() {
        return copy(getCount());
    }

    public void set(ItemStack stack) {
        setItem(stack.getItem());
        setMaxCount(stack.getMaxCount());
        setCount(stack.getCount());
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = Math.clamp(0, 999, maxCount);
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setItem(ItemConvertible item) {
        this.item = item != null ? item.asItem() : Items.AIR;
    }

    public void setCount(int count) {
        this.count = Math.clamp(0, getMaxCount(), count);
    }

    public void increment(int count) {
        setCount(getCount() + count);
    }

    public void increment() {
        increment(1);
    }

    public void decrement(int count) {
        setCount(getCount() - count);
    }

    public void decrement() {
        decrement(1);
    }

    public ItemType getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public boolean isEmpty() {
        return count < 1 || item == Items.AIR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemStack stack = (ItemStack) o;
        return getCount() == stack.getCount() && getMaxCount() == stack.getMaxCount() && Objects.equals(getItem(), stack.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem(), getCount(), getMaxCount());
    }
}
