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
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.text.IText;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author squid233
 * @since 2020/10/16
 */
public class ComboBox extends ScreenComp {
    public static final Color BG_COLOR = new Color(64, 64, 64, 32);
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final ObjectList<IText> texts = new ObjectArrayList<>();
    private IText selectedItem;
    private boolean isWide;

    public ComboBox(int x, int y, int width, int height, ObjectList<IText> texts) {
        this(x, y, width, height);
        this.texts.addAll(texts);
    }

    public ComboBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ComboBox(int x, int y, int height, ObjectList<IText> texts) {
        this(x, y, height);
        this.texts.addAll(texts);
    }

    public ComboBox(int x, int y, int height) {
        this(x, y, 0, height);
        isWide = true;
    }

    public void render(Graphics g) {
        ScreenUtil.drawBg(g, BG_COLOR);
        for (int i = 0; i < texts.size(); i++) {
            ScreenUtil.drawText(g, (Minecraft2D.getWidth() >> 1) - (texts.get(i).getDisplayLength() >> 1) - 8, (i << 4) + i + y, texts.get(i));
        }
    }

    public ComboBox addText(IText text) {
        texts.add(text);
        return this;
    }

    public ObjectList<IText> getTexts() {
        return texts;
    }

    public ComboBox setSelectedItem(IText selectedItem) {
        this.selectedItem = selectedItem;
        return this;
    }

    public IText getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return isWide ? Minecraft2D.getWidth() : width;
    }

    @Override
    public int getHeight() {
        return height + Minecraft2D.getHeight() - Options.getI(Options.HEIGHT, Integer.parseInt(Options.HEIGHT_DEF));
    }
}
