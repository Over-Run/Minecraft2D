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
import io.github.overrun.mc2d.util.stream.ArrayStream;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.awt.*;
import java.util.List;

import static io.github.overrun.mc2d.Minecraft2D.getHeight;
import static io.github.overrun.mc2d.Minecraft2D.getWidth;
import static io.github.overrun.mc2d.screen.Screens.BG_COLOR;
import static io.github.overrun.mc2d.util.Coordinator.*;
import static io.github.overrun.mc2d.util.Coordinator.U_M;
import static io.github.overrun.mc2d.util.DrawHelper.drawRect;
import static io.github.overrun.mc2d.util.DrawHelper.fillRect;

/**
 * @author squid233
 * @since 2020/12/06
 */
public class ComboBoxWidget extends ScreenWidget {
    private final List<ComboBoxItem> items;
    private ComboBoxItem selectedItem;

    public ComboBoxWidget(List<ComboBoxItem> items) {
        this.items = items;
    }

    public ComboBoxWidget(ComboBoxItem... items) {
        this(new ObjectArrayList<>(items.length));
        ((ObjectArrayList<ComboBoxItem>) getItems()).addElements(0, items);
    }

    @Override
    public void render(Graphics g) {
        fillRect(g, BG_COLOR, getX(), getY(), getWidth(), getY() << 2, U_L);
        ArrayStream.forEach(items, (item, i) -> {
            final IText t = item.getContent();
            final Point p = transformation(-(t.getPrevWidth(g) >> 1),
                    getY() + i * t.getPrevHeight(g),
                    U_M);
            if (getSelectedItem() == item) {
                drawRect(g,
                        Color.WHITE,
                        -(t.getPrevWidth(g) >> 1) - 4,
                        p.y - 30,
                        t.getPrevWidth(g) + 4,
                        t.getPrevHeight(g) + 4,
                        U_M);
            }
            System.out.println(t + ":width:" + t.getPrevWidth(g));
            System.out.println(t + ":height:" + t.getPrevHeight(g));
            item.render(g, p.y - 30);
        });
    }

    public ComboBoxWidget setSelectedItem(ComboBoxItem item) {
        selectedItem = item;
        return this;
    }

    public List<ComboBoxItem> getItems() {
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
