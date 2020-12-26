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
import io.github.overrun.mc2d.client.gui.screen.widget.Element;
import io.github.overrun.mc2d.client.gui.screen.widget.EntryListWidget;
import io.github.overrun.mc2d.client.util.BuiltinGraphics;
import io.github.overrun.mc2d.event.ButtonClickCallback;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.util.Images;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static io.github.overrun.mc2d.Minecraft2D.*;
import static io.github.overrun.mc2d.util.DrawHelper.BG_COLOR;
import static java.awt.event.KeyEvent.VK_ESCAPE;

/**
 * @author squid233
 * @since 2020/10/16
 */
public abstract class Screen {
    protected final List<Element> children = new ArrayList<>(6);
    protected final List<AbstractButtonWidget> buttons = new ArrayList<>(6);
    private final IText title;
    private Screen parent;

    protected Screen(IText title, Screen parent) {
        this(title);
        this.parent = parent;
    }

    protected Screen(IText title) {
        this.title = title;
        init();
    }

    protected void init() {}

    public void render(BuiltinGraphics g) {
        drawChildren(g);
        g.drawCenteredText(title, 5);
        for (Element e : children) {
            if (e instanceof AbstractButtonWidget) {
                ((AbstractButtonWidget) e).renderTooltips(g);
            }
        }
    }

    private void drawChildren(BuiltinGraphics g) {
        for (Element e : children) {
            e.render(g);
        }
    }

    public void onKeyDown(KeyEvent e) {
        if (e.getKeyChar() == VK_ESCAPE) {
            close();
        }
    }

    public Element addChild(Element child) {
        children.add(child);
        return child;
    }

    public AbstractButtonWidget addButton(AbstractButtonWidget button) {
        buttons.add(button);
        return (AbstractButtonWidget) addChild(button);
    }

    public static void drawFadeBackground(BuiltinGraphics g) {
        g.drawWithColor(BG_COLOR, gg -> gg.fillRect(0, 0, getWidth(), getHeight()));
    }

    public static void drawDirtBackground(BuiltinGraphics g) {
        for (int i = 0, h = getHeight(); i < h; i += 16) {
            for (int j = 0, w = getWidth(); j < w; j += 16) {
                g.drawImage(Images.OPTIONS_BACKGROUND, j, i);
            }
        }
    }

    public static void renderBackground(BuiltinGraphics g) {
        drawDirtBackground(g);
        drawFadeBackground(g);
    }

    /**
     * Close a screen.<br>
     * Override {@link Screen#getParent()} can implement.
     */
    protected void close() {
        if (getParent() != null) {
            openScreen(getParent());
        }
    }

    protected void open(Screen screen) {
        openScreen(screen);
    }

    protected Screen getParent() {
        return parent;
    }

    public final void onMouseClicked() {
        for (Element e : children) {
            if (e instanceof AbstractButtonWidget) {
                AbstractButtonWidget b = (AbstractButtonWidget) e;
                if (b.isEnable() && b.isHover()) {
                    b.getAction().onPress(b);
                    ButtonClickCallback.post(b);
                    break;
                }
            } else if (e instanceof EntryListWidget) {
                // ((EntryListWidget) e).onMouseClick();
            }
        }
    }
}
