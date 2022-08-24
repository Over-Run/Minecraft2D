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

package io.github.overrun.mc2d.client.gui.widget;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.gui.Drawable;
import io.github.overrun.mc2d.client.gui.DrawableHelper;
import io.github.overrun.mc2d.client.gui.Element;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.Style;
import io.github.overrun.mc2d.text.TextColor;
import io.github.overrun.mc2d.util.Identifier;

/**
 * @author squid233
 * @since 2021/01/25
 */
public abstract class AbstractButtonWidget extends DrawableHelper implements Drawable, Element {
    public static final Identifier WIDGETS_LOCATION = new Identifier("textures/gui/widgets.png");
    public static final TextColor NOT_ACTIVE_COLOR = new TextColor("", 0xffa0a0a0, 0xff202020);
    protected int width;
    protected int height;
    public int x;
    public int y;
    private IText message;
    protected boolean hovered;
    public boolean active = true;

    public AbstractButtonWidget(int x, int y, int width, int height, IText message) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = message;
    }

    protected int getImageY(boolean hovered) {
        int i = 1;
        if (!active) {
            i = 0;
        } else if (hovered) {
            i = 2;
        }
        return i;
    }

    public void renderButton(int mouseX, int mouseY) {
        Mc2dClient client = Mc2dClient.getInstance();
        client.getTextureManager().bindTexture(WIDGETS_LOCATION);
        int i = getImageY(isHovered());
        drawTexture(x, y, 0, 46 + i * 20, width >> 1, height);
        drawTexture(x + width, y, 200 - (width >> 1), 46 + i * 20, width >> 1, height);
        renderBg(client, mouseX, mouseY);
        drawCenteredText(client.textRenderer,
            getMessage().setStyle(Style.EMPTY.withColor(active ? TextColor.WHITE : NOT_ACTIVE_COLOR)),
            x + width,
            // y + (height * 2 / 2 - 16 / 2)
            y + height - 8);
    }

    protected void renderBg(Mc2dClient client, int mouseX, int mouseY) {
    }

    public void onClick(int mouseX, int mouseY) {
    }

    public void renderToolTip(int mouseX, int mouseY) {
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public IText getMessage() {
        return message;
    }

    public void setMessage(IText message) {
        this.message = message;
    }

    public boolean isHovered() {
        return hovered;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        hovered = mouseX >= x && mouseY >= y && mouseX < x + (width << 1) && mouseY < y + (height << 1);
        renderButton(mouseX, mouseY);
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return active && hovered;
    }
}
