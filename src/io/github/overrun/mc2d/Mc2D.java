package io.github.overrun.mc2d;

import io.github.overrun.mc2d.client.Client;

/**
 * @author squid233
 */
public class Mc2D {
    public static final String VERSION = "200912-pa-build.0";
    public static final String NAMESPACE = "minecraft2d";

    private static final Client CLIENT = new Client();

    public static Client getClient() {
        return CLIENT;
    }
}
