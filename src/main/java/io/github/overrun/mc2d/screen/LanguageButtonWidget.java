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

import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.text.LiteralText;

import java.awt.Image;

/**
 * @author squid233
 * @since 2020/10/13
 */
public class LanguageButtonWidget extends ButtonWidget {
    public static final Image BUTTON = Images.getImagePart(Images.WIDGETS, 0, 106, 20, 20);
    public static final Image BUTTON_HOVER = Images.getImagePart(Images.WIDGETS, 0, 126, 20, 20);

    public LanguageButtonWidget(int x, int y, PressAction action) {
        super(x, y, 40, LiteralText.EMPTY, action);
    }

    @Override
    public int getHeight() {
        return 40;
    }

    @Override
    public Image getTexture() {
        return isHover ? BUTTON_HOVER : BUTTON;
    }
}
