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

import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.text.TranslatableText;

import java.awt.*;

import static io.github.overrun.mc2d.screen.Screens.NO;
import static io.github.overrun.mc2d.util.Coordinator.U_M;
import static io.github.overrun.mc2d.util.DrawHelper.drawCenterImage;
import static io.github.overrun.mc2d.util.Images.LOGO;

/**
 * @author squid233
 * @since 2020/11/24
 */
public final class TitleScreen extends Screen {
    public TitleScreen(Screen parent) {
        super(parent);
        addButton(new ButtonWidget(-100, 62, 200, U_M, new TranslatableText("button.mc2d.singleplayer"),
                w -> open(NO)));
        addButton(new ButtonWidget(-100, 88, 200, U_M, new TranslatableText("button.mc2d.multiplayer"),
                w -> open(NO)));
        addButton(new ButtonWidget(-100, 114, 200, U_M, new LiteralText("Mods"),
                w -> open(NO)));
        addButton(new ButtonWidget(-100, 140, 200, U_M, new TranslatableText("button.mc2d.options"),
                w -> open(NO)));
        addButton(new ButtonWidget(-100, 166, 200, U_M, new TranslatableText("button.mc2d.exit_game"),
                w -> System.exit(0)));
        addButton(new LanguageButtonWidget(60, 192, U_M, w -> open(Screens.LANG_SCREEN)));
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        drawCenterImage(g, LOGO, 10);
    }
}
