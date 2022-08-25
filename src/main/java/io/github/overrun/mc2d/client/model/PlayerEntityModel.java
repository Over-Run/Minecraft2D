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

import org.joml.Math;

/**
 * The player entity model.
 *
 * @author squid233
 * @since 0.6.0
 */
public class PlayerEntityModel extends EntityModel implements AutoCloseable {
    public final Vertices headR, headL;
    public final Vertices bodyR, bodyL;
    public final Vertices armR, armL;
    public final Vertices legR, legL;
    private double prevRot = 0;

    public PlayerEntityModel() {
        var v00 = new Vertex(-0.25f, 2.0f, 0);
        var v01 = new Vertex(-0.25f, 1.5f, 0);
        var v02 = new Vertex(0.25f, 1.5f, 0);
        var v03 = new Vertex(0.25f, 2.0f, 0);
        var v04 = new Vertex(-0.125f, 1.5f, 0);
        var v05 = new Vertex(-0.125f, 0.75f, 0);
        var v06 = new Vertex(0.125f, 0.75f, 0);
        var v07 = new Vertex(0.125f, 1.5f, 0);
        var v08 = new Vertex(-0.125f, 0, 0);
        var v09 = new Vertex(-0.125f, -0.75f, 0);
        var v10 = new Vertex(0.125f, -0.75f, 0);
        var v11 = new Vertex(0.125f, 0, 0);
        final float w = 24;
        final float h = 20;
        headR = new Vertices(v00.remap(0, 0), v01.remap(0, 8 / h), v02.remap(8 / w, 8 / h), v03.remap(8 / w, 0));
        headL = new Vertices(v03.remap(24 / w, 0), v02.remap(24 / w, 8 / h), v01.remap(16 / w, 8 / h), v00.remap(16 / w, 0));
        bodyR = new Vertices(v04.remap(8 / w, 8 / h), v05.remap(8 / w, 20 / h), v06.remap(12 / w, 20 / h), v07.remap(12 / w, 8 / h));
        bodyL = new Vertices(v07.remap(16 / w, 8 / h), v06.remap(16 / w, 20 / h), v05.remap(12 / w, 20 / h), v04.remap(12 / w, 8 / h));
        armR = new Vertices(v08.remap(16 / w, 8 / h), v09.remap(16 / w, 20 / h), v10.remap(20 / w, 20 / h), v11.remap(20 / w, 8 / h)).setPosition(0, 1.5, 0);
        armL = new Vertices(v11.remap(24 / w, 8 / h), v10.remap(24 / w, 20 / h), v09.remap(20 / w, 20 / h), v08.remap(20 / w, 8 / h)).setPosition(0, 1.5, 0);
        legR = new Vertices(v08.remap(0, 8 / h), v09.remap(0, 20 / h), v10.remap(4 / w, 20 / h), v11.remap(4 / w, 8 / h)).setPosition(0, 0.75, 0);
        legL = new Vertices(v11.remap(8 / w, 8 / h), v10.remap(8 / w, 20 / h), v09.remap(4 / w, 20 / h), v08.remap(4 / w, 8 / h)).setPosition(0, 0.75, 0);
    }

    private void renderL(double rot) {
        headL.render();
        bodyL.render();
        armL.setRotateZDeg(-rot).render();
        legL.setRotateZDeg(rot).render();
    }

    public void render(float delta, boolean facingRight, int animation) {
        double rot = Math.lerp(prevRot, Math.sin(animation * 0.5) * 35, delta);
        if (facingRight) {
            renderL(rot);
        }
        headR.render();
        bodyR.render();
        armR.setRotateZDeg(rot).render();
        legR.setRotateZDeg(-rot).render();
        if (!facingRight) {
            renderL(rot);
        }
        prevRot = rot;
    }

    @Override
    public void close() {
        headR.close();
        headL.close();
        bodyR.close();
        bodyL.close();
        armR.close();
        armL.close();
        legR.close();
        legL.close();
    }
}
