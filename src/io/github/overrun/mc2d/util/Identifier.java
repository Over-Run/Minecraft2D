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
        this.namespace = StringHelper.isEmpty(id[0]) ? Minecraft2D.NAMESPACE : id[0];
        this.path = id[1];
        if (!isNamespaceValid(this.namespace)) {
            throw new InvalidIdentifierException("Non [a-z0-9_.-] character in namespace of location: " + this.namespace + ':' + this.path);
        } else if (!isPathValid(this.path)) {
            throw new InvalidIdentifierException("Non [a-z0-9/._-] character in path of location: " + this.namespace + ':' + this.path);
        }
    }

    public Identifier(String namespace, String path) {
        this(new String[]{namespace, path});
    }

    public Identifier(String id) {
        this(id.split(":"));
    }

    private static boolean isNamespaceValid(String namespace) {
        for (int i = 0; i < namespace.length(); ++i) {
            if (!isNamespaceCharacterValid(namespace.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNamespaceCharacterValid(char c) {
        return c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '.';
    }

    private static boolean isPathValid(String path) {
        for (int i = 0; i < path.length(); ++i) {
            if (!isPathCharacterValid(path.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPathCharacterValid(char c) {
        return c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '/' || c == '.';
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Identifier && ((Identifier) o).getNamespace().equals(getNamespace()) && ((Identifier) o).getPath().equals(getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNamespace(), getPath());
    }

    @Override
    public String toString() {
        return getNamespace() + ':' + getPath();
    }
}
