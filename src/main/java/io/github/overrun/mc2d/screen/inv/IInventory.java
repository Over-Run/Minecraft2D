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

import io.github.overrun.mc2d.world.entity.player.PlayerEntity;
import io.github.overrun.mc2d.world.item.ItemConvertible;
import io.github.overrun.mc2d.world.item.ItemStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

/**
 * The inventory.
 *
 * @author squid233
 * @since 0.6.0
 */
public interface IInventory {
    Int2ObjectMap<ItemStack> getItems();

    int size();

    boolean isEmpty();

    /**
     * Gets the first slot index of the given item, or -1 if not found.
     *
     * @param item the item
     * @return the slot index or -1
     */
    int indexOf(ItemConvertible item);

    /**
     * Gets the stack at the given slot id.
     *
     * @param slot the slot id
     * @return the stack
     */
    ItemStack getStack(int slot);

    /**
     * Removes the stack at the given slot id with the specified count.
     *
     * @param slot  the slot id
     * @param count The remove count. If {@code count} is less than the original stack, remove all.
     * @return the copy of removed stack
     */
    ItemStack removeStack(int slot, int count);

    /**
     * Removes all items in the stack at the given slot id.
     *
     * @param slot the slot id
     * @return the copy of original stack
     */
    ItemStack removeStack(int slot);

    /**
     * Replaces the current stack in an inventory slot with the provided stack.
     *
     * @param slot  The inventory slot of which to replace the item stack.
     * @param stack The replacing item stack. If the stack is too big for
     *              this inventory,
     *              it gets resized to this inventory's maximum amount.
     */
    void setStack(int slot, ItemStack stack);

    void clear();

    void markDirty();

    boolean canPlayerUse(PlayerEntity player);
}
