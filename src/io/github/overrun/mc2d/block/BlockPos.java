package io.github.overrun.mc2d.block;

import java.util.StringJoiner;

/**
 * @author squid233
 */
public class BlockPos {
    private final int x;
    private final int y;

    public BlockPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public BlockPos() {
        this(0, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BlockPos && ((BlockPos) o).getX() == getX() && ((BlockPos) o).getY() == getY();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BlockPos.class.getSimpleName() + "[", "]")
                .add("x=" + x)
                .add("y=" + y)
                .toString();
    }
}
