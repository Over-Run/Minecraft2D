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

package io.github.overrun.mc2d.client.gui.screen.widget;

import io.github.overrun.mc2d.client.util.BuiltinGraphics;
import io.github.overrun.mc2d.text.IText;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static io.github.overrun.mc2d.util.Coordinator.*;
import static io.github.overrun.mc2d.util.Images.*;
import static io.github.overrun.mc2d.util.ImgUtil.getSubImage;

/**
 * @author squid233
 * @since 2020/10/12
 */
public class ButtonWidget extends AbstractButtonWidget {
    private final int x;
    private final int y;
    private final int width;
    private final int layout;
    private IText text;
    private PressAction action;
    private List<IText> tooltips = new ArrayList<>(1);
    private boolean isEnable = true;

    public ButtonWidget(int x, int y, int width, int layout, IText text, PressAction action, Consumer<List<IText>> tooltipSupplier) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.layout = layout;
        this.text = text;
        this.action = action;
        tooltipSupplier.accept(tooltips);
    }

    public ButtonWidget(int x, int y, int width, IText text, PressAction action, Consumer<List<IText>> tooltipSupplier) {
        this(x, y, width, D_M, text, action, tooltipSupplier); }

    public ButtonWidget(int x, int y, int width, int layout, IText text, PressAction action) { this(x, y, width, layout, text, action, t -> {}); }

    public ButtonWidget(int x, int y, int width, IText text, PressAction action) { this(x, y, width, text, action, t -> {}); }

    public ButtonWidget(int x,
                        int y,
                        int width,
                        int layout,
                        IText text) {
        this(x, y, width, layout, text, w -> {});
        setEnable(false);
    }

    public ButtonWidget(int x, int y, int width, IText text) {
        this(x, y, width, D_M, text);
    }

    @Override
    public void render(BuiltinGraphics g) {
        BufferedImage bi = null;
        if (getTexture() instanceof BufferedImage) {
            bi = (BufferedImage) getTexture();
        }
        // Draw top border
        g.drawImage(bi == null ? EMPTY : getSubImage(bi, 0, 0, bi.getWidth(), 1), getPrevPos().x, getPrevPos().y, getWidth() << 1, 2);
        // Draw left border
        g.drawImage(bi == null ? EMPTY : getSubImage(bi, 0, 0, 1, bi.getHeight()), getPrevPos().x, getPrevPos().y, 2, getHeight() << 1);
        // Draw right border
        g.drawImage(bi == null ? EMPTY : getSubImage(bi, bi.getWidth() - 1, 0, 1, 20), getPrevPos().x + (getWidth() << 1) - 2, getPrevPos().y, 2, getHeight() << 1);
        // Draw bottom border
        g.drawImage(bi == null ? EMPTY : getSubImage(bi, 0, bi.getHeight() - 1, bi.getWidth(), 1), getPrevPos().x, getPrevPos().y + (getHeight() << 1) - 2, getWidth() << 1, 2);
        // Draw center
        g.drawImage(bi == null ? EMPTY : getSubImage(bi, 1, 1, bi.getWidth() - 2, bi.getHeight() - 2), getPrevPos().x + 2, getPrevPos().y + 2, (getWidth() << 1) - 4, (getHeight() << 1) - 4);
        int tY = getHeight() - (text.getPrevHeight(g.getGraphics()) >> 1);
        g.drawText(text, transformation(
                getX() + getWidth() - (text.getPrevWidth(g.getGraphics()) >> 1),
                layout <= M_R ? getY() + tY : getY() - tY, layout));
    }

    public ButtonWidget setTooltips(List<IText> tooltips) {
        if (tooltips != null) { this.tooltips = tooltips; }
        return this;
    }

    public ButtonWidget setTooltips(Consumer<List<IText>> tooltips) {
        tooltips.accept(this.tooltips);
        return this;
    }

    public void setText(IText text) {
        this.text = text;
    }

    @Override
    public ButtonWidget setEnable(boolean enable) { isEnable = enable; return this; }

    @Override
    public IText getText() { return text; }

    @Override
    public List<IText> getTooltips() { return tooltips; }

    public void setAction(PressAction action) { this.action = action; }

    @Override
    public boolean isEnable() { return isEnable; }

    @Override
    public Image getUsualTexture() { return BUTTON; }

    @Override
    public Image getHoverTexture() { return BUTTON_HOVER; }

    @Override
    public Image getDisableTexture() { return BUTTON_DISABLE; }

    @Override
    public PressAction getAction() { return action; }

    @Override
    public int getHeight() { return 20; }

    @Override
    public int getWidth() { return width; }

    @Override
    public Point getPrevPos() { return transformation(getX(), getY(), layout); }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ButtonWidget that = (ButtonWidget) o;
        return getX() == that.getX() && getY() == that.getY()
                && getWidth() == that.getWidth() && layout == that.layout
                && isEnable() == that.isEnable() && Objects.equals(getText(), that.getText())
                && Objects.equals(getAction(), that.getAction()) && Objects.equals(getTooltips(), that.getTooltips());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getWidth(), layout, getText(), getAction(), getTooltips(), isEnable());
    }
}
