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

package io.github.overrun.mc2d.block;

import io.github.overrun.mc2d.Minecraft2D;
import io.github.overrun.mc2d.item.ItemConvertible;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.Properties;

/**
 * @author squid233
 * @since 2020/09/14
 */
public abstract class AbstractBlock implements Serializable, ItemConvertible {
    private static final long serialVersionUID = 7516708491939894861L;
    public int x;
    public int y;

    public void draw(Graphics g, int x) { }

    /**
     * draw block screen
     *
     * @param g graphics
     */
    public abstract void draw(Graphics g);

    public int getX() {
        return x;
    }

    public AbstractBlock setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public AbstractBlock setY(int y) {
        this.y = y;
        return this;
    }

    public AbstractBlock setPos(BlockPos pos) {
        return setPos(pos.toString());
    }

    /**
     * set pos
     *
     * @param pos pos
     * @return this
     */
    public abstract AbstractBlock setPos(String pos);

    public AbstractBlock setPos(int x, int y) {
        setPos(BlockPos.of(x, y));
        return this;
    }

    public BlockPos getPos() {
        return BlockPos.of(x, y);
    }

    public int getPreviewX(int x) {
        return (x << 4) + 8;
    }

    public int getPreviewX() {
        return getPreviewX(x);
    }

    public int getPreviewY() {
        return Minecraft2D.getHeight() - ((y << 4) + 24);
    }

    /**
     * get block model
     *
     * @return model
     */
    public abstract Properties getModel();

    public static class Settings { }
}
