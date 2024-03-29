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

package io.github.overrun.mc2d.world.entity.player;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.model.PlayerEntityModel;
import io.github.overrun.mc2d.screen.inv.PlayerInventory;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.world.IWorldFixer;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.world.entity.Entity;
import io.github.overrun.mc2d.world.entity.EntityType;
import io.github.overrun.mc2d.world.ibt.IBinaryTag;
import io.github.overrun.mc2d.world.item.ItemStack;

import java.util.UUID;

import static io.github.overrun.mc2d.client.Keyboard.isKeyPress;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public class PlayerEntity extends Entity {
    public static final Identifier TEXTURE = new Identifier("textures/entity/player.png");
    private static final long PLAYER_VERSION = 2L;
    public static final IWorldFixer WORLD_FIXER = (name, value) -> {
        if ("version".equals(name)) {
            long v = (long) value.value();
            if (v != PLAYER_VERSION) {
                throw new RuntimeException("Doesn't compatible with version " + v + ". Current is " + PLAYER_VERSION);
            }
        }
        return value;
    };
    public static final PlayerEntityModel model = new PlayerEntityModel();
    public int hotBarNum = 0;
    public final PlayerInventory inventory = new PlayerInventory(this);
    public boolean facingRight = true;
    private int animation = 0;

    public PlayerEntity(World world, EntityType<? extends PlayerEntity> entityType) {
        super(world, entityType);
        teleport(Math.random() * world.width, world.height + 10, 1.5);
    }

    public static UUID generateUUID(String name) {
        // TODO: 2022/9/6 Generate UUID from username
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    public void setItemMainHand(ItemStack item) {
        inventory.setStack(Slot.HOT_BAR_ID0 + hotBarNum, item);
    }

    public ItemStack getItemMainHand() {
        return inventory.getStack(Slot.HOT_BAR_ID0 + hotBarNum);
    }

    public double processInput() {
        double xa = 0;
        boolean moveL = isKeyPress(GLFW_KEY_A)
                        || isKeyPress(GLFW_KEY_LEFT);
        boolean moveR = isKeyPress(GLFW_KEY_D)
                        || isKeyPress(GLFW_KEY_RIGHT);
        if (moveL) {
            --xa;
            facingRight = false;
        }
        if (moveR) {
            ++xa;
            facingRight = true;
        }
        if (moveL ^ moveR) {
            animation++;
        } else {
            if (animation > 4) animation %= 4;
            else if (animation > 0) animation--;
            else animation = 0;
        }
        if (isKeyPress(GLFW_KEY_SPACE)
            || isKeyPress(GLFW_KEY_W)
            || isKeyPress(GLFW_KEY_UP)) {
            velocity.y = 0.5f;
        }
        return xa;
    }

    @Override
    public void tick() {
        super.tick();
        double xa = 0.0;
        if (Mc2dClient.getInstance().screen == null) {
            xa = processInput();
        }
        moveRelative(xa, onGround ? 0.1f : 0.02f);
        velocity.y -= 0.08;
        move((float) velocity.x(), (float) velocity.y());
        velocity.mul(0.91, 0.98, 0.91);
        if (onGround) {
            velocity.mul(0.7, 1.0, 0.7);
        }
    }

    public void render(float delta, int mouseX, int mouseY) {
        var client = Mc2dClient.getInstance();
        client.getTextureManager().bindTexture(TEXTURE);
        glPushMatrix();
        glTranslated(lerpPos.x(), lerpPos.y(), 0);
        model.render(delta, facingRight, animation);
        glPopMatrix();
    }

    @Override
    public IBinaryTag save(IBinaryTag tag) {
        tag.set("version", PLAYER_VERSION);
        return super.save(tag);
    }
}
