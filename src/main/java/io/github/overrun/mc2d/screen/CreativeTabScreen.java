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

import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.game.Player;
import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.item.Item;
import io.github.overrun.mc2d.item.ItemGroup;
import io.github.overrun.mc2d.item.Items;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.util.Highlight;
import io.github.overrun.mc2d.util.IntUtil;
import io.github.overrun.mc2d.util.ResourceLocation;
import io.github.overrun.mc2d.util.registry.Registry;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;

/**
 * @author squid233
 * @since 2020/10/01
 */
public class CreativeTabScreen extends ScreenHandler {
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/creative_tab/tab_items.png");
    private static final Image IMG = Images.getImagePart(new ImageIcon(TEXTURE.toString()).getImage(), 0, 0, 195, 136);
    public static final int HOTBAR = 9;
    public static final Image TABS = new ImageIcon(new ResourceLocation("textures/gui/creative_tab/tabs.png").toString()).getImage();
    public static final Image SCROLL_VALID = Images.getImagePart(TABS, 232, 0, 12, 15);
    public static final Image SCROLL_INVALID = Images.getImagePart(TABS, 244, 0, 12, 15);
    private static int x = (Mc2dClient.getInstance().getWidth() >> 1) - 195,
            y = (Mc2dClient.getInstance().getHeight() >> 1) - 136;

    static {
        ItemGroup.BUILDING_BLOCKS.appendStacks();
    }

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
        x = (Mc2dClient.getInstance().getWidth() >> 1) - 195;
        y = (Mc2dClient.getInstance().getHeight() >> 1) - 136;
        Screen.drawImage(g, IMG, x, y - 30, 390, 272);
        Screen.drawText(g, x + 16, y - 16, LiteralText.of("Building Blocks"), 3);
        Screen.drawImage(g, shouldShowScrollBar() ? SCROLL_VALID : SCROLL_INVALID, x + 350, y + 6, 24, 30);
        for (int i = 0; i < ItemGroup.BUILDING_BLOCKS.size(); i++) {
            slots.get(i).setStack(ItemGroup.BUILDING_BLOCKS.get(i));
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                Item it = slots.get(IntUtil.clamp(j, 0, 9) * IntUtil.clamp(i, 1, 9)).getStack().getItem();
                if (!it.equals(Items.AIR)) {
                    Screen.drawImage(g, Images.getItemTexture(it), x + 18 + j * 36, y + 6 + i * 36, 32, 32);
                }
            }
        }
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        if (e.getX() > x
                && e.getX() < x + 194
                && e.getY() > y
                && e.getY() < y + 135) {
            for (int i = 0; i < slots.size() - HOTBAR; i++) {
                if (!Registry.BLOCK.get(i + 1).equals(Blocks.AIR)
                        && e.getX() > slots.get(i).getX() + x
                        && e.getX() < slots.get(i).getX() + x + 31
                        && e.getY() > slots.get(i).getY() + y
                        && e.getY() < slots.get(i).getY() + y + 31
                ) {
                    Player.handledBlock = i + 1;
                    break;
                }
            }
        }
    }

    @Override
    public void onMouseMoved(Graphics g) {
        for (Slot slot : slots) {
            Highlight.slot(g, x + slot.getX(), y + slot.getY(), 32, 32);
        }
    }

    public boolean shouldShowScrollBar() {
        // 5 * 9 == 45
        return ItemGroup.BUILDING_BLOCKS.size() > 45;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }
}
