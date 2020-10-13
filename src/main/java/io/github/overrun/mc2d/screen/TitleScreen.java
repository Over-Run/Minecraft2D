/*
 * MIT License
 *
 * Copyright (c) 2020 Over-Run
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

package io.github.overrun.mc2d.screen;

import io.github.overrun.mc2d.Minecraft2D;
import io.github.overrun.mc2d.image.ImageIcons;
import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.text.TranslatableText;
import io.github.overrun.mc2d.util.ResourceLocation;

import java.awt.Graphics;
import java.awt.Image;

import static io.github.overrun.mc2d.Minecraft2D.LOGGER;

/**
 * @author squid233
 * @since 2020/10/12
 */
public class TitleScreen extends ScreenHandler {
    public static final Image LOGO = ImageIcons.getGameImage("textures/gui/logo.png");
    public static final int LOGO_WIDTH = 356;
    public static final int LOGO_HEIGHT = 59;

    public TitleScreen() {
        addButton(new ButtonWidget(150, 400, TranslatableText.of("button.minecraft2d.singleplayer"), button -> LOGGER.info("Pressed!")));
        addButton(new ButtonWidget((Minecraft2D.getWidth() >> 1) - 208, 180, 200, TranslatableText.of("button.minecraft2d.options"),
                button -> LOGGER.info("Pressed!")));
        addButton(new ButtonWidget((Minecraft2D.getWidth() >> 1) - 8, 180, 200, TranslatableText.of("button.minecraft2d.exit_game"),
                button -> System.exit(0)));
        // (+ 152) == (- 208 + 360)
        addButton(new LanguageButtonWidget((Minecraft2D.getWidth() >> 1) + 152, 210, button -> Screens.setOpening(Screens.LANGUAGES_SCREEN)));
    }

    @Override
    public void render(Graphics g) {
        drawOptionsBg(g);
        drawDefaultBackground(g);
        Screen.drawImage(g, LOGO, (Minecraft2D.getWidth() >> 1) - LOGO_WIDTH - 8, 10, LOGO_WIDTH << 1, LOGO_HEIGHT << 1);
        super.render(g);
    }

    @Override
    public ResourceLocation getTexture() {
        return Images.OPTIONS_BG_ID;
    }
}
