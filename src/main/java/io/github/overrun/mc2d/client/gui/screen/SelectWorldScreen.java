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

import io.github.overrun.mc2d.client.gui.screen.widget.AbstractButtonWidget;
import io.github.overrun.mc2d.client.gui.screen.widget.ButtonWidget;
import io.github.overrun.mc2d.client.gui.screen.widget.TextFieldWidget;
import io.github.overrun.mc2d.client.util.BuiltinGraphics;
import io.github.overrun.mc2d.event.TextFieldChangeCallback;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.text.TranslatableText;
import io.github.overrun.mc2d.text.UnformatText;
import io.github.overrun.mc2d.world.SavesManager;

import java.util.Random;

import static io.github.overrun.mc2d.util.Constants.*;
import static io.github.overrun.mc2d.util.Coordinator.D_M;

/**
 * TODO
 * @author squid233
 * @since 2020/12/18
 */
public final class SelectWorldScreen extends Screen {
    public SelectWorldScreen(Screen parent) { super(new TranslatableText("narrator.mc2d.chooseAWorld"), parent); }

    @Override
    protected void init() {
        String[] strArr = SavesManager.getAllSaveName();
        IText[] texts = new IText[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            texts[i] = new UnformatText(strArr[i]);
        }
        // addChild(new EntryListWidget(texts));
        addButton(new ButtonWidget(4, 80, 150, D_M, new TranslatableText("button.mc2d.createNewWorld"), b -> open(new NameWorld(this))));
        addButton(new ButtonWidget(4, 40, 150, new TranslatableText(BACK), button -> close()));
    }

    @Override
    public void render(BuiltinGraphics g) {
        renderBackground(g);
        super.render(g);
    }

    private static final class NameWorld extends Screen {
        private NameWorld(Screen parent) { super(new TranslatableText("button.mc2d.createNewWorld"), parent); }

        @Override
        protected void init() {
            class Tfw extends TextFieldWidget { Tfw() {
                super(-200, 50, 200, new LiteralText("New World"));
                setTooltips(t -> t.add(new LiteralText("A name of the world")));
            } }
            addChild(new Tfw());
            AbstractButtonWidget bw1 = addButton(new ButtonWidget(-200, 60, 98, new TranslatableText(DONE), b -> {}));
            addChild(new TextFieldWidget(-200, 95, 200, new LiteralText(String.valueOf(new Random().nextInt()))) {
                @Override public boolean isEnable() { return false; }
            }.setTooltips(t -> t.add(new LiteralText("A seed for the world"))));
            addButton(new ButtonWidget(4, 60, 98, new TranslatableText(CANCEL), b -> close()));
            addButton(new ButtonWidget(-200, 105, 200, new LiteralText("More World Options")));
            TextFieldChangeCallback.EVENT.register(textField -> bw1.setEnable(textField instanceof Tfw && !textField.getText().toString().isEmpty()));
        }

        @Override
        public void render(BuiltinGraphics g) {
            renderBackground(g);
            super.render(g);
        }
    }
}
