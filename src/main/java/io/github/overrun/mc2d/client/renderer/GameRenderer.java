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

import javax.swing.JFrame;

import static io.github.overrun.mc2d.Minecraft2D.LOGGER;

/**
 * @author squid233
 * @since 2020/09/15
 */
public final class GameRenderer implements Runnable {
    private final JFrame gameFrame;
    private Thread thread;
    private boolean exited;
    public int fps;

    public GameRenderer(JFrame gameFrame) {
        this.gameFrame = gameFrame;
        fps = Options.getI(Options.FPS, Options.DEF_FPS);
        LOGGER.info("Created render thread");
        LOGGER.info("FPS: {}", fps);
        LOGGER.info("Render interval: {}ms", getInterval());
    }

    @Override
    public void run() {
        LOGGER.info("Start rendering");
        while (!exited) {
            gameFrame.repaint();
            try {
                //noinspection BusyWait
                Thread.sleep(getInterval());
            } catch (InterruptedException e) {
                LOGGER.error("{} error: {}", thread.getName(), e);
                exited = true;
            }
        }
        LOGGER.info("{} stop rendering", thread.getName());
    }

    public int getInterval() {
        return 1000 / fps;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, "RenderThread");
            thread.start();
        }
    }
}
