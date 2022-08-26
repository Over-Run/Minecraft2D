/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Overrun Organization
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

package io.github.overrun.mc2d.screen;

import io.github.overrun.mc2d.screen.inv.IInventory;
import io.github.overrun.mc2d.screen.inv.PlayerInventory;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.world.entity.PlayerEntity;

/**
 * @author squid233
 * @since 2021/01/25
 */
public final class CreativeTabScreenHandler extends ScreenHandler {
    public final IInventory inventory;

    public CreativeTabScreenHandler(PlayerInventory playerInventory, IInventory inventory) {
        this.inventory = inventory;
        // content
        for (int i = 0; i < 45; i++) {
            addSlot(new Slot(inventory, Slot.CONTAINER_ID0 + i, 9 + i % 9 * 18, 18 + i / 9 * 18));
        }
        // hot-bar
        for (int i = 0; i < 10; i++) {
            addSlot(new Slot(playerInventory, Slot.HOT_BAR_ID0 + i, 9 + i * 18, 112));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }
}
