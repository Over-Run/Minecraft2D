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

package io.github.overrun.mc2d.world.entity;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.model.PlayerEntityModel;
import io.github.overrun.mc2d.world.World;
import org.joml.Math;

import static org.lwjgl.opengl.GL11.*;

/**
 * The testing entity.
 *
 * @author squid233
 * @since 0.6.0
 */
public class HumanEntity extends Entity {
    public static final PlayerEntityModel model = new PlayerEntityModel();
    private boolean facingRight;

    public HumanEntity(World world) {
        super(world);
        setPosition(Math.random() * world.width, 70, 1.5);
    }

    @Override
    public void tick() {
        super.tick();
        double xa = Math.random() * 2 - 1;
        xa = xa > 0.0 ? 1.0 : (xa < 0.0 ? -1.0 : 0.0);
        facingRight = xa >= 0;
        if (onGround && Math.random() > 0.5) {
            velocity.y = 0.5;
        }
        moveRelative(xa, onGround ? 0.1f : 0.02f);
        velocity.y -= 0.08;
        move((float) velocity.x(), (float) velocity.y());
        velocity.mul(0.91, 0.98, 0.91);
        if (onGround) {
            velocity.mul(0.7, 1.0, 0.7);
        }
        if (position.y < -64) {
            remove();
        }
    }

    public void render(float delta) {
        var client = Mc2dClient.getInstance();
        client.getTextureManager().bindTexture(PlayerEntity.TEXTURE);
        glPushMatrix();
        glTranslated(Math.lerp(prevPos.x(), position.x(), delta),
            Math.lerp(prevPos.y(), position.y(), delta),
            Math.lerp(prevPos.z(), position.z(), delta));
        model.render(delta, facingRight, 0);
        glPopMatrix();
    }
}