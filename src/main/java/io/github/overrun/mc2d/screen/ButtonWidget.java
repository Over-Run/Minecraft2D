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
import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.text.IText;

import java.awt.Image;
import java.util.Objects;

/**
 * @author squid233
 * @since 2020/10/12
 */
public class ButtonWidget {
    public static final Image BUTTON = Images.getImagePart(Images.WIDGETS, 0, 66, 200, 20);
    public static final Image BUTTON_CHOOSE = Images.getImagePart(Images.WIDGETS, 0, 86, 200, 20);

    private final int x;
    private final int y;
    private final int width;
    private final IText text;
    private final PressAction action;
    private boolean isCenter;

    public ButtonWidget(int x, int y, int width, IText text, PressAction action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.text = text;
        this.action = action;
        this.isCenter = false;
    }

    public ButtonWidget(int y, int width, IText text, PressAction action) {
        this(0, y, width, text, action);
        this.isCenter = true;
    }

    public int getX() {
        return isCenter ? (Minecraft2D.getWidth() >> 1) - (width >> 1) - 8 : x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return 30;
    }

    public PressAction getAction() {
        return action;
    }

    public IText getText() {
        return text;
    }

    public Image getTexture() {
        return BUTTON;
    }

    public boolean isCenter() {
        return isCenter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ButtonWidget widget = (ButtonWidget) o;
        return getX() == widget.getX() &&
                getY() == widget.getY() &&
                getWidth() == widget.getWidth() &&
                getAction() == widget.getAction();
    }

    public interface PressAction {
        void onPress(ButtonWidget widget);
    }
}
