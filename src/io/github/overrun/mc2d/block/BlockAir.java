package io.github.overrun.mc2d.block;

import io.github.overrun.mc2d.Mc2D;
import io.github.overrun.mc2d.option.Options;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author squid233
 */
public class BlockAir extends Block {
    public BlockAir(Settings settings) {
        super(settings);
    }

    @Override
    public void draw(Graphics g, BlockPos pos) {
        int prevX = (pos.getX() << 4) + 8;
        int prevY = Mc2D.getClient().getHeight() - ((pos.getY() << 4) + 24);
        if (Options.getB(Options.POS_GRID_OPT)) {
            Color c = g.getColor();
            g.setColor(new Color(128, 128, 128, 128));
            g.drawRect(prevX, prevY, 15, 15);
            g.setColor(c);
        }
    }
}
