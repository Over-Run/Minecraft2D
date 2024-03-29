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

import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.world.entity.player.PlayerEntity;
import io.github.overrun.mc2d.world.item.ItemConvertible;
import io.github.overrun.mc2d.world.item.ItemGroup;
import io.github.overrun.mc2d.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author squid233
 * @since 0.6.0
 */
public class ItemGroupsInventory implements IInventory {
    private final Map<Integer, ItemStack> stacks = new HashMap<>();

    @Override
    public Map<Integer, ItemStack> getItems() {
        stacks.clear();
        for (int i = 0; i < size(); i++) {
            stacks.put(Slot.CONTAINER_ID0 + i, ItemGroup.BUILDING_BLOCKS.getStacks().get(i));
        }
        return stacks;
    }

    @Override
    public int size() {
        return 45;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int indexOf(ItemConvertible item) {
        for (int i = 0; i < size(); i++) {
            if (ItemGroup.BUILDING_BLOCKS.getStacks().get(i).getItem() == item.asItem()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ItemStack getStack(int slot) {
        getItems();
        return stacks.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        return removeStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return getStack(slot).copy();
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
    }

    @Override
    public void clear() {
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
