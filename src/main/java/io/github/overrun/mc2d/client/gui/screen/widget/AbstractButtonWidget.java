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

package io.github.overrun.mc2d.client.gui.screen.widget;

import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.client.util.BuiltinGraphics;
import io.github.overrun.mc2d.text.IText;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import static io.github.overrun.mc2d.Minecraft2D.getMouseX;
import static io.github.overrun.mc2d.Minecraft2D.getMouseY;

/**
 * @author squid233
 * @since 2020/12/02
 */
public abstract class AbstractButtonWidget implements Element {
    public abstract AbstractButtonWidget setEnable(boolean enable);

    /**
     * If the button is enable, the button can click
     *
     * @return Is the button enable
     */
    public abstract boolean isEnable();
    public abstract IText getText();
    public abstract Image getUsualTexture();
    public abstract int getWidth();
    public abstract int getHeight();

    /**
     *
     *
     * @return A list collect the tooltips.
     */
    public abstract List<IText> getTooltips();

    public Point getPrevPos() {
        return new Point(getX(), getY());
    }

    /**
     * Render the tooltips. Invoke by {@link Screen}.
     *
     * @param g The Graphics object.
     */
    public void renderTooltips(BuiltinGraphics g) {
        if (isHover()) {
            g.renderTooltips(getTooltips(), getMouseX(), getMouseY());
        }
    }

    public boolean isHover() {
        return getMouseX() > getPrevPos().getX()
                && getMouseX() < getPrevPos().getX() + (getWidth() << 1)
                && getMouseY() > getPrevPos().getY()
                && getMouseY() < getPrevPos().getY() + (getHeight() << 1);
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
        void onPress(AbstractButtonWidget button);
    }
}
