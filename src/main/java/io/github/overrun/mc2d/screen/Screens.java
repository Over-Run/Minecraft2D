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

/**
 * @author squid233
 * @since 2020/10/01
 */
public class Screens {
    public static final Screen EMPTY = new Screen() {};
    public static final TitleScreen TITLE_SCREEN = new TitleScreen();
    public static final CreativeTabScreen CREATIVE_TAB = new CreativeTabScreen();
    public static final LanguagesScreen LANGUAGES_SCREEN = new LanguagesScreen();

    private static Screen openingScreen = TITLE_SCREEN;

    public static void setOpening(Screen screen) {
        if (openingScreen != screen) {
            openingScreen = screen;
        }
    }

    public static void closeAll() {
        openingScreen = EMPTY;
    }

    public static Screen getOpening() {
        if (openingScreen == null) {
            openingScreen = EMPTY;
        }
        return openingScreen;
    }

    public static boolean isOpening(Screen screen) {
        return getOpening() == screen;
    }
}
