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

import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.registry.Registry;
import io.github.overrun.mc2d.world.entity.player.PlayerEntity;

/**
 * The builtin entity types.
 *
 * @author squid233
 * @since 0.6.0
 */
public class EntityTypes {
    public static final EntityType<PlayerEntity> PLAYER = register(1, "player", new EntityTypeBuilder<>(PlayerEntity::new).build(PlayerEntity.WORLD_FIXER));
    public static final EntityType<HumanEntity> HUMAN = register(2, "human", new EntityTypeBuilder<>(HumanEntity::new).build(null));

    public static void register() {
    }

    private static <T extends EntityType<?>> T register(int rawId, String id, T t) {
        return Registry.ENTITY.set(rawId, new Identifier(id), t);
    }
}
