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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import static io.github.overrun.mc2d.Minecraft2D.getMouseX;
import static io.github.overrun.mc2d.Minecraft2D.getMouseY;
import static io.github.overrun.mc2d.util.DrawHelper.drawImage;

/**
 * @author squid233
 * @since 2020/12/02
 */
public abstract class AbstractButtonWidget extends ScreenWidget {
    public abstract boolean isEnable();
    public abstract Image getUsualTexture();
    public abstract int getHeight();
    public abstract int getWidth();

    public Point getPrevPos() {
        return new Point(getX(), getY());
    }

    public boolean isHover() {
        return getMouseX() > getPrevPos().getX()
                && getMouseX() < getPrevPos().getX() + getWidth()
                && getMouseY() > getPrevPos().getY()
                && getMouseY() < getPrevPos().getY() + getHeight();
    }

    public Image getHoverTexture() {
        return getUsualTexture();
    }

    public Image getDisableTexture() {
        return getUsualTexture();
    }

    public Image getTexture() {
        return isEnable() ? isHover() ? getHoverTexture() : getUsualTexture() : getDisableTexture();
    }

    public abstract PressAction getAction();

    public interface PressAction {
        void onPress(AbstractButtonWidget widget);
    }

    @Override
    public void render(Graphics g) {
        drawImage(g, getTexture(), getPrevPos(), getWidth(), getHeight());
    }
}
