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
import io.github.overrun.mc2d.lang.Language;
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.text.TranslatableText;
import io.github.overrun.mc2d.util.Constants;
import io.github.overrun.mc2d.util.ResourceLocation;

import java.awt.Graphics;

/**
 * @author squid233
 * @since 2020/10/13
 */
public class LanguagesScreen extends ScreenHandler {
    private int scroll = 0;
    private final Language[] lang = {Language.EN_US, Language.ZH_CN};

    public LanguagesScreen() {
        addButton(new ButtonWidget(10, 200, TranslatableText.of(Constants.BUTTON_DONE), button -> Screens.setOpening(Screens.TITLE_SCREEN)));
        for (int i = 0; i < lang.length; i++) {
            LiteralText lt = LiteralText.of(lang[i].getName());
            int finalI = i;
            addButton(new ButtonWidget(75 + (i << 5), 600, lt, button -> {
                String c = lang[finalI].getCode();
                if (!c.equals(Language.getCurrentLang())) {
                    Language.setCurrentLang(c);
                    Language.reload();
                    Options.set(Options.LANG, c);
                }
            }));
        }
    }

    @Override
    public void render(Graphics g) {
        drawOptionsBg(g);
        drawDefaultBackground(g);
        Screen.drawRect(g, 0, 80, Minecraft2D.getWidth(), Minecraft2D.getHeight(), DEFAULT_BACKGROUND);
        super.render(g);
        Screen.drawText(g, 10, 45, TranslatableText.of("text.minecraft2d.choose_lang"));
    }

    @Override
    public ResourceLocation getTexture() {
        return Images.OPTIONS_BG_ID;
    }
}
