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
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static io.github.overrun.mc2d.util.Coordinator.*;
import static io.github.overrun.mc2d.util.DrawHelper.drawText;
import static io.github.overrun.mc2d.util.Images.*;

/**
 * @author squid233
 * @since 2020/10/12
 */
public class ButtonWidget extends AbstractButtonWidget {
    private final int x;
    private final int y;
    private final int width;
    private final int layout;
    private final IText text;
    private final PressAction action;
    private final Consumer<List<IText>> tooltipSupplier;
    private final List<IText> tooltips = new ObjectArrayList<>(1);
    private boolean isEnable = true;

    public ButtonWidget(int x,
                        int y,
                        int width,
                        int layout,
                        IText text,
                        PressAction action,
                        Consumer<List<IText>> tooltipSupplier) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.layout = layout;
        this.text = text;
        this.action = action;
        this.tooltipSupplier = tooltipSupplier;
        tooltipSupplier.accept(tooltips);
    }

    public ButtonWidget(int x,
                        int y,
                        int width,
                        IText text,
                        PressAction action,
                        Consumer<List<IText>> tooltipSupplier) {
        this(x, y, width, D_M, text, action, tooltipSupplier);
    }

    public ButtonWidget(int x,
                        int y,
                        int width,
                        int layout,
                        IText text,
                        PressAction action) {
        this(x, y, width, layout, text, action, t -> {});
    }

    public ButtonWidget(int x,
                        int y,
                        int width,
                        IText text,
                        PressAction action) {
        this(x, y, width, text, action, t -> {});
    }

    public ButtonWidget(int x,
                        int y,
                        int width,
                        int layout,
                        IText text) {
        this(x, y, width, layout, text, w -> {});
        setEnable(false);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        int tY = (getHeight() >> 1) - (text.getPrevHeight(g) >> 1);
        drawText(g, text,
                transformation(
                        getX() + (getWidth() >> 1) - (text.getPrevWidth(g) >> 1),
                        layout <= M_R ? getY() + tY : getY() - tY,
                        layout)
        );
    }

    public ButtonWidget setEnable(boolean enable) {
        isEnable = enable;
        return this;
    }

    public IText getText() {
        return text;
    }

    public List<IText> getTooltips() {
        return tooltips;
    }

    @Override
    public boolean isEnable() {
        return isEnable;
    }

    @Override
    public Image getUsualTexture() {
        return BUTTON;
    }

    @Override
    public Image getHoverTexture() {
        return BUTTON_HOVER;
    }

    @Override
    public Image getDisableTexture() {
        return BUTTON_DISABLE;
    }

    @Override
    public PressAction getAction() {
        return action;
    }

    @Override
    public int getHeight() {
        return 25;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public Point getPrevPos() {
        return transformation(getX(), getY(), layout);
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ButtonWidget that = (ButtonWidget) o;
        return getX() == that.getX()
                && getY() == that.getY()
                && getWidth() == that.getWidth()
                && layout == that.layout
                && Objects.equals(text, that.text)
                && Objects.equals(getAction(), that.getAction())
                && Objects.equals(tooltipSupplier, that.tooltipSupplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(),
                getY(),
                getWidth(),
                layout,
                text,
                getAction(),
                tooltipSupplier);
    }
}
