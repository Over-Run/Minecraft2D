package io.github.overrun.mc2d.util;

import io.github.overrun.mc2d.Mc2D;

/**
 * @author squid233
 */
public class Identifier {
    private final String namespace;
    private final String path;

    public Identifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public Identifier(String path) {
        this(Mc2D.NAMESPACE, path);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(namespace.hashCode() + "" + path.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Identifier && ((Identifier) obj).getNamespace().equals(namespace) && ((Identifier) obj).getPath().equals(path);
    }

    @Override
    public String toString() {
        return namespace + ':' + path;
    }
}
