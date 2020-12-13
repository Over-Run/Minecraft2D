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

import io.github.overrun.mc2d.text.TranslatableText;

import java.awt.Graphics;

import static io.github.overrun.mc2d.util.Constants.BACK;
import static io.github.overrun.mc2d.util.Coordinator.D_M;
import static io.github.overrun.mc2d.util.Coordinator.U_M;
import static io.github.overrun.mc2d.util.DrawHelper.drawCenteredText;

/**
 * @author squid233
 * @since 2020/12/12
 */
public class OptionsScreen extends Screen {
    public OptionsScreen() {
        addButton(new ButtonWidget(-100, 30, 200, D_M, new TranslatableText(BACK), w -> close()));
        addButton(new ButtonWidget(-210, 55, 200, U_M, new TranslatableText("button.mc2d.language"),
                w -> open(Screens.LANG_SCREEN)));
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        drawCenteredText(g, new TranslatableText("button.mc2d.options"), 5);
    }
}
