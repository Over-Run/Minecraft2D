package io.github.overrun.mc2d.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author squid233
 */
public class Input extends KeyAdapter {
    public static final char ESCAPE = '\u001b';

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == ESCAPE) {
            System.exit(0);
        }
    }
}
