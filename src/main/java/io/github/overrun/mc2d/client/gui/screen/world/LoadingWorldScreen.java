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

package io.github.overrun.mc2d.client.gui.screen.world;

import io.github.overrun.mc2d.client.gui.screen.DirtScreen;
import io.github.overrun.mc2d.client.world.render.WorldRenderer;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.util.Utils;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.world.entity.EntityTypes;
import io.github.overrun.mc2d.world.entity.player.PlayerEntity;
import org.overrun.swgl.core.util.LogFactory9;
import org.slf4j.Logger;

/**
 * @author squid233
 * @since 2021/01/26
 */
public final class LoadingWorldScreen extends DirtScreen {
    private static final Logger logger = LogFactory9.getLogger();

    public LoadingWorldScreen() {
        super(IText.translatable("text.screen.world.loading"));
    }

    @Override
    public void tick() {
        super.tick();
        try {
            client.world = new World(256, 128);
            if (!client.world.load()) {
                client.world.genTerrain();
            } else {
                client.player = (PlayerEntity) client.world.getEntity(PlayerEntity.generateUUID("Player0"));
            }
            // TODO: 2022/9/6 Username
            if (client.player == null) {
                client.player = Utils.also(client.world.spawnEntity(EntityTypes.PLAYER), e ->
                    e.setUUID(PlayerEntity.generateUUID("Player0"))
                );
            }
            client.player.setDisplayName("Player0");
            client.worldRenderer = new WorldRenderer(client, client.world);
            client.world.addListener(client.worldRenderer);
            onClose();
        } catch (Exception e) {
            client.world = null;
            logger.error("Exception occurred loading world!", e);
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        client.openScreen(null);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
