/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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

package io.github.overrun.mc2d.client.model;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.mod.ModLoader;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.registry.BaseRegistry;
import io.github.overrun.mc2d.util.registry.Registry;
import io.github.overrun.mc2d.world.block.BlockType;
import org.overrun.swgl.core.asset.tex.TextureParam;
import org.overrun.swgl.core.asset.tex.atlas.SpriteInfo;
import org.overrun.swgl.core.asset.tex.atlas.TextureAtlas;
import org.overrun.swgl.core.io.IFileProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.function.Function;

import static org.lwjgl.opengl.GL11C.GL_NEAREST;

/**
 * @author squid233
 * @since 0.6.0
 */
public class BlockModelMgr {
    private static final Comparator<SpriteInfo> COMPARATOR =
        Comparator.comparingInt((SpriteInfo info) -> -info.height()).thenComparingInt(info -> -info.width());
    public static final Identifier BLOCK_ATLAS = new Identifier("textures/block-atlas");
    private static TextureAtlas blockAtlas;

//    public static final Identifier ITEM_ATLAS = new Identifier("textures/item-atlas");
//    private static TextureAtlas itemAtlas;

    public static String blockTexture(Identifier id) {
        return "assets/" + id.getNamespace() + "/textures/" + id.getPath() + ".png";
    }

//    public static String itemTexture(Identifier id) {
//        return "assets/" + id.getNamespace() + "/" + "textures/item/" + id.getPath() + ".png";
//    }

    private static TextureAtlas loadAtlas(Function<Identifier, String> function,
                                          BaseRegistry<?> registry,
                                          Identifier id) {
        var atlas = new TextureAtlas(0);
        var textures = new HashSet<Identifier>();
        var infoList = new ArrayList<SpriteInfo>();
        for (var e : registry) {
            var tex = ((BlockType) e.getValue()).getTexture();
            if (tex != null) {
                textures.add(tex);
            }
        }
        for (var tex : textures) {
            infoList.add(new SpriteInfo(
                function.apply(tex),
                tex.isVanilla() ? IFileProvider.ofCaller() : IFileProvider.of(ModLoader.getLoader(tex.getNamespace())),
                16,
                16)
            );
        }
        infoList.sort(COMPARATOR);
        atlas.extraParam(new TextureParam().minFilter(GL_NEAREST).magFilter(GL_NEAREST));
        atlas.load(infoList);
        Mc2dClient.getInstance().getTextureManager().addTexture(id, atlas.getId());
        return atlas;
    }

    public static void loadAtlas() {
        blockAtlas = loadAtlas(BlockModelMgr::blockTexture, Registry.BLOCK, BLOCK_ATLAS);
//        itemAtlas = loadAtlas(BlockModelMgr::itemTexture, Registry.ITEM, ITEM_ATLAS);
    }

    public static TextureAtlas getBlockAtlas() {
        return blockAtlas;
    }

//    public static TextureAtlas getItemAtlas() {
//        return itemAtlas;
//    }
}
