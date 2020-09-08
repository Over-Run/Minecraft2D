package io.github.overrun.mc2d.block;

import io.github.overrun.mc2d.engine.ITickable;
import io.github.overrun.mc2d.registry.IRegistrable;

import java.awt.Graphics;
import java.util.Properties;

/**
 * @author squid233
 */
public class Block extends AbstractBlock implements IRegistrable, ITickable {
    private Properties model;

    public Block(Settings settings) {
        super(settings);
    }

    public Properties getModel() {
        return model;
    }

    public void setModel(Properties model) {
        this.model = model;
    }

    @Override
    public void draw(Graphics g) {
        g.draw3DRect(100, 100, 60, 60, false);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Block;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void onTick() { }

    public static class Settings extends AbstractBlock.Settings { }
}
