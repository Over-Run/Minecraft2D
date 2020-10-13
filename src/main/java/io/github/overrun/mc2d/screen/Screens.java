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

import io.github.overrun.mc2d.util.ResourceLocation;

/**
 * @author squid233
 * @since 2020/10/01
 */
public class Screens {
    public static final ScreenHandler EMPTY = new ScreenHandler() {
        @Override
        public boolean canUse() {
            return false;
        }

        @Override
        public ResourceLocation getTexture() {
            return null;
        }
    };
    public static final TitleScreen TITLE_SCREEN = new TitleScreen();
    public static final CreativeTabScreen CREATIVE_TAB = new CreativeTabScreen();
    public static final LanguagesScreen LANGUAGES_SCREEN = new LanguagesScreen();

    private static ScreenHandler openingScreenHandler = TITLE_SCREEN;

    public static void setOpening(ScreenHandler screenHandler) {
        if (openingScreenHandler != screenHandler) {
            openingScreenHandler = screenHandler;
        }
    }

    public static void closeAll() {
        openingScreenHandler = EMPTY;
    }

    public static ScreenHandler getOpening() {
        if (openingScreenHandler == null) {
            openingScreenHandler = EMPTY;
        }
        return openingScreenHandler;
    }

    public static boolean isOpening(ScreenHandler screenHandler) {
        return getOpening() == screenHandler;
    }
}
