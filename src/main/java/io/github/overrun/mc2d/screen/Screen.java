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
import io.github.overrun.mc2d.client.renderer.Renderable;
import io.github.overrun.mc2d.util.registry.Registry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.*;
import java.awt.event.KeyEvent;

import static io.github.overrun.mc2d.screen.Screens.openScreen;
import static io.github.overrun.mc2d.util.DrawHelper.drawDefaultBackground;
import static io.github.overrun.mc2d.util.DrawHelper.drawDirtBackground;
import static java.awt.event.KeyEvent.VK_ESCAPE;

/**
 * @author squid233
 * @since 2020/11/24
 */
public abstract class Screen implements Renderable {
    protected final ObjectList<ScreenWidget> widgets = new ObjectArrayList<>(5);
    protected final ObjectList<ButtonWidget> buttons = new ObjectArrayList<>(5);
    private final Screen parent;
    protected Mc2dClient client;

    protected Screen(Screen parent) {
        this.parent = parent;
        this.client = Mc2dClient.getInstance();
    }

    @Override
    public void render(Graphics g) {
        drawDirtBackground(g);
        drawDefaultBackground(g);
        for (ScreenWidget widget : widgets) {
            widget.render(g);
        }
    }

    public void onKeyDown(KeyEvent e) {
        if (e.getKeyCode() == VK_ESCAPE) {
            close();
        }
    }

    public void addWidget(ScreenWidget widget) {
        widgets.add(widget);
    }

    public void addButton(ButtonWidget widget) {
        buttons.add(widget);
        addWidget(widget);
    }

    protected void close() {
        if (parent != null) openScreen(parent);
    }

    public void open(Screen screen) {
        openScreen(screen);
    }

    public Mc2dClient getClient() {
        return client;
    }

    public void onMouseClicked() {
        for (int i = buttons.size() - 1; i >= 0; i--) {
            ButtonWidget b = buttons.get(i);
            if (b.isHover()) {
                b.getAction().onPress(b);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return Registry.SCREEN.getId(this).toString();
    }
}
