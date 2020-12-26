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

import io.github.overrun.mc2d.event.TextFieldChangeCallback;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.UnformatText;

import javax.swing.JOptionPane;
import java.awt.Image;

import static io.github.overrun.mc2d.util.Coordinator.U_M;
import static io.github.overrun.mc2d.util.Images.BUTTON_DISABLE;

/**
 * @author squid233
 * @since 2020/12/24
 */
public class TextFieldWidget extends ButtonWidget {
    public TextFieldWidget(int x, int y, int width, int layout, IText initValue) {
        super(x, y, width, layout, initValue);
        setAction(b -> {
            IText t = getText();
            String s = JOptionPane.showInputDialog("", t);
            setText(new UnformatText(s != null ? s : ""));
            if (!t.equals(getText())) { TextFieldChangeCallback.post(this); }
        });
    }

    public TextFieldWidget(int x, int y, int width, IText initValue) {
        this(x, y, width, U_M, initValue);
    }

    public TextFieldWidget(int x, int y, int width, int layout) {
        this(x, y, width, layout, IText.of());
    }

    public TextFieldWidget(int x, int y, int width) {
        this(x, y, width, U_M);
    }

    @Override
    public Image getTexture() {
        return BUTTON_DISABLE;
    }

    @Override
    public boolean isEnable() {
        return true;
    }
}
