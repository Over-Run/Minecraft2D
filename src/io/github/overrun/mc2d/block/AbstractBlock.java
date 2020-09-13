package io.github.overrun.mc2d.block;

import io.github.overrun.mc2d.item.ItemConvertible;

import java.awt.*;
import java.io.Serializable;

/**
 * @author squid233
 */
public abstract class AbstractBlock implements ItemConvertible, Serializable {
    private static final long serialVersionUID = 5705287045260471081L;

    public AbstractBlock(Settings settings) { }

    /**
     * draw block to screen
     *
     * @param g target graphics
     * @param pos block pos
     */
    public abstract void draw(Graphics g, BlockPos pos);

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
