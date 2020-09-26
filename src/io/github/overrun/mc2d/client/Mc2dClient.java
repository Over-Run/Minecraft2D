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
import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.game.Player;
import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.input.KeyAdapter;
import io.github.overrun.mc2d.input.MouseAdapter;
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.screen.Screen;
import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.util.Colors;
import io.github.overrun.mc2d.util.factory.Mc2dFactories;
import io.github.overrun.mc2d.world.Worlds;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Point;

/**
 * @author squid233
 * @since 2020/09/14
 */
public class Mc2dClient extends JFrame implements Screen {
    private static Mc2dClient instance;
    public static final Point NULL_POINT = new Point();
    private static boolean initialized = false;

    private Mc2dClient() {
        super("Minecraft 2D " + Minecraft2D.VERSION + " [Esc:Quit Game] - Made by OverRun Organization");
        if (!initialized) {
            initialized = true;
            setSize(Options.getI(Options.WIDTH, 1296), Options.getI(Options.HEIGHT, 486));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setIconImage(new ImageIcon("icon.png").getImage());
            addKeyListener(new KeyAdapter());
            var mouse = new MouseAdapter();
            addMouseListener(mouse);
            addMouseWheelListener(mouse);
            Minecraft2D.LOGGER.debug("Max memory: " + ((Runtime.getRuntime().maxMemory() >> 10 >> 10) >= 1024
                    ? (Runtime.getRuntime().maxMemory() >> 10 >> 10 >> 10) + "GB"
                    : (Runtime.getRuntime().maxMemory() >> 10 >> 10) + "MB"));
            new RenderThread(this).start();
            setVisible(true);
        }
    }

    public static Mc2dClient getInstance() {
        if (instance == null) {
            instance = new Mc2dClient();
        }
        return instance;
    }

    @Override
    public void paint(Graphics g) {
        var buf = createImage(getWidth(), getHeight());
        var gg = buf.getGraphics();
        /////
        drawBackground(gg, Colors.decode(Colors.skyBlue));
        /////
        Worlds.overworld.getStorageBlock().draw(gg);
        /////
        drawImage(gg, Images.getBlockTexture(Blocks.getByRawId(Player.handledBlock)), getWidth() - 49, 1, 32, 32);
        LiteralText handledBlockId = Mc2dFactories.getText().getLiteralText(Blocks.getByRawId(Player.handledBlock).getRegistryName().toString());
        drawText(gg, getWidth() - 16 - handledBlockId.getDisplayLength(), 34, handledBlockId);
        /////
        g.drawImage(buf, 0, 0, null);
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
