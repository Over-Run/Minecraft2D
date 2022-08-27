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

package io.github.overrun.mc2d.screen.inv;

import io.github.overrun.mc2d.world.entity.PlayerEntity;
import io.github.overrun.mc2d.world.item.ItemConvertible;
import io.github.overrun.mc2d.world.item.ItemStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author squid233
 * @since 0.6.0
 */
public class PlayerInventory implements IInventory {
    private final Int2ObjectMap<ItemStack> stacks = new Int2ObjectOpenHashMap<>(size());
    public final PlayerEntity player;

    public PlayerInventory(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public Int2ObjectMap<ItemStack> getItems() {
        return stacks;
    }

    @Override
    public int size() {
        return 10 + 1 + 27 + 4 + 4 + 1;
    }

    @Override
    public boolean isEmpty() {
        for (var stack : stacks.values()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int indexOf(ItemConvertible item) {
        for (var e : stacks.int2ObjectEntrySet()) {
            if (e.getValue().getItem() == item.asItem()) {
                return e.getIntKey();
            }
        }
        return -1;
    }

    @Override
    public ItemStack getStack(int slot) {
        return stacks.getOrDefault(slot, ItemStack.ofEmpty());
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        var result = Inventories.splitStack(stacks, slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(stacks, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        stacks.put(slot, stack);
        if (stack.getCount() > stack.getMaxCount()) {
            stack.setCount(stack.getMaxCount());
        }
        markDirty();
    }

    @Override
    public void clear() {
        stacks.clear();
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
