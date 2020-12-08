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

package io.github.overrun.mc2d.input;

import io.github.overrun.mc2d.screen.GameScreen;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static io.github.overrun.mc2d.input.KeyBinding.*;
import static io.github.overrun.mc2d.screen.Screens.getOpenScreen;

/**
 * @author squid233
 * @since 2020/11/22
 */
public final class KeyInput extends KeyAdapter {
    public static final char K_ESC = '\u001b';

    private static int pressing = 0;

    @Override
    public void keyPressed(KeyEvent e) {
        pressing = e.getKeyCode();
        execute(e, PRESS);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        execute(e, TYPED);
        getOpenScreen().onKeyDown(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (pressing != 0)
            execute(e, RELEASE);
        pressing = 0;
    }

    public static boolean getKeyDown(int keyCode) {
        return pressing == keyCode;
    }

    private void execute(KeyEvent e, int type) {
        KeyBinding kb = getKeyBinding(e.getKeyChar());
        if (kb.getType() == type && getOpenScreen() instanceof GameScreen)
            kb.onTyping().accept(kb);
    }
}
