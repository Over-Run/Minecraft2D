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

package io.github.overrun.mc2d.util.factory;

import io.github.overrun.mc2d.block.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Use {@link Mc2dFactories#getBlockPos()}
 *
 * @author squid233
 * @date 2020/9/16
 */
public class BlockPosFactory {
    private static final HashMap<ArrayList<Integer>, BlockPos> BLOCK_POS_MAP = new HashMap<>();
    private static final ArrayList<Integer> X_AND_Y = new ArrayList<>(2);

    BlockPosFactory() {
        X_AND_Y.add(0);
        X_AND_Y.add(0);
    }

    public BlockPos get(int x, int y) {
        X_AND_Y.set(0, x);
        X_AND_Y.set(1, y);
        if (BLOCK_POS_MAP.get(X_AND_Y) == null || !BLOCK_POS_MAP.containsKey(X_AND_Y)) {
            BLOCK_POS_MAP.put(X_AND_Y, new BlockPos(x, y));
        }
        return BLOCK_POS_MAP.get(X_AND_Y);
    }
}
