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

import io.github.overrun.mc2d.text.IText;

/**
 * @author squid233
 * @since 2021/01/25
 */
public class ButtonWidget extends AbstractPressableButtonWidget {
    public static final TooltipSupplier EMPTY = (button, mouseX, mouseY) -> {
    };
    protected final PressAction onPress;
    protected final TooltipSupplier tooltipSupplier;

    public ButtonWidget(int x, int y, int width, int height, IText message, PressAction onPress) {
        this(x, y, width, height, message, onPress, EMPTY);
    }

    public ButtonWidget(int x, int y, int width, int height, IText message, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message);
        this.onPress = onPress;
        this.tooltipSupplier = tooltipSupplier;
    }

    @Override
    public void onPress() {
        onPress.onPress(this);
    }

    @Override
    public void renderButton(int mouseX, int mouseY) {
        super.renderButton(mouseX, mouseY);
        if (isHovered()) {
            renderToolTip(mouseX, mouseY);
        }
    }

    @Override
    public void renderToolTip(int mouseX, int mouseY) {
        super.renderToolTip(mouseX, mouseY);
        tooltipSupplier.onTooltip(this, mouseX, mouseY);
    }

    public interface TooltipSupplier {
        void onTooltip(ButtonWidget button, int mouseX, int mouseY);
    }

    public interface PressAction {
        void onPress(ButtonWidget button);
    }
}
