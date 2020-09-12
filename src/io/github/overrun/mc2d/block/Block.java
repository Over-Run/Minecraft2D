package io.github.overrun.mc2d.block;

import io.github.overrun.mc2d.Mc2D;
import io.github.overrun.mc2d.engine.ITickable;
import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.registry.IRegistrable;
import io.github.overrun.mc2d.util.Highlight;

import java.awt.Graphics;
import java.util.Properties;

/**
 * @author squid233
 */
public class Block extends AbstractBlock implements IRegistrable, ITickable {
    public int x = 0;
    public int y = 0;
    protected Properties model;

    public Block(Settings settings) {
        super(settings);
    }

    public Properties getModel() {
        return model;
    }

    public void setModel(Properties model) {
        this.model = model;
    }

    public void setPos(BlockPos pos) {
        x = pos.getX();
        y = pos.getY();
    }

    public BlockPos getPos() {
        return new BlockPos(x, y);
    }

    @Override
    public void draw(Graphics g, BlockPos pos) {
        int prevX = (pos.getX() << 4) + 8;
        int prevY = Mc2D.getClient().getHeight() - ((pos.getY() << 4) + 24);
        g.drawImage(Images.getBlockTexture(this), prevX, prevY, 16, 16, null);
        Highlight.block(g, prevX, prevY);
    }

    @Override
    public boolean equals(Object obj) {
        Block b;
        if (obj instanceof Block) {
            b = (Block) obj;
        } else {
            return false;
        }
        return b.model.equals(model);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void onTick() { }

    public static class Settings extends AbstractBlock.Settings { }
}
