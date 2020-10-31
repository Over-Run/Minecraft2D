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
import io.github.overrun.mc2d.input.MultiAdapter;
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.screen.ScreenUtil;
import io.github.overrun.mc2d.screen.Screens;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;

/**
 * @author squid233
 * @since 2020/09/14
 */
public class Mc2dClient extends JFrame {
    private static Mc2dClient instance;
    public static final Point NULL_POINT = new Point();

    private Mc2dClient() {
        super("Minecraft 2D " + Minecraft2D.VERSION + " - Made by OverRun Organization");
        setSize(Options.getI(Options.WIDTH, 1040), Options.getI(Options.HEIGHT, 486));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("icon.png")).getImage());
        MultiAdapter ma = new MultiAdapter();
        addKeyListener(ma);
        addMouseListener(ma);
        addMouseWheelListener(ma);
        Minecraft2D.LOGGER.info("Max memory: {}", Runtime.getRuntime().maxMemory() >> 20 >= 1024
                ? (Runtime.getRuntime().maxMemory() >> 30) + "GB"
                : (Runtime.getRuntime().maxMemory() >> 20) + "MB");
        new RenderThread(this).start();
    }

    public static synchronized Mc2dClient getInstance() {
        if (instance == null) {
            instance = new Mc2dClient();
        }
        return instance;
    }

    @Override
    public void paint(Graphics g) {
        Image buf = createImage(getWidth(), getHeight());
        Graphics gg = buf.getGraphics();
        /////
        /*drawBackground(gg, Colors.decode(Colors.skyBlue));
        /////
        Worlds.overworld.getFrontStorageBlock().draw(gg);
        /////
        ScreenUtil.drawImage(gg, Images.getBlockTexture(Registry.BLOCK.get(Player.handledBlock)), getWidth() - 49, 1, 32, 32);
        LiteralText handledBlockId = LiteralText.of(Registry.BLOCK.get(Player.handledBlock).getRegistryName().toString());
        ScreenUtil.drawText(gg, 0, 15, handledBlockId, Color.WHITE);
        /////*/
        Screens.getOpening().render(gg);
        Screens.getOpening().onMouseMoved(gg);
        /////
        g.drawImage(buf, 0, 0, null);
    }

    private void drawBackground(Graphics g, Color color) {
        ScreenUtil.drawRect(g, 0, 0, getWidth(), getHeight(), color);
    }

    @Override
    public Point getMousePosition() throws HeadlessException {
        Point p = super.getMousePosition();
        if (p != null) {
            return p;
        }
        return NULL_POINT;
    }
}
