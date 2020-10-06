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

package io.github.overrun.mc2d.util;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.screen.Screen;
import io.github.overrun.mc2d.screen.ScreenHandler;

import java.awt.Graphics;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class Highlight {
    public static void black(Graphics g, int prevX, int prevY, int width, int height) {
        if (
                Mc2dClient.getInstance().getMousePosition().x >= prevX
                && Mc2dClient.getInstance().getMousePosition().x <= prevX + (width - 1)
                && Mc2dClient.getInstance().getMousePosition().y >= prevY
                && Mc2dClient.getInstance().getMousePosition().y <= prevY + (height - 1)
        ) {
            g.drawRect(prevX, prevY, 15, 15);
        }
    }

    public static void block(Graphics g, int prevX, int prevY) {
        black(g, prevX, prevY, 16, 16);
    }

    public static void slot(Graphics g, int prevX, int prevY, int width, int height) {
        if (
                Mc2dClient.getInstance().getMousePosition().x >= prevX
                && Mc2dClient.getInstance().getMousePosition().x <= prevX + (width - 1)
                && Mc2dClient.getInstance().getMousePosition().y >= prevY
                && Mc2dClient.getInstance().getMousePosition().y <= prevY + (height - 1)
        ) {
            Screen.operationWithColor(g, ScreenHandler.SLOT_HIGHLIGHT, (gg) -> gg.fillRect(prevX, prevY, width, height));
        }
    }
}
