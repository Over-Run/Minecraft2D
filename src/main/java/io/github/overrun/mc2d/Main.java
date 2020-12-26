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

package io.github.overrun.mc2d;

import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.renderer.GameRenderer;
import io.github.overrun.mc2d.input.KeyInput;
import io.github.overrun.mc2d.input.MouseInput;
import io.github.overrun.mc2d.lang.Language;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Dimension;

import static io.github.overrun.mc2d.Minecraft2D.VERSION;
import static io.github.overrun.mc2d.Minecraft2D.logger;
import static io.github.overrun.mc2d.option.Options.*;
import static io.github.overrun.mc2d.util.ImgUtil.readImage;

/**
 * @author squid233
 * @since 2020/09/14
 */
public final class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        logger.info("Loading for game Minecraft2D {}", VERSION);
        Language.load("mc2d");
        init();
    }

    private static void init() throws ClassNotFoundException {
        JFrame jf = initFrame();
        final long mem = Runtime.getRuntime().maxMemory() >> 20;
        logger.info("Max memory: {}", mem >= 1024
                ? (mem >> 10) + " GB"
                : mem + " MB");
        initReg();
        GameRenderer renderer = new GameRenderer(jf);
        Mc2dClient.getInstance().initRenderer(renderer);
        renderer.start();
        jf.setVisible(true);
    }

    private static JFrame initFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame jf = new JFrame("Minecraft2D " + Minecraft2D.VERSION);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setIconImage(readImage("icon.png"));
        Mc2dClient.getInstance().setPreferredSize(new Dimension(getI(WIDTH, DEF_WIDTH), getI(HEIGHT, DEF_HEIGHT)));
        jf.getContentPane().add(Mc2dClient.getInstance());
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.addKeyListener(new KeyInput());
        jf.addMouseListener(new MouseInput());
        return jf;
    }

    private static void initReg() throws ClassNotFoundException {
        Class.forName(Blocks.class.getName());
    }
}
