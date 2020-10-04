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

import io.github.overrun.mc2d.util.IntUtil;

import java.util.HashMap;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class BlockPos {
    private final int x;
    private final int y;
    private static final HashMap<String, BlockPos> CACHE = new HashMap<>();

    private BlockPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static BlockPos of(int x, int y) {
        String st = x + "," + y;
        if (!CACHE.containsKey(st)) {
            CACHE.put(st, new BlockPos(x, y));
        }
        return CACHE.get(st);
    }

    /**
     * Use string to get this object.
     * {@code pos} must be {@code BlockPos{x=0, y=0}} or {@code 0,0}. Otherwise throw exception
     *
     * @param pos BlockPos string
     * @return this
     */
    public static BlockPos of(String pos) {
        String dmp = pos;
        int x, y;
        if (dmp.startsWith(BlockPos.class.getSimpleName()
                + "{") && dmp.endsWith("}")) {
            dmp = dmp.substring(9, dmp.length() - 1);
            String[] dmp1 = dmp.split(", ");
            x = IntUtil.parseInt(dmp1[0].split("=")[1]);
            y = IntUtil.parseInt(dmp1[1].split("=")[1]);
        } else if (
                dmp.contains(",")) {
            String[] dmp1 = dmp.split(",");
            x = IntUtil.parseInt(dmp1[0]);
            y = IntUtil.parseInt(dmp1[1]);
        } else {
            throw new IllegalArgumentException("pos is invalid string: " + pos + ", wrong format");
        }
        return of(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BlockPos.class.getSimpleName() + "[", "]")
                .add("x=" + x)
                .add("y=" + y)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BlockPos && ((BlockPos) o).x == x && ((BlockPos) o).y == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
