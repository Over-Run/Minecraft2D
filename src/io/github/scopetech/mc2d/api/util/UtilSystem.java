package io.github.scopetech.mc2d.api.util;

/**
 * @author squid233
 */
@SuppressWarnings({"unused", "RedundantSuppression"})
public class UtilSystem {
    public static int getPropertyInt(String key, int def) {
        return Integer.parseInt(System.getProperty(key, String.valueOf(def)));
    }

    public static int getPropertyInt(String key) {
        return getPropertyInt(key, 0);
    }
}
