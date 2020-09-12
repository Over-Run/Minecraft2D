package io.github.overrun.mc2d.util;

import io.github.overrun.mc2d.Mc2D;

import java.awt.Graphics;

/**
 * @author squid233
 */
public class Highlight {
    public static void block(Graphics g, int prevX, int prevY) {
        try {
            if (Mc2D.getClient().getMousePosition() != null
                    && Mc2D.getClient().getMousePosition().x >= prevX
                    && Mc2D.getClient().getMousePosition().x <= prevX + 15
                    && Mc2D.getClient().getMousePosition().y >= prevY
                    && Mc2D.getClient().getMousePosition().y <= prevY + 15
            ) {
                g.drawRect(prevX, prevY, 15, 15);
            }
        } catch (NullPointerException ignored) { }
    }
}
