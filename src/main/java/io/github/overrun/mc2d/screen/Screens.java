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

import io.github.overrun.mc2d.logger.Logger;
import io.github.overrun.mc2d.util.registry.Registry;

import java.awt.Color;

import static io.github.overrun.mc2d.Minecraft2D.LOGGER;

/**
 * @author squid233
 * @since 2020/11/24
 */
public final class Screens {
    public static final Color BG_COLOR = new Color(64, 64, 64, 64);

    public static final Screen TITLE_SCREEN;
    public static final Screen LANG_SCREEN;

    static {
        TITLE_SCREEN = register("title_screen", new TitleScreen(null));
        LANG_SCREEN = register("lang_screen", new LanguageScreen(TITLE_SCREEN));
    }

    private static Screen screen = TITLE_SCREEN;

    private static Screen register(String id, Screen screen) {
        if (Logger.logDebugEnable()) {
            LOGGER.debug("Registered screen: {}", Registry.register(Registry.SCREEN, id, screen));
        }
        return screen;
    }

    public static void openScreen(Screen s) {
        screen = s;
    }

    public static Screen getOpenScreen() {
        return screen;
    }
}
