/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Overrun Organization
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

package io.github.overrun.mc2d.client.gui.screen.world;

import io.github.overrun.mc2d.client.gui.screen.DirtScreen;
import io.github.overrun.mc2d.client.gui.screen.TitleScreen;
import io.github.overrun.mc2d.text.TranslatableText;

/**
 * @author squid233
 * @since 2021/01/26
 */
public final class SavingWorldScreen extends DirtScreen {
    public SavingWorldScreen() {
        super(new TranslatableText("Saving World"));
    }

    @Override
    public void tick() {
        super.tick();
        if (client.world != null) {
            client.world.save(client.player);
        }
        onClose();
    }

    @Override
    public void onClose() {
        super.onClose();
        client.openScreen(new TitleScreen());
        client.world = null;
        client.worldRenderer = null;
        client.player = null;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
