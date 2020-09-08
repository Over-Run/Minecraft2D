package io.github.overrun.mc2d;

import io.github.overrun.mc2d.client.Client;

/**
 * @author squid233
 */
public class Mc2D {
    public static final String VERSION = "200906-pa-build.0";
    public static final String NAMESPACE = "minecraft2d";

    private static Client client;

    public static Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }
}
