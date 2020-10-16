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

package io.github.overrun.mc2d.screen;

import io.github.overrun.mc2d.Minecraft2D;
import io.github.overrun.mc2d.image.ImageIcons;
import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.item.Item;
import io.github.overrun.mc2d.item.ItemGroup;
import io.github.overrun.mc2d.item.ItemStack;
import io.github.overrun.mc2d.item.Items;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.util.Highlight;
import io.github.overrun.mc2d.util.ResourceLocation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;

/**
 * @author squid233
 * @since 2020/10/01
 */
public class CreativeTabScreen extends ScreenHandler {
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/creative_tab/tab_items.png");
    private static final Image IMG = ImageIcons.getImage(TEXTURE);
    public static final int HOTBAR = 9;
    public static final Image SCROLL_VALID = ImageIcons.getGameImage("textures/gui/creative_tab/scroll_valid.png");
    public static final Image SCROLL_INVALID = ImageIcons.getGameImage("textures/gui/creative_tab/scroll_invalid.png");
    public static final Color NAME_COLOR = new Color(65, 65, 65);
    private int x = (Minecraft2D.getWidth() >> 1) - 195,
            y = (Minecraft2D.getHeight() >> 1) - 136;
    public ItemStack handledStack = ItemStack.EMPTY;

    public CreativeTabScreen() {
        // Creative Tabs
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(26 + j * 36, 36 + i * 36));
            }
        }

        // Hotbar
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(26 + i * 36, 224));
        }
        slots.defaultReturnValue(new Slot(0, 0));
    }

    @Override
    public void render(Graphics g) {
        drawDefaultBackground(g);
        x = (Minecraft2D.getWidth() >> 1) - 195;
        y = (Minecraft2D.getHeight() >> 1) - 136;
        ScreenUtil.drawImage(g, IMG, x, y - 30, 390, 272);
        ScreenUtil.drawText(g, x + 16, y - 5, LiteralText.of("Building Blocks"), NAME_COLOR);
                ScreenUtil.drawImage(g, shouldShowScrollBar() ? SCROLL_VALID : SCROLL_INVALID, x + 350, y + 6, 24, 30);
        for (int i = 0, size = ItemGroup.BUILDING_BLOCKS.size(); i < size; i++) {
            slots.get(i).setStack(ItemGroup.BUILDING_BLOCKS.get(i));
        }
        for (int i = 0, size = ItemGroup.BUILDING_BLOCKS.size(); i < size; i++) {
            Item item = slots.get(i).getStack().getItem();
            if (!item.equals(Items.AIR)) {
                ScreenUtil.drawImage(g, Images.getItemTexture(item), x + 18 + (i % 9) * 36, y + 6 + (i / 9) * 36, 32, 32);
            }
        }
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        if (e.getX() > x && e.getX() < x + 388) {
            if (e.getY() > y && e.getY() < y + 270) {
                for (int i = 0, size = slots.size() - HOTBAR; i < size; i++) {
                    if (
                            e.getX() > slots.get(i).getX() + x
                            && e.getX() < slots.get(i).getX() + x + 32
                            && e.getY() > slots.get(i).getY() + y
                            && e.getY() < slots.get(i).getY() + y + 32
                    ) {
                        if (handledStack.getItem() != Items.AIR) {
                            if (handledStack.getItem() == slots.get(i).getStack().getItem()) {
                                handledStack.grow();
                            } else {
                                handledStack = ItemStack.EMPTY;
                            }
                        } else {
                            handledStack = slots.get(i).getStack();
                        }
                        break;
                    }
                }
            } else if (
                    e.getY() > y + 270
                            && e.getY() < y + 272
            ) {
            }
        }
    }

    @Override
    public void onMouseMoved(Graphics g) {
        for (Slot slot : slots) {
            Highlight.slot(g, x + slot.getX(), y + slot.getY(), 32, 32);
        }
        if (handledStack != ItemStack.EMPTY) {
            ScreenUtil.drawImage(g, Images.getItemTexture(handledStack.getItem()),
                    Minecraft2D.getMouseX() - 24,
                    Minecraft2D.getMouseY() - 46,
                    32, 32);
        }
    }

    public boolean shouldShowScrollBar() {
        // 5 * 9 == 45
        return ItemGroup.BUILDING_BLOCKS.size() > 45;
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }
}
