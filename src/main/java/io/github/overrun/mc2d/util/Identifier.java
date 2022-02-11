/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Overrun Organization
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

import java.util.Objects;

/**
 * @author squid233
 * @since 2021/01/25
 */
public class Identifier {
    public static final String VANILLA = "mc2d";
    private static final long serialVersionUID = 1L;
    private final String namespace;
    private final String path;

    protected Identifier(String[] id) {
        if (id.length > 1) {
            namespace = id[0];
            path = id[1];
        } else {
            namespace = VANILLA;
            path = id[0];
        }
    }

    public Identifier(String namespace, String path) {
        this(new String[]{namespace, path});
    }

    public Identifier(String id) {
        this(id.split(":", 2));
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    public boolean isVanilla() {
        return VANILLA.equals(namespace);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Identifier that = (Identifier) o;
        return Objects.equals(getNamespace(), that.getNamespace()) && Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNamespace(), getPath());
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }
}
