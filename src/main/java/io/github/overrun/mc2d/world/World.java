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

import io.github.overrun.mc2d.client.GameVersion;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.Utils;
import io.github.overrun.mc2d.util.registry.Registry;
import io.github.overrun.mc2d.world.block.BlockType;
import io.github.overrun.mc2d.world.entity.Entity;
import io.github.overrun.mc2d.world.entity.EntityType;
import io.github.overrun.mc2d.world.entity.EntityTypes;
import io.github.overrun.mc2d.world.entity.player.PlayerEntity;
import io.github.overrun.mc2d.world.ibt.BinaryTag;
import io.github.overrun.mc2d.world.ibt.IBinaryTag;
import org.jetbrains.annotations.Nullable;
import org.joml.SimplexNoise;
import org.overrun.swgl.core.phys.p2d.AABRect2f;
import org.overrun.swgl.core.util.LogFactory9;
import org.overrun.swgl.core.util.timing.Timer;
import org.slf4j.Logger;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static io.github.overrun.mc2d.world.block.Blocks.*;
import static io.github.overrun.mc2d.world.ibt.IBTType.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public class World {
    private static final Logger logger = LogFactory9.getLogger();
    public int pickZ = 1;
    private final BlockType[] blocks;
    public final int width;
    public final int height;
    public final int depth;
    public final Timer timer = new Timer(20);
    private final List<IWorldListener> listeners = new ArrayList<>();
    public final Map<UUID, Entity> entities = new HashMap<>();

    public World(int w, int h) {
        width = w;
        height = h;
        depth = 2;
        blocks = new BlockType[w * h * depth];
        Arrays.fill(blocks, AIR);
        for (int i = 0; i < 10; i++) {
            spawnEntity(EntityTypes.HUMAN);
        }
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

    public int getMinX() {
        return 0;
    }

    public int getMinY() {
        return 0;
    }

    public int getMinZ() {
        return 0;
    }

    public int getMaxX() {
        return width;
    }

    public int getMaxY() {
        return height;
    }

    public int getMaxZ() {
        return depth;
    }

    /**
     * Generates the terrain.
     */
    public void genTerrain() {
        // generate terrain
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                int layers = (int) sumOctaves(16, x, z, 0, 0.5f, 0.007f, 1, 64);
                setBlockStates(x, layers - 1, z, GRASS_BLOCK);
                for (int y = layers - 4, c = layers - 1; y < c; y++) {
                    setBlockStates(x, y, z, DIRT);
                }
                for (int y = 1, c = layers - 4; y < c; y++) {
                    setBlockStates(x, y, z, STONE);
                }
            }
        }
        // generate bedrock

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                setBlockStates(x, 0, z, BEDROCK);
            }
        }
    }

    public void addListener(IWorldListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IWorldListener listener) {
        listeners.remove(listener);
    }

    public @Nullable Entity getEntity(UUID uuid) {
        return entities.get(uuid);
    }

    public <T extends Entity> T spawnEntity(EntityType<T> entityType) {
        var entity = entityType.createEntity(this);
        entities.put(entity.uuid(), entity);
        return entity;
    }

    public void update() {
        timer.update();
        for (int i = 0; i < timer.ticks; i++) {
            tick();
        }
    }

    public void tick() {
        var it = entities.values().iterator();
        while (it.hasNext()) {
            var entity = it.next();
            entity.tick();
            if (entity.removed) {
                it.remove();
            }
        }
    }

    public boolean isInBorder(int x, int y, int z) {
        return x >= getMinX() && x < getMaxX() &&
               y >= getMinY() && y < getMaxY() &&
               z >= getMinZ() && z < getMaxZ();
    }

    public boolean setBlockStates(int x, int y, int z, BlockType blockStates) {
        if (isInBorder(x, y, z)) {
            final int index = getIndex(x, y, z);
            if (blocks[index] == blockStates) {
                return false;
            }
            for (var listener : listeners) {
                listener.blockChanged(x, y, z);
            }
            blocks[index] = blockStates;
            return true;
        }
        return false;
    }

    public BlockType getBlockStates(int x, int y, int z) {
        if (isInBorder(x, y, z)) {
            return blocks[getIndex(x, y, z)];
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
                var aabb = getBlockStates(x, y, z).getCollisionShape();
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

    public boolean load() {
        logger.info("Loading world");
        for (var listener : listeners) {
            listener.allChanged();
        }
        var file = new File("level.dat");
        if (file.exists()) {
            try (var is = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))))) {
                deserialize(BinaryTag.deserialize(is));
                return true;
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Catching loading world", e);
                return false;
            }
        }
        return false;
    }

    public void save() {
        logger.info("Saving world");
        try (var os = new ObjectOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream("level.dat"))))) {
            var tag = new BinaryTag();
            serialize(tag);
            tag.serialize(os);
        } catch (IOException e) {
            logger.error("Catching saving world", e);
        }
    }

    public void serialize(IBinaryTag tag) throws IOException {
        tag.set("version", GameVersion.worldVersion());
        tag.set("width", width);
        tag.set("height", height);
        tag.set("depth", depth);
        Utils.let(new BinaryTag[entities.size()], t -> {
            for (int i = 0; i < t.length; i++) {
                t[i] = new BinaryTag();
            }
            int i = 0;
            for (var entity : entities.values()) {
                entity.save(t[i]);
                i++;
            }
            tag.set("entities", t);
        });
        Utils.let(new BinaryTag(), t -> {
            for (var e : Registry.BLOCK) {
                t.set(e.getKey().toString(), e.getValue().getRawId());
            }
            tag.set("blocksMap", t);
        });
        Utils.let(new int[blocks.length], v -> {
            for (int i = 0; i < blocks.length; i++) {
                v[i] = blocks[i].getRawId();
            }
            tag.set("blocks", v);
        });
    }

    public void deserialize(IBinaryTag tag) throws IOException {
        long v = tag.get(LONG, "version");
        if (GameVersion.worldVersion() != v) {
            throw new RuntimeException("Doesn't compatible with version " + v + ". Current is " + GameVersion.worldVersion());
        }
        final int w = tag.get(INT, "width");
        final int h = tag.get(INT, "height");
        final int d = tag.get(INT, "depth");
        Utils.let(tag.get(TAG_ARRAY, "entities"), entities -> {
            for (var t : entities) {
                var type = Registry.ENTITY.getById(new Identifier(t.get(STRING, "type")));
                if (type != null) {
                    var entity = type.createEntity(this);
                    var uuid = UUID.fromString(t.get(STRING, "uuid"));
                    entity.setUUID(uuid);
                    this.entities.put(uuid, entity);
                    if (entity instanceof PlayerEntity) {
                        // TODO: Adapt every entity and add player to entities
                        var fixer = type.worldFixer();
                        if (fixer != null) {
                            fixer.adapt("version", t.getGeneric("version"));
                        }
                    }
                    entity.load(t);
                }
            }
        });
        var blocksMap = new LinkedHashMap<Integer, Identifier>();
        Utils.let(tag.get(TAG, "blocksMap"), t -> {
            for (var e : t.getMappings().entrySet()) {
                blocksMap.put((int) e.getValue().value(), new Identifier(e.getKey()));
            }
        });
        Arrays.fill(blocks, AIR);
        int[] rawBlocks = tag.get(INT_ARRAY, "blocks");
        for (int i = 0, sz = w * h * d; i < sz; i++) {
            blocks[i] = Registry.BLOCK.getById(blocksMap.get(rawBlocks[i]));
        }
    }
}
