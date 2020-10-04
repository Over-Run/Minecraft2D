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

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.util.ResourceLocation;
import io.github.overrun.mc2d.util.list.SafeList;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * @author squid233
 * @since 2020/10/01
 */
public abstract class ScreenHandler implements Screen {
    public static final Color DEFAULT_BACKGROUND = new Color(64, 64, 64, 128);
    public static final Color SLOT_HIGHLIGHT = new Color(255, 255, 255, 128);

    protected final SafeList<Slot> slots = new SafeList<>();

    protected static void drawDefaultBackground(Graphics g) {
        Screen.operationWithColor(g, DEFAULT_BACKGROUND, (gg) -> gg.fillRect(0, 0, Mc2dClient.getInstance().getWidth(), Mc2dClient.getInstance().getHeight()));
    }

    public void render(Graphics g) { }

    public void onMousePressed(MouseEvent e) { }

    public void onMouseMoved(Graphics g) { }

    public void addSlot(Slot slot) {
        slots.add(slot);
    }

    /**
     * can open this screen handler
     *
     * @return can use
     */
    public abstract boolean canUse();

    /**
     * return this screen handler's texture
     *
     * @return identifier
     */
    public abstract ResourceLocation getTexture();
}
