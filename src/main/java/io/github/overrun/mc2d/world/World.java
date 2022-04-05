/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Overrun Organization
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

package io.github.overrun.mc2d.world;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.registry.Registry;
import io.github.overrun.mc2d.world.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.overrun.swgl.core.phys.p2d.AABBox2f;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static io.github.overrun.mc2d.world.block.Blocks.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public class World {
    private static final Logger logger = LogManager.getLogger(World.class.getName());
    private static final long serialVersionUID = 3L;
    public static int z;
    private final Identifier[] blocks;
    public final int width;
    public final int height;
    public final int depth;

    public World(int w, int h) {
        width = w;
        height = h;
        depth = 2;
        blocks = new Identifier[w * h * depth];
        Arrays.fill(blocks, AIR.getId());
        // generate the terrain
        for (int i = 0; i < w; i++) {
            for (byte j = 0; j < 2; j++) {
                setBlock(i, 0, j, BEDROCK);
                setBlock(i, 0, j, BEDROCK);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < w; j++) {
                for (byte k = 0; k < 2; k++) {
                    setBlock(j, i + 1, k, COBBLESTONE);
                    setBlock(j, i + 1, k, COBBLESTONE);
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < w; j++) {
                for (byte k = 0; k < 2; k++) {
                    setBlock(j, i + 3, k, DIRT);
                    setBlock(j, i + 3, k, DIRT);
                }
            }
        }
        for (int i = 0; i < w; i++) {
            for (byte j = 0; j < 2; j++) {
                setBlock(i, 5, j, GRASS_BLOCK);
                setBlock(i, 5, j, GRASS_BLOCK);
            }
        }
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth) {
            blocks[getIndex(x, y, z)] = block.getId();
        }
    }

    public Block getBlock(int x, int y, int z) {
        if (x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth) {
            return Registry.BLOCK.getById(blocks[getIndex(x, y, z)]);
        } else {
            return AIR;
        }
    }

    public int getIndex(int x, int y, int z) {
        return x % width + y * width + z * width * height;
    }

    public List<AABBox2f> getCubes(int z, AABBox2f origin) {
        var lst = new ArrayList<AABBox2f>();
        int x0 = (int) origin.getMinX();
        int y0 = (int) origin.getMinY();
        int x1 = (int) (origin.getMaxX() + 1.0f);
        int y1 = (int) (origin.getMaxY() + 1.0f);

        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }

        if (x1 > width) {
            x1 = width;
        }
        if (y1 > height) {
            y1 = height;
        }

        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                var aabb = getBlock(x, y, z).getCollisionShape();
                if (aabb != null) {
                    lst.add(new AABBox2f(aabb.getMinX() / 16.0f + x, aabb.getMinY() / 16.0f + y, aabb.getMaxX() / 16.0f + x, aabb.getMaxY() / 16.0f + y));
                }
            }
        }
        return lst;
    }

    public void load(Player player) {
        logger.info("Loading world");
        var file = new File("level.dat");
        if (file.exists()) {
            try (var fis = new FileInputStream(file);
                 var gis = new GZIPInputStream(fis);
                 var isr = new InputStreamReader(gis);
                 var br = new BufferedReader(isr);
                 var jr = new JsonReader(br)) {
                deserialize(jr, player);
            } catch (IOException e) {
                logger.catching(e);
            }
        }
    }

    public void save(Player player) {
        logger.info("Saving world");
        try (var fos = new FileOutputStream("level.dat");
             var gos = new GZIPOutputStream(fos);
             var osw = new OutputStreamWriter(gos);
             var bw = new BufferedWriter(osw);
             var jw = new JsonWriter(bw)) {
            serialize(jw, player);
        } catch (IOException e) {
            logger.catching(e);
        }
    }

    public void serialize(JsonWriter writer, Player player) throws IOException {
        writer.beginObject()
            .name("version").value(serialVersionUID)
            .name("width").value(width)
            .name("height").value(height)
            .name("depth").value(depth)
            .name("player");
        player.serialize(writer);
        var idMap = new HashMap<Identifier, Integer>();
        writer.name("id_map").beginObject();
        for (var reg : Registry.BLOCK) {
            var k = Registry.BLOCK.getRawId(reg.getValue());
            var v = reg.getKey();
            writer.name(String.valueOf(k)).value(v.toString());
            idMap.put(v, k);
        }
        writer.endObject();
        writer.name("blocks").beginArray();
        for (var block : blocks) {
            writer.value(idMap.get(block));
        }
        writer.endArray().endObject();
    }

    public void deserialize(JsonReader reader, Player player) throws IOException {
        var idMap = new HashMap<Integer, Identifier>();
        int w = 0, h = 0, d = 0;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "version" -> {
                    var v = reader.nextLong();
                    if (serialVersionUID != v) {
                        throw new RuntimeException("Doesn't compatible with version " + v + ". Current is " + serialVersionUID);
                    }
                }
                case "width" -> w = reader.nextInt();
                case "height" -> h = reader.nextInt();
                case "depth" -> d = reader.nextInt();
                case "player" -> player.deserialize(reader);
                case "id_map" -> {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        idMap.put(Integer.parseInt(reader.nextName()),
                            new Identifier(reader.nextString()));
                    }
                    reader.endObject();
                }
                case "blocks" -> {
                    reader.beginArray();
                    for (int i = 0, l = w * h * d; i < l; i++) {
                        blocks[i] = idMap.get(reader.nextInt());
                    }
                    reader.endArray();
                }
            }
        }
        reader.endObject();
    }
}
