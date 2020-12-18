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

import io.github.overrun.mc2d.option.Options;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;

import static io.github.overrun.mc2d.option.Options.*;
import static io.github.overrun.mc2d.screen.Screens.getOpenScreen;
import static io.github.overrun.mc2d.util.Utils.compute;

/**
 * @author squid233
 * @since 2020/09/14
 */
public final class Mc2dClient extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final Point NP = new Point();

    private static Mc2dClient instance;

    private Mc2dClient() {
        setSize(getI(Options.WIDTH, DEF_WIDTH) - 16, getI(Options.HEIGHT, DEF_HEIGHT) - 38);
    }

    @Override
    public void paint(Graphics g) {
        Image buf = createImage(getWidth(), getHeight());
        Graphics gg = buf.getGraphics();
        getOpenScreen().render(gg);
        g.drawImage(buf, 0, 0, null);
    }

    @Override
    public Point getMousePosition() throws HeadlessException {
        return compute(super.getMousePosition(), NP);
    }

    public JFrame getFrame() {
        return (JFrame) super.getParent().getParent().getParent().getParent();
    }

    public static Mc2dClient getInstance() {
        if (instance == null) {
            instance = new Mc2dClient();
        }
        return instance;
    }
}
