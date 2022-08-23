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
import org.joml.SimplexNoise;
import org.overrun.swgl.core.phys.p2d.AABRect2f;
import org.overrun.swgl.core.util.LogFactory9;
import org.slf4j.Logger;

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
    private static final Logger logger = LogFactory9.getLogger();
    private static final long WORLD_VERSION = 3L;
    public int pickZ = 0;
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
    }

    private static float sumOctaves(
        int numIterations,
        float x,
        float y,
        float z,
        float persistence,
        float scale,
        float low,
        float high
    ) {
        float maxAmp = 0;
        float amp = 1;
        float freq = scale;
        float noise = 0;
        for (int i = 0; i < numIterations; i++) {
            noise += SimplexNoise.noise(x * freq, y * freq, z * freq) * amp;
            maxAmp += amp;
            amp *= persistence;
            freq *= 2;
        }
        noise /= maxAmp;
        noise = (noise * (high - low) + (high + low)) * 0.5f;
        return noise;
    }

    public void genTerrain() {
        // generate the terrain
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                setBlock(x, 0, z, BEDROCK);

                int layers = (int) sumOctaves(16, x, 0, z, 0.5f, 0.007f, 1, height - 12);
                setBlock(x, layers - 1, z, GRASS_BLOCK);
                for (int y = layers - 4, c = layers - 1; y < c; y++) {
                    setBlock(x, y, z, DIRT);
                }
                for (int y = 1, c = layers - 4; y < c; y++) {
                    setBlock(x, y, z, STONE);
                }
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
        }
        return AIR;
    }

    public int getIndex(int x, int y, int z) {
        return x + y * width + z * width * height;
    }

    public List<AABRect2f> getCubes(int z, AABRect2f origin) {
        var lst = new ArrayList<AABRect2f>();
        int x0 = (int) origin.minX();
        int y0 = (int) origin.minY();
        int x1 = (int) (origin.maxX() + 1.0f);
        int y1 = (int) (origin.maxY() + 1.0f);

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
                    lst.add(new AABRect2f(aabb.minX() + x,
                        aabb.minY() + y,
                        aabb.maxX() + x,
                        aabb.maxY() + y));
                }
            }
        }
        return lst;
    }

    public boolean load(Player player) {
        logger.info("Loading world");
        var file = new File("level.dat");
        if (file.exists()) {
            try (var fis = new FileInputStream(file);
                 var gis = new GZIPInputStream(fis);
                 var isr = new InputStreamReader(gis);
                 var br = new BufferedReader(isr);
                 var jr = new JsonReader(br)) {
                deserialize(jr, player);
                return true;
            } catch (IOException e) {
                logger.error("Catching", e);
                return false;
            }
        }
        return false;
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
            logger.error("Catching", e);
        }
    }

    public void serialize(JsonWriter writer, Player player) throws IOException {
        writer.beginObject()
            .name("version").value(WORLD_VERSION)
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
                    if (WORLD_VERSION != v) {
                        throw new RuntimeException("Doesn't compatible with version " + v + ". Current is " + WORLD_VERSION);
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
