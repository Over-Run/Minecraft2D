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

import io.github.overrun.mc2d.Minecraft2D;
import io.github.overrun.mc2d.input.KeyInput;
import io.github.overrun.mc2d.input.MouseInput;
import io.github.overrun.mc2d.option.Options;

import javax.swing.*;
import java.awt.*;

import static io.github.overrun.mc2d.Minecraft2D.LOGGER;
import static io.github.overrun.mc2d.screen.Screens.getOpenScreen;

/**
 * @author squid233
 * @since 2020/09/14
 */
public final class Mc2dClient extends JFrame {
    private static final long serialVersionUID = 1L;

    public static final Point NP = new Point();

    private static Mc2dClient instance;

    private Mc2dClient() {
        super("Minecraft2D " + Minecraft2D.VERSION);
        setSize(Options.getI(Options.WIDTH, 854), Options.getI(Options.HEIGHT, 480));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addKeyListener(new KeyInput());
        addMouseListener(new MouseInput());
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("icon.png")).getImage());
        LOGGER.info("Max memory: {}", Runtime.getRuntime().maxMemory() >> 20 >= 1024
                ? (Runtime.getRuntime().maxMemory() >> 30) + "GB"
                : (Runtime.getRuntime().maxMemory() >> 20) + "MB");
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
        final Point p = super.getMousePosition();
        return p == null ? NP : p;
    }

    public static Mc2dClient getInstance() {
        if (instance == null) {
            instance = new Mc2dClient();
        }
        return instance;
    }
}
