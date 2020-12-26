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

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import io.github.overrun.mc2d.client.gui.screen.widget.ButtonWidget;
import io.github.overrun.mc2d.client.gui.screen.widget.EntryListWidget;
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.text.TranslatableText;

import java.awt.Graphics;

import static io.github.overrun.mc2d.lang.Language.getByLocale;
import static io.github.overrun.mc2d.util.Constants.BACK;
import static io.github.overrun.mc2d.util.DrawHelper.drawCenteredText;

/**
 * @author squid233
 * @since 2020/10/13
 */
public final class LanguageScreen extends Screen {
    public static final BiMap<String, IText> MAP = ImmutableBiMap.of(
            "en_us", genItem("en_us"),
            "zh_cn", genItem("zh_cn")
    );
    private EntryListWidget cbw;

    public LanguageScreen(Screen parent) {
        super(new TranslatableText("narrator.mc2d.chooseLang"), parent);
    }

    @Override
    protected void init() {
        cbw = new EntryListWidget(
                MAP.get("en_us"),
                MAP.get("zh_cn")
        ).setSelectedItem(MAP.get(Options.get(Options.LANG, Options.DEF_LANG)))
                .setAction(b -> Options.setAndSave(Options.LANG, MAP.inverse().get(cbw.getSelectedItem().getText())));
        addChild(cbw);
        addButton(new ButtonWidget(-200, 60, 200, new TranslatableText(BACK), b -> close()));
    }

    private static IText genItem(String locale) {
        return new LiteralText(String.format("%s (%s)", getByLocale(locale, "language.name"), getByLocale(locale, "language.region")));
    }

    @Override
    public void render(Graphics g) {
        renderBackground(g);
        super.render(g);
        drawCenteredText(g, new TranslatableText("narrator.mc2d.langWarning"), 25);
    }
}
