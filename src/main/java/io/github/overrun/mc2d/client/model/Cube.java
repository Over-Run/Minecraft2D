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

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 0.6.0
 */
public class Cube implements AutoCloseable {
    private final Polygon[] polygons;
    private boolean compiled = false;
    private int list;
    private double rotateZ;
    private double x, y, z;

    public Cube(Polygon... polygons) {
        this.polygons = polygons;
    }

    private void compile() {
        list = glGenLists(1);
        glNewList(list, GL_COMPILE);
        glBegin(GL_QUADS);
        for (var p : polygons) {
            p.render();
        }
        glEnd();
        glEndList();
    }

    public void render() {
        if (!compiled) {
            compile();
            compiled = true;
        }
        glPushMatrix();
        glTranslated(x, y, z);
        glRotated(rotateZ, 0, 0, 1);
        glCallList(list);
        glPopMatrix();
    }

    public Cube setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Cube setRotateZ(double ang) {
        return setRotateZDeg(Math.toDegrees(ang));
    }

    public Cube setRotateZDeg(double ang) {
        rotateZ = ang;
        return this;
    }

    @Override
    public void close() {
        if (list != 0 && glIsList(list)) {
            glDeleteLists(list, 1);
        }
    }
}
