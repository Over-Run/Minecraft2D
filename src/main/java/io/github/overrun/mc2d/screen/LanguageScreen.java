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

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.text.TranslatableText;

import java.awt.Graphics;

import static io.github.overrun.mc2d.lang.Language.getByLocale;
import static io.github.overrun.mc2d.util.Constants.CANCEL;
import static io.github.overrun.mc2d.util.Constants.DONE;
import static io.github.overrun.mc2d.util.Coordinator.D_M;
import static io.github.overrun.mc2d.util.DrawHelper.drawCenteredText;

/**
 * @author squid233
 * @since 2020/10/13
 */
public final class LanguageScreen extends Screen {
    private final BiMap<String, IText> map = ImmutableBiMap.of(
            "en_us", genItem("en_us"),
            "zh_cn", genItem("zh_cn")
    );
    private final ComboBoxWidget cbw;

    public LanguageScreen() {
        cbw = new ComboBoxWidget(this,
                map.get("en_us"),
                map.get("zh_cn")
        ).setSelectedItem(map.get(Options.get(Options.LANG, Options.DEF_LANG)));
        addWidget(cbw);
        addButton(new ButtonWidget(-210, 30, 200, D_M, new TranslatableText(DONE), w -> {
            Options.setAndSave(Options.LANG, map.inverse().get(cbw.getSelectedItem().getText()));
            close();
        }));
        addButton(new ButtonWidget(10, 30, 200, D_M, new TranslatableText(CANCEL), w -> {
            cbw.setSelectedItem(map.get(Options.get(Options.LANG, Options.DEF_LANG)));
            close();
        }));
    }

    private IText genItem(String locale) {
        return new LiteralText(getByLocale(locale, "language.name")
                + " ("
                + getByLocale(locale, "language.region")
                + ")"
        );
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        drawCenteredText(g, new TranslatableText("options.mc2d.choose_lang"), 5);
        drawCenteredText(g,
                new TranslatableText("options.mc2d.current_lang", cbw.getSelectedItem().getText()),
                25);
        drawCenteredText(g, new TranslatableText("options.mc2d.lang_warning"), 45);
    }
}
