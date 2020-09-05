package io.github.scopetech.mc2d.engine;

import io.github.scopetech.mc2d.Mc2D;
import io.github.scopetech.mc2d.api.util.UtilSystem;

/**
 * @author squid233
 */
public class RenderThread implements Runnable {
    private Thread thread;
    private boolean exited = false;
    private final int interval;

    public RenderThread() {
        interval = 1000 / UtilSystem.getPropertyInt("fps", 30);
        System.out.println("[RenderThread]Created");
        System.out.println("[RenderThread]Render interval: " + interval + "ms");
    }

    @Override
    public void run() {
        System.out.println(thread.getName() + "Start rendering");
        while (!exited) {
            Mc2D.getClient().repaint();
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
