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

import io.github.overrun.mc2d.util.registry.Registry;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.world.ibt.IBTType;
import io.github.overrun.mc2d.world.ibt.IBinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.overrun.swgl.core.phys.p2d.AABRect2f;
import org.overrun.swgl.core.util.math.Numbers;

import java.util.UUID;

/**
 * The base entity.
 *
 * @author squid233
 * @since 0.6.0
 */
public class Entity {
    public final World world;
    public final EntityType<?> entityType;
    public final Vector3d prevPos = new Vector3d();
    public final Vector3d position = new Vector3d();
    public final Vector3d lerpPos = new Vector3d();
    public final Vector3d velocity = new Vector3d();
    protected UUID uuid;
    private @Nullable String displayName;
    public AABRect2f box;
    public boolean onGround = false;
    public boolean removed = false;
    protected float bbWidth = 0.6f;
    protected float bbHeight = 1.8f;

    public Entity(World world, EntityType<? extends Entity> entityType) {
        this.world = world;
        this.entityType = entityType;
        uuid = UUID.randomUUID();
        resetPosition(0, 0, 0);
    }

    public UUID uuid() {
        return uuid;
    }

    public void setUUID(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public String displayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }

    public void resetPosition(double x, double y, double z) {
        position.set(x, y, z);
        float hw = bbWidth * 0.5f;
        box = new AABRect2f((float) (x - hw), (float) y, (float) (x + hw), (float) (y + bbHeight));
    }

    public void teleport(double x, double y, double z) {
        position.set(x, y, z);
        float hw = bbWidth * 0.5f;
        box.set((float) (x - hw), (float) y, (float) (x + hw), (float) (y + bbHeight));
    }

    public void remove() {
        removed = true;
    }

    public void tick() {
        prevPos.set(position);
    }

    public void moveRelative(double x, float speed) {
        if ((x * x) >= 0.01f) {
            velocity.x += x * speed / Math.abs(x);
        }
    }

    public void move(float x, float y) {
        float xaOrg = x;
        float yaOrg = y;
        var cubes = world.getCubes(1, box.expand(x, y, new AABRect2f()));
        for (var cube : cubes) {
            y = box.clipYCollide(y, cube);
        }
        box.move(0.0f, y);
        for (var cube : cubes) {
            x = box.clipXCollide(x, cube);
        }
        box.move(x, 0.0f);
        onGround = yaOrg != y && yaOrg < 0.0f;
        if (Numbers.isNonEqual(xaOrg, x))
            velocity.x = 0.0f;
        if (Numbers.isNonEqual(yaOrg, y))
            velocity.y = 0.0f;
        position.set(
            (box.minX() + box.maxX()) * 0.5f,
            box.minY(),
            position.z()
        );
    }

    /**
     * Sets the tag to serialize.
     *
     * @param tag the tag
     * @return the tag with new properties
     */
    public IBinaryTag save(IBinaryTag tag) {
        tag.set("type", Registry.ENTITY.getId(entityType).toString());
        tag.set("uuid", uuid.toString());
        tag.set("position", new double[]{position.x(), position.y(), position.z()});
        tag.set("velocity", new double[]{velocity.x(), velocity.y(), velocity.z()});
        tag.set("onGround", (byte) (onGround ? 1 : 0));
        return tag;
    }

    /**
     * Load properties from the tag.
     *
     * @param tag the tag
     */
    public void load(IBinaryTag tag) {
        double[] pos = tag.get(IBTType.DOUBLE_ARRAY, "position");
        teleport(pos[0], pos[1], pos[2]);
        prevPos.set(pos);
        double[] vel = tag.get(IBTType.DOUBLE_ARRAY, "velocity");
        velocity.set(vel);
        onGround = tag.get(IBTType.BYTE, "onGround") != 0;
    }
}
