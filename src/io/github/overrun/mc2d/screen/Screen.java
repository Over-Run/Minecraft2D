package io.github.overrun.mc2d.screen;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author squid233
 */
public interface Screen {
    /**
     * draw background
     *
     * @param g graphics
     * @param color color
     */
    default void drawBackground(Graphics g, Color color) {
        Color c = g.getColor();
        g.setColor(color);
        g.fillRect(0, 0, width(), height());
        g.setColor(c);
    }

    /**
     * return width
     *
     * @return width
     */
    int width();

    /**
     * return height
     *
     * @return height
     */
    int height();
}
