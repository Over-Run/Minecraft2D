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

package io.github.overrun.mc2d.client;

import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.client.gui.screen.TitleScreen;
import io.github.overrun.mc2d.client.renderer.GameRenderer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.github.overrun.mc2d.Minecraft2D.VERSION;
import static io.github.overrun.mc2d.util.Utils.compute;

/**
 * @author squid233
 * @since 2020/09/14
 */
public final class Mc2dClient extends JPanel {
    private GameRenderer renderer;
    private Screen screen = new TitleScreen();

    private static final class Holder {
        private static final Mc2dClient INSTANCE = new Mc2dClient();
    }

    public static final Point NP = new Point();

    private Mc2dClient() {}

    public void openScreen(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public void initRenderer(GameRenderer renderer) {
        if (this.renderer == null) {
            this.renderer = renderer;
        }
    }

    public GameRenderer getRenderer() {
        return renderer;
    }

    public JFrame getFrame() {
        return (JFrame) super.getParent().getParent().getParent().getParent();
    }

    @Override
    public void paint(Graphics g) {
        Image buf = createImage(getWidth(), getHeight());
        Graphics gg = buf.getGraphics();
        screen.render(gg);
        getFrame().setTitle(String.format(System.getProperty("mc2d.title", "Minecraft2D %1s"), VERSION, LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss")), renderer.getFps()));
        g.drawImage(buf, 0, 0, null);
    }

    @Override
    public Point getMousePosition() throws HeadlessException {
        return compute(super.getMousePosition(), NP);
    }

    public static Mc2dClient getInstance() {
        return Holder.INSTANCE;
    }
}
