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

package io.github.overrun.mc2d.text;

import io.github.overrun.mc2d.screen.Screen;

import java.awt.Color;
import java.util.Objects;

/**
 * @author squid233
 * @since 2020/10/16
 */
public class Style {
    public static final Style EMPTY = new Style(false, false, Color.WHITE, null);
    private final boolean isBold;
    private final boolean isItalic;
    private final Color color;
    private final PressAction pressAction;

    private Style(boolean isBold, boolean isItalic, Color color, PressAction pressAction) {
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.color = color;
        this.pressAction = pressAction;
    }

    public interface PressAction {
        void onPress(Screen screen);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean isBold;
        private boolean isItalic;
        private Color color = Color.WHITE;
        private PressAction pressAction;

        public Builder bold() {
            isBold = true;
            return this;
        }

        public Builder italic() {
            isItalic = true;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder pressAction(PressAction action) {
            pressAction = action;
            return this;
        }

        public Style build() {
            return new Style(isBold, isItalic, color, pressAction);
        }
    }

    public boolean isBold() {
        return isBold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public Color getColor() {
        return color;
    }

    public PressAction getPressAction() {
        return Objects.requireNonNullElse(pressAction, text -> {});
    }
}
