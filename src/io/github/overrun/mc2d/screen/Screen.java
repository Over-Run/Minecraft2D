package io.github.overrun.mc2d.screen;

import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.text.IText;

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
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(c);
    }

    /**
     * draw text to screen
     *
     * @param g graphics
     * @param x x
     * @param y y
     * @param text IText
     */
    default void drawText(Graphics g, int x, int y, IText text) {
        char[] cs = text.asString().toCharArray();
        for (int i = 0; i < text.asString().length(); i++) {
            g.drawImage(Images.getAsciiInMap(cs[i]), x + i * 7, y, null);
        }
    }

    /**
     * return width
     *
     * @return width
     */
    int getWidth();

    /**
     * return height
     *
     * @return height
     */
    int getHeight();
}
