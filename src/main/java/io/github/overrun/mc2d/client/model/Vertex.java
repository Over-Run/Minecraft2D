/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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

package io.github.overrun.mc2d.client.model;

/**
 * @author squid233
 * @since 0.6.0
 */
public class Vertex {
    private final float x, y, z;
    private final float u, v;

    public Vertex(float x, float y, float z, float u, float v) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
        this.v = v;
    }

    public Vertex(float x, float y, float z) {
        this(x, y, z, 0f, 0f);
    }

    public Vertex(Vertex vertex) {
        x = vertex.x;
        y = vertex.y;
        z = vertex.z;
        u = vertex.u;
        v = vertex.v;
    }

    public Vertex remap(float u, float v) {
        return new Vertex(x(), y(), z(), u, v);
    }

    public Vertex position(float x, float y, float z) {
        return new Vertex(x, y, z, u(), v());
    }

    public Vertex x(float x) {
        return new Vertex(x, y(), z());
    }

    public Vertex y(float y) {
        return new Vertex(x(), y, z());
    }

    public Vertex z(float z) {
        return new Vertex(x(), y(), z);
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }

    public float u() {
        return u;
    }

    public float v() {
        return v;
    }
}
