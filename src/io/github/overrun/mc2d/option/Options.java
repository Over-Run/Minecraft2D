package io.github.overrun.mc2d.option;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author squid233
 */
public class Options {
    public static final Properties OPTIONS = new Properties(1);

    public static final String POS_GRID_OPT = "pos_grid";
    public static final String FPS_OPT = "fps";

    static {
        try {
            OPTIONS.load(new FileReader("options.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return OPTIONS.getProperty(key);
    }

    public static int getI(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getB(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
