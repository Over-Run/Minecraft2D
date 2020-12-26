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

package io.github.overrun.mc2d.client.gui.screen;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.gui.screen.widget.ButtonWidget;
import io.github.overrun.mc2d.client.gui.screen.widget.LanguageButtonWidget;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.text.TranslatableText;

import java.awt.Graphics;

import static io.github.overrun.mc2d.Minecraft2D.VERSION;
import static io.github.overrun.mc2d.util.Coordinator.*;
import static io.github.overrun.mc2d.util.DrawHelper.drawCenterImage;
import static io.github.overrun.mc2d.util.DrawHelper.drawText;
import static io.github.overrun.mc2d.util.Images.LOGO;

/**
 * @author squid233
 * @since 2020/10/12
 */
public final class TitleScreen extends Screen {
    public TitleScreen() {
        super(IText.of());
    }

    @Override
    protected void init() {
        initWidgets();
    }

    private void initWidgets() {
        int y = 216, spacingY = 48;
        addButton(new ButtonWidget(-200, y, 200, U_M, new TranslatableText("button.mc2d.singleplayer"),
                w -> open(new SelectWorldScreen(this))));
        addButton(new ButtonWidget(-200, y + spacingY, 200, U_M, new TranslatableText("button.mc2d.multiplayer")));
        addButton(new ButtonWidget(-200, y + spacingY * 2, 200, U_M, new LiteralText("Mods")));
        addButton(new LanguageButtonWidget(-248, y + spacingY * 3 + 24, U_M, w -> open(new LanguageScreen(this))));
        addButton(new ButtonWidget(-200, y + spacingY * 3 + 24, 98, U_M, new TranslatableText("button.mc2d.options"),
                w -> open(new OptionsScreen(this))));
        addButton(new ButtonWidget(4, y + spacingY * 3 + 24, 98, U_M, new TranslatableText("button.mc2d.exitGame"),
                w -> {
                    Mc2dClient.getInstance().getRenderer().stop();
                    Mc2dClient.getInstance().getFrame().dispose();
                    System.exit(0);
                }));
    }

    @Override
    public void render(Graphics g) {
        renderBackground(g);
        super.render(g);
        drawCenterImage(g, LOGO, 58, LOGO.getWidth() << 1, LOGO.getHeight() << 1);
        LiteralText title = new LiteralText("Minecraft2D %s", VERSION);
        drawText(g, title, 0, title.getPrevHeight(g), D_L);
    }
}
