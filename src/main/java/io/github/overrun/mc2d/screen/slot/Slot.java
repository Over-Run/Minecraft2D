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

package io.github.overrun.mc2d.screen.slot;

import io.github.overrun.mc2d.screen.inv.IInventory;

/**
 * @author squid233
 * @since 2021/01/23
 */
public record Slot(IInventory inventory, int id, int x, int y) {
    public static final int HOT_BAR_ID0 = 0;
    public static final int HOT_BAR_ID1 = 1;
    public static final int HOT_BAR_ID2 = 2;
    public static final int HOT_BAR_ID3 = 3;
    public static final int HOT_BAR_ID4 = 4;
    public static final int HOT_BAR_ID5 = 5;
    public static final int HOT_BAR_ID6 = 6;
    public static final int HOT_BAR_ID7 = 7;
    public static final int HOT_BAR_ID8 = 8;
    public static final int HOT_BAR_ID9 = 9;
    public static final int OFFHAND_ID = 10;
    public static final int PLAYER_INV_ID0 = 11;
    public static final int CONTAINER_ID0 = 38;
    public static final int WEAR_ID0 = 294;
    public static final int ARMOR_ID0 = 298;
    public static final int FLYING_ID0 = 302;
}
