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

import io.github.overrun.mc2d.asset.AssetManager;
import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.util.Highlight;
import io.github.overrun.mc2d.util.Identifier;

import java.awt.Graphics;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author squid233
 * @date 2020/9/14
 */
public class Block extends AbstractBlock {
    private static final long serialVersionUID = 4041316942709149977L;
    private Identifier regName;
    private final Properties model = new Properties();
    private boolean loadedModel;

    public Block(Settings settings) { }

    @Override
    public void draw(Graphics g, int x) {
        g.drawImage(Images.getBlockTexture(this), getPreviewX(x), getPreviewY(), 16, 16, null);
        Highlight.block(g, getPreviewX(x), getPreviewY());
    }

    @Override
    public Block setRegistryName(Identifier registryName) {
        if (regName != null) {
            throw new IllegalStateException("Registry name is already exist. Old: " + regName + ", New: " + registryName);
        }
        regName = registryName;
        return this;
    }

    @Override
    public Identifier getRegistryName() {
        try {
            return regName;
        } catch (Exception e) {
            return new Identifier("air");
        }
    }

    @Override
    public void draw(Graphics g) {
        draw(g, x);
    }

    @Override
    public AbstractBlock setPos(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        return this;
    }

    @Override
    public Properties getModel() {
        if (!loadedModel) {
            try (FileReader fr =
                         new FileReader(AssetManager.getAsString(getRegistryName().getNamespace(), "models", "block", getRegistryName().getPath() + ".mc2dm"))) {
                model.load(fr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadedModel = true;
        }
        return model;
    }

    public static class Settings { }
}
