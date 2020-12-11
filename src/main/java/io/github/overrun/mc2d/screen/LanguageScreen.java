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

import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.text.TranslatableText;

import java.awt.Graphics;
import java.util.Map;

import static io.github.overrun.mc2d.lang.Language.getByLocale;
import static io.github.overrun.mc2d.util.Constants.CANCEL;
import static io.github.overrun.mc2d.util.Constants.DONE;
import static io.github.overrun.mc2d.util.Coordinator.D_M;

/**
 * @author squid233
 * @since 2020/12/06
 */
public class LanguageScreen extends Screen {
    private static final Map<String, ComboBoxItem> MAP = Map.of(
            "en_us", genItem("en_us"),
            "zh_cn", genItem("zh_cn")
    );
    public LanguageScreen(Screen parent) {
        super(parent);
        addButton(new ButtonWidget(-210, 30, 200, D_M, new TranslatableText(DONE), w -> close()));
        addButton(new ButtonWidget(10, 30, 200, D_M, new TranslatableText(CANCEL), w -> close()));
        addWidget(new ComboBoxWidget(
                MAP.get("en_us"),
                MAP.get("zh_cn")
        ).setSelectedItem(MAP.get(Options.get(Options.LANG, "en_us"))));
    }

    private static ComboBoxItem genItem(String locale) {
        return new ComboBoxItem(new LiteralText(getByLocale(locale, "language.name")
                + " ("
                + getByLocale(locale, "language.region")
                + ")"
        ));
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }
}
