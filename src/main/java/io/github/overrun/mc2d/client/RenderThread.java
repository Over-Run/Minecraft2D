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
import io.github.overrun.mc2d.option.Options;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class RenderThread implements Runnable {
    private Thread thread;
    private boolean exited = false;
    private final int interval;
    private final Mc2dClient client;
    public int fps;

    public RenderThread(Mc2dClient client) {
        this.client = client;
        fps = Options.getI(Options.FPS, 30);
        interval = 1000 / fps;
        Minecraft2D.LOGGER.debug("Created render thread");
        Minecraft2D.LOGGER.info("FPS: {}", fps);
        Minecraft2D.LOGGER.debug("Render interval: {}ms", interval);
    }

    @Override
    public void run() {
        Minecraft2D.LOGGER.info("Start rendering");
        while (!exited) {
            client.repaint();
            try {
                //noinspection BusyWait
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Minecraft2D.LOGGER.error(thread.getName() + " {} {}", "Error:", e.toString());
                exited = true;
            }
        }
        Minecraft2D.LOGGER.info(thread.getName() + " {}", "Stop rendering");
        System.exit(0);
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, "RenderThread");
            thread.start();
        }
    }
}
