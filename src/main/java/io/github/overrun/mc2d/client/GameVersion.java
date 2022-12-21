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

package io.github.overrun.mc2d.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.overrun.swgl.core.util.LogFactory9;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * The game version info.
 *
 * @author squid233
 * @since 0.6.0
 */
public final class GameVersion {
    private static final Logger logger = LogFactory9.getLogger();
    private static final GameVersion INSTANCE;
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(GameVersion.class, new Serializer()).create();
    private String versionString = null;
    private int versionNumber = 0;
    private long worldVersion = 0L;
    private int protocolVersion = 0;

    /**
     * The serializer.
     *
     * @author squid233
     * @since 0.6.0
     */
    private static final class Serializer extends TypeAdapter<GameVersion> {
        @Override
        public void write(JsonWriter out, GameVersion value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public GameVersion read(JsonReader in) throws IOException {
            var ver = new GameVersion();
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "versionString" -> ver.versionString = in.nextString();
                    case "versionNumber" -> ver.versionNumber = in.nextInt();
                    case "worldVersion" -> ver.worldVersion = in.nextLong();
                    case "protocolVersion" -> ver.protocolVersion = in.nextInt();
                }
            }
            in.endObject();
            return ver;
        }
    }

    static {
        GameVersion version;
        try (var reader = new InputStreamReader(
            Objects.requireNonNull(GameVersion.class.getClassLoader().getResourceAsStream("version.json")),
            StandardCharsets.UTF_8)) {
            version = GSON.fromJson(reader, GameVersion.class);
        } catch (Exception e) {
            logger.error("Error reading game version", e);
            version = new GameVersion();
        }
        INSTANCE = version;
    }

    /**
     * Gets the version string.
     *
     * @return the version string
     */
    public static String versionString() {
        return INSTANCE.versionString;
    }

    /**
     * Gets the version number.
     *
     * @return The version number, negative for snapshot; otherwise release.
     */
    public static int versionNumber() {
        return INSTANCE.versionNumber;
    }

    /**
     * Gets the world version.
     *
     * @return the world version
     */
    public static long worldVersion() {
        return INSTANCE.worldVersion;
    }

    /**
     * Gets the protocol version.
     *
     * @return the protocol version
     */
    public static int protocolVersion() {
        return INSTANCE.protocolVersion;
    }
}
