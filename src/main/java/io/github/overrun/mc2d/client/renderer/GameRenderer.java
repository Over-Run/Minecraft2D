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

package io.github.overrun.mc2d.client.renderer;

import io.github.overrun.mc2d.option.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JFrame;

/**
 * @author squid233
 * @since 2020/09/15
 */
public final class GameRenderer implements Runnable {
    private static final Logger logger = LogManager.getLogger("Minecraft2D|GameRenderer");
    private final JFrame gameFrame;
    private Thread thread;
    private boolean exited;
    private int fps;
    public static final int INTERVAL = 1000 / Options.getI(Options.FPS, Options.DEF_FPS);

    public GameRenderer(JFrame gameFrame) {
        this.gameFrame = gameFrame;
        logger.info("Created render thread");
    }

    @Override
    public void run() {
        logger.info("Start rendering");
        // The render time
        long update;
        // The sleep time
        long sleep;
        while (!exited) {
            long before = System.nanoTime();
            gameFrame.repaint();
            update = (System.nanoTime() - before) / 1000000L;
            sleep = Math.max(2, INTERVAL - update);
            try {
                //noinspection BusyWait
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                logger.error("{} error: {}", thread.getName(), e);
                exited = true;
            }
            fps = (int) (1000 / (update + sleep));
        }
        logger.info("Stop rendering");
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, "RenderThread");
            thread.start();
        }
    }

    public void stop() {
        exited = true;
    }

    public int getFps() {
        return fps;
    }
}
