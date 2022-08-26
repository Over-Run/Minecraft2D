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
    public final Cube headR, headL;
    public final Cube bodyR, bodyL;
    // The right1/left1 face of the right2/left2 arm/leg -> arm/legR1/L1R2/L2
    public final Cube armRR, armLR;
    public final Cube legRR, legLR;
    public final Cube armRL, armLL;
    public final Cube legRL, legLL;
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
        //todo
        final float w = 40;
        final float h = 32;
        headR = new Cube(new Polygon(v00.remap(0, 0), v01.remap(0, 8 / h), v02.remap(8 / w, 8 / h), v03.remap(8 / w, 0)));
        headL = new Cube(new Polygon(v03.remap(24 / w, 0), v02.remap(24 / w, 8 / h), v01.remap(16 / w, 8 / h), v00.remap(16 / w, 0)));
        bodyR = new Cube(new Polygon(v04.remap(0, 8 / h), v05.remap(0, 20 / h), v06.remap(4 / w, 20 / h), v07.remap(4 / w, 8 / h)));
        bodyL = new Cube(new Polygon(v07.remap(12 / w, 8 / h), v06.remap(12 / w, 20 / h), v05.remap(8 / w, 20 / h), v04.remap(8 / w, 8 / h)));
        armRR = new Cube(new Polygon(v08.remap(16 / w, 8 / h), v09.remap(16 / w, 20 / h), v10.remap(20 / w, 20 / h), v11.remap(20 / w, 8 / h))).setPosition(0, 1.5, 0);
        armLR = new Cube(new Polygon(v11.remap(28 / w, 8 / h), v10.remap(28 / w, 20 / h), v09.remap(24 / w, 20 / h), v08.remap(24 / w, 8 / h))).setPosition(0, 1.5, 0);
        legRR = new Cube(new Polygon(v08.remap(0, 20 / h), v09.remap(0, 32 / h), v10.remap(4 / w, 32 / h), v11.remap(4 / w, 20 / h))).setPosition(0, 0.75, 0);
        legLR = new Cube(new Polygon(v11.remap(12 / w, 20 / h), v10.remap(12 / w, 32 / h), v09.remap(8 / w, 32 / h), v08.remap(8 / w, 20 / h))).setPosition(0, 0.75, 0);
        armRL = new Cube(new Polygon(v08.remap(32 / w, 8 / h), v09.remap(32 / w, 20 / h), v10.remap(36 / w, 20 / h), v11.remap(36 / w, 8 / h))).setPosition(0, 1.5, 0);
        armLL = new Cube(new Polygon(v11.remap(36 / w, 20 / h), v10.remap(36 / w, 32 / h), v09.remap(32 / w, 32 / h), v08.remap(32 / w, 20 / h))).setPosition(0, 1.5, 0);
        legRL = new Cube(new Polygon(v08.remap(16 / w, 20 / h), v09.remap(16 / w, 32 / h), v10.remap(20 / w, 32 / h), v11.remap(20 / w, 20 / h))).setPosition(0, 0.75, 0);
        legLL = new Cube(new Polygon(v11.remap(28 / w, 20 / h), v10.remap(28 / w, 32 / h), v09.remap(24 / w, 32 / h), v08.remap(24 / w, 20 / h))).setPosition(0, 0.75, 0);
    }

    public void render(float delta, boolean facingRight, int animation) {
        double rot = Math.lerp(prevRot, Math.sin(animation * 0.5) * 35, delta);
        if (facingRight) {
            headR.render();
            armLR.setRotateZDeg(rot);
            armLR.render();
            legLR.setRotateZDeg(-rot);
            legLR.render();
            bodyR.render();
            armRR.setRotateZDeg(-rot);
            armRR.render();
            legRR.setRotateZDeg(rot);
            legRR.render();
        } else {
            headL.render();
            armLL.setRotateZDeg(rot);
            armLL.render();
            legLL.setRotateZDeg(-rot);
            legLL.render();
            bodyL.render();
            armRL.setRotateZDeg(-rot);
            armRL.render();
            legRL.setRotateZDeg(rot);
            legRL.render();
        }
        prevRot = rot;
    }

    @Override
    public void close() {
        headR.close();
        headL.close();
        bodyR.close();
        bodyL.close();
        armRR.close();
        armLR.close();
        legRR.close();
        legLR.close();
        armRL.close();
        armLL.close();
        legRL.close();
        legLL.close();
    }
}
