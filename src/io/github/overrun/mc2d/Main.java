package io.github.overrun.mc2d;

import io.github.overrun.mc2d.util.JavaVersion;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author squid233
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(System.getProperty(JavaVersion.JAVA_CLASS_VERSION).split("\\.")[0]) < JavaVersion.Class.V_11.getVer()) {
            JOptionPane.showMessageDialog(null, "Your computer is using Java 10 or lower. Please install Java 11 or above.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        Mc2D.getClient();
    }
}
