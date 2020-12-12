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

import io.github.overrun.mc2d.text.IText;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import java.awt.Graphics;
import java.util.Map;

import static io.github.overrun.mc2d.Minecraft2D.getHeight;
import static io.github.overrun.mc2d.Minecraft2D.getWidth;
import static io.github.overrun.mc2d.screen.Screens.BG_COLOR;
import static io.github.overrun.mc2d.util.Coordinator.U_L;
import static io.github.overrun.mc2d.util.DrawHelper.fillRect;

/**
 * @author squid233
 * @since 2020/10/16
 */
public class ComboBoxWidget extends ScreenWidget {
    private final Map<IText, ComboBoxItem> items;
    private ComboBoxItem selectedItem;

    public ComboBoxWidget(Screen screen, IText... itemContents) {
        this.items = new Object2ObjectArrayMap<>(itemContents.length);
        for (int i = 0; i < itemContents.length; i++) {
            IText t = itemContents[i];
            items.put(t, (ComboBoxItem) screen.addButton(
                    new ComboBoxItem(getY() + i * 25, t, w -> setSelectedItem((ComboBoxItem) w)))
            );
        }
    }

    @Override
    public void render(Graphics g) {
        fillRect(g, BG_COLOR, getX(), getY(), getWidth(), getY() << 2, U_L);
        for (ComboBoxItem i : items.values()) {
            i.render(g);
        }
    }

    public ComboBoxWidget setSelectedItem(ComboBoxItem item) {
        selectedItem = item;
        return this;
    }

    public ComboBoxWidget setSelectedItem(IText itemContent) {
        return setSelectedItem(getItems().get(itemContent));
    }

    public Map<IText, ComboBoxItem> getItems() {
        return items;
    }

    public ComboBoxItem getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return getHeight() / 6;
    }
}
