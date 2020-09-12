package io.github.overrun.mc2d.api.util;

import java.util.function.Supplier;

/**
 * @author squid233
 */
public class UtilSystem {
    public static int getPropertyInt(String key, int def, Supplier<?> supplier) {
        if (System.getProperty(key) == null && supplier != null) {
            supplier.get();
        }
        return Integer.parseInt(System.getProperty(key, String.valueOf(def)));
    }

    public static int getPropertyInt(String key, int def) {
        return getPropertyInt(key, def, null);
    }

    public static int getPropertyInt(String key) {
        return getPropertyInt(key, 0);
    }
}
