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
import io.github.overrun.mc2d.world.entity.player.PlayerEntity;
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
    private static final int KEEP_FACING_TICKS = 20;
    private boolean facingRight;
    private int animation = 0;
    private boolean keepFacing = false;
    private int currentFacingTicks = 0;

    public HumanEntity(World world, EntityType<? extends HumanEntity> entityType) {
        super(world, entityType);
        teleport(Math.random() * world.width, 70, 1.5);
    }

    @Override
    public void tick() {
        super.tick();
        animation++;
        double xa = Math.random() * 2 - 1;
        if (keepFacing) {
            xa = facingRight ? 1.0 : -1.0;
            currentFacingTicks++;
            if (currentFacingTicks >= KEEP_FACING_TICKS) keepFacing = false;
        } else {
            currentFacingTicks = 0;
            xa = xa > 0.0 ? 1.0 : -1.0;
            facingRight = xa >= 0;
            keepFacing = true;
        }
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
        model.render(delta, facingRight, animation);
        glPopMatrix();
    }
}
