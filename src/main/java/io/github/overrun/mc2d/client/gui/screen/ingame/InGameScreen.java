/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Over-Run
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

package io.github.overrun.mc2d.client.gui.screen.ingame;

import io.github.overrun.mc2d.Player;
import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.level.World;
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.util.Identifier;

import static io.github.overrun.mc2d.block.Blocks.RAW_ID_BLOCKS;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/25
 */
public final class InGameScreen extends Screen {
    private World world;
    private Player player;

    public InGameScreen() {
        super("");
    }

    @Override
    protected void init() {
        super.init();
        world = client.world;
        player = client.player;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        if (world != null) {
            world.render(client, mouseX, mouseY, width, height);
        }
        glPushMatrix();
        glTranslatef(width - 64, 0, 0);
        client.getTextureManager().bindTexture(new Identifier("textures/block/" + RAW_ID_BLOCKS.get(player.handledBlock).toString() + ".png"));
        drawTexture(0, 0, 64, 64);
        glPopMatrix();
        player.move();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW_KEY_ENTER && world != null) {
            world.save();
        }
        if (keyCode == GLFW_KEY_1) {
            player.handledBlock = 1;
        }
        if (keyCode == GLFW_KEY_2) {
            player.handledBlock = 2;
        }
        if (keyCode == GLFW_KEY_3) {
            player.handledBlock = 3;
        }
        if (keyCode == GLFW_KEY_4) {
            player.handledBlock = 4;
        }
        if (keyCode == Options.getI(Options.KEY_CREATIVE_TAB, GLFW_KEY_E)) {
            client.openScreen(new CreativeTabScreen(player, this));
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        super.onClose();
        client.openScreen(new PauseScreen(this));
    }
}
