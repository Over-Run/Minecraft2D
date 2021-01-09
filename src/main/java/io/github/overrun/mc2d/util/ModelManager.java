/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Over-Run
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

package io.github.overrun.mc2d.util;

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.Blocks;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.lwjgl.opengl.GL11;

/**
 * @author squid233
 * @since 2021/01/09
 */
public final class ModelManager {
    public static final Object2IntMap<Block> BLOCK_MODELS = new Object2IntArrayMap<>(2);

    public static int putModel(Block block) {
        int id = ImageReader.loadTexture(getModelPath(block), GL11.GL_NEAREST);
        BLOCK_MODELS.put(block, id);
        return id;
    }

    public static String getModelPath(int rawId) {
        return getModelPath(Blocks.RAW_ID_BLOCKS.get((byte) rawId));
    }

    public static String getModelPath(Block block) {
        return Blocks.BLOCKS.inverse().get(block) + ".png";
    }

    public static int getModelId(Block block) {
        return BLOCK_MODELS.getOrDefault(block, 0);
    }

    public static int getModelId(int rawId) {
        return getModelId(Blocks.RAW_ID_BLOCKS.get((byte) rawId));
    }
}
