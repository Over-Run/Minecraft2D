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

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author squid233
 * @since 2020/09/15
 */
public final class BlockPos implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int x;
    private final int y;

    public BlockPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static BlockPos of() { return of(0, 0); }

    public static BlockPos of(int x, int y) { return new BlockPos(x, y); }

    public int getX() { return x; }

    public int getY() { return y; }

    @Override
    public String toString() {
        return new StringJoiner(", ", BlockPos.class.getSimpleName() + "[", "]")
                .add("x=" + x)
                .add("y=" + y)
                .toString();
    }

    @Override
    public boolean equals(Object o) { return o instanceof BlockPos && ((BlockPos) o).x == x && ((BlockPos) o).y == y; }

    @Override
    public int hashCode() { return Objects.hash(getX(), getY()); }
}
