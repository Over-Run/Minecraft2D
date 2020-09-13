package io.github.overrun.mc2d.input;

import io.github.overrun.mc2d.Mc2D;
import io.github.overrun.mc2d.world.Dimensions;

import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author squid233
 */
public class Input extends KeyAdapter {
    public static final char ESCAPE = '\u001b';

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == ESCAPE) {
            int opt = JOptionPane.showConfirmDialog(Mc2D.getClient(), "Are you sure want to exit?", "Are you sure?", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                int save = JOptionPane.showConfirmDialog(Mc2D.getClient(), "Are you sure want to save?", "Are you sure?", JOptionPane.YES_NO_OPTION);
                if (save == JOptionPane.YES_OPTION){
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/save.dat"))) {
                        oos.writeObject(Dimensions.OVERWORLD.getStorageBlocks());
                    } catch (IOException ee) {
                        ee.printStackTrace();
                    }
                }
                System.exit(0);
            }
        }
    }
}
