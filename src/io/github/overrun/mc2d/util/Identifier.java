/*
 * MIT License
 *
 * Copyright (c) 2020 Over-Run
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.overrun.mc2d.util;

import io.github.overrun.mc2d.Minecraft2D;

import java.util.Objects;

/**
 * @author squid233
 * @since 2020/09/14
 */
public class Identifier {
    private final String namespace;
    private final String path;

    public Identifier(String[] id) {
        if (id.length == 1) {
            this.namespace = Minecraft2D.NAMESPACE;
            this.path = id[0].toLowerCase();
        } else if (id.length == 0) {
            this.namespace = Minecraft2D.NAMESPACE;
            this.path = "air";
        } else {
            this.namespace = id[0].toLowerCase().replaceAll("\\W", "");
            this.path = id[1].toLowerCase();
        }
    }

    public Identifier(String namespace, String path) {
        this(new String[]{namespace, path});
    }

    public Identifier(String id) {
        this(id.split(":"));
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Identifier && ((Identifier) o).namespace.equals(namespace) && ((Identifier) o).path.equals(path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    @Override
    public String toString() {
        return namespace + ':' + path;
    }
}
