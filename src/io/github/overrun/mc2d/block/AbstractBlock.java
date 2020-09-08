package io.github.overrun.mc2d.block;

import io.github.overrun.mc2d.item.ItemConvertible;

import java.awt.*;

/**
 * @author squid233
 */
public abstract class AbstractBlock implements ItemConvertible {
    public AbstractBlock(Settings settings) { }

    /**
     * draw block to screen
     *
     * @param g target graphics
     */
    public abstract void draw(Graphics g);

    /**
     * 比较两个方块是否相同
     *
     * @param obj target object
     * @return if equals target obj
     */
    @Override
    public abstract boolean equals(Object obj);

    public static class Settings {}
}
