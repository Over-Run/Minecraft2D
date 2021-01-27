/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Over-Run
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

package io.github.overrun.mc2d.client.gui.screen.ingame;

import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.client.gui.screen.world.SavingWorldScreen;
import io.github.overrun.mc2d.client.gui.widget.ButtonWidget;
import io.github.overrun.mc2d.text.TextColor;

/**
 * @author squid233
 * @since 2021/01/26
 */
public final class PauseScreen extends Screen {
    private final Screen parent;

    public PauseScreen(Screen parent) {
        super("Pausing");
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        if (client.world != null) {
            client.world.save();
        }
        addButton(new ButtonWidget((width >> 1) - 300,
                80,
                300,
                20,
                "Back to Game",
                b -> onClose()));
        addButton(new ButtonWidget((width >> 1) - 300,
                130,
                300,
                20,
                "Save and Back to Title Screen",
                b -> client.openScreen(new SavingWorldScreen())));
    }

    @Override
    public void render(int mouseX, int mouseY) {
        renderBackground();
        super.render(mouseX, mouseY);
        drawCenteredText(textRenderer, title, width >> 1, 50, TextColor.WHITE);
    }

    @Override
    public void onClose() {
        super.onClose();
        client.openScreen(parent);
    }
}
