package io.github.scopetech.mc2d;

import io.github.scopetech.mc2d.client.Client;

/**
 * @author squid233
 */
public class Mc2D {
    public static final String VERSION = "200905-pa-build.0";
    public static final String NAMESPACE = "minecraft2d";

    private static final Client CLIENT = new Client();

    public static Client getClient() {
        return CLIENT;
    }
}
