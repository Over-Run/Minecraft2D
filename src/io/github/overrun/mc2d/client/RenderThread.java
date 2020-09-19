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

/**
 * @author squid233
 * @date 2020/9/15
 */
public class RenderThread implements Runnable {
    private Thread thread;
    private boolean exited = false;
    private final int interval;
    public int fps;

    public RenderThread() {
        fps = Options.getI(Options.FPS_OPT);
        interval = 1000 / fps;
        System.out.println("[RenderThread]Created");
        System.out.println("[RenderThread]FPS: " + fps);
        System.out.println("[RenderThread]Render interval: " + interval + "ms");
    }

    @Override
    public void run() {
        System.out.println(thread.getName() + "Start rendering");
        while (!exited) {
            Mc2dClient.getInstance().repaint();
            try {
                //noinspection BusyWait
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                System.out.println(thread.getName() + "Error: " + e.toString());
                exited = true;
            }
        }
        System.out.println(thread.getName() + "Stop rendering");
        System.exit(0);
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, "[RenderThread]");
            thread.start();
        }
    }
}
