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

package io.github.overrun.mc2d.mod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author squid233
 * @since 2021/01/27
 */
public final class ModLoader {
    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(ModInfoFile.class, new ModInfoFile.Serializer())
        .create();
    private static final Map<String, ModInstance> INSTANCES = new LinkedHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger("Minecraft2D ModLoader");

    /**
     * Load all mods from "mods" directory.
     *
     * @throws RuntimeException If an error is occurred on loading mod
     */
    public static void loadMods() throws IllegalArgumentException {
        var dir = new File("mods");
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdir();
        }
        var files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (var file : files) {
                var filename = file.getName();
                var isModFile = file.isFile() &&
                                (filename.endsWith(".jar") || filename.endsWith(".zip"));
                if (isModFile) {
                    String namespace = null, name, version, main;
                    try {
                        var loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
                        var is = loader.getResourceAsStream("mc2d.mod.json");
                        if (is != null) {
                            try (var reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                                var modInfoFile = GSON.fromJson(reader, ModInfoFile.class);
                                namespace = modInfoFile.getNamespace();
                                if (namespace == null) {
                                    throw new RuntimeException("Mod namespace is null; this is not allowed!");
                                }
                                name = Objects.requireNonNull(modInfoFile.getName(), namespace);
                                version = Objects.requireNonNull(modInfoFile.getVersion(), "0.0.0");
                                main = modInfoFile.getMain();
                                try {
                                    var clazz = loader.loadClass(main);
                                    var modInitializer = (ModInitializer) clazz.getDeclaredConstructor().newInstance();
                                    var instance = new ModInstance(modInitializer, loader, name, version);
                                    INSTANCES.put(namespace, instance);
                                    try {
                                        for (Field field : clazz.getDeclaredFields()) {
                                            if (field.getDeclaredAnnotation(Mod.Instance.class) != null) {
                                                field.set(modInitializer, instance);
                                            }
                                        }
                                        clazz.getDeclaredConstructor().setAccessible(false);
                                    } catch (SecurityException | IllegalArgumentException | IllegalAccessException |
                                             NoSuchMethodException ignored) {
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException("Could not execute entrypoint due to errors, provided by '" + namespace + "'!", e);
                                }
                            } catch (IOException e) {
                                logger.error("Catching loading mod", e);
                            }
                        }
                    } catch (MalformedURLException e) {
                        logger.warn("Error while loading mod", e);
                    }
                    if (namespace == null || namespace.isEmpty() || namespace.isBlank()) {
                        throw new RuntimeException("Mod namespace is null or empty; this is not allowed!");
                    }
                    try {
                        for (var instance : INSTANCES.values()) {
                            logger.info("Loading mod {}@{}!", instance.name(), instance.version());
                            instance.initializer().onInitialize();
                        }
                    } catch (Throwable t) {
                        throw new RuntimeException("Could not execute entrypoint due to errors, provided by '" + namespace + "'!", t);
                    }
                }
            }
        }
    }

    /**
     * Get the {@link ClassLoader} of the mod.
     *
     * @param namespace The namespace of the mod.
     * @return The ClassLoader.
     */
    public static URLClassLoader getLoader(String namespace) {
        return INSTANCES.get(namespace).classLoader();
    }

    /**
     * Get the mod's friendly name.
     *
     * @param namespace The namespace of the mod.
     * @return The mod's friendly name.
     */
    public static String getName(String namespace) {
        return INSTANCES.get(namespace).name();
    }

    public static String getVersion(String namespace) {
        return INSTANCES.get(namespace).version();
    }

    /**
     * Get loaded mod count.
     *
     * @return Mod count.
     */
    public static int getModCount() {
        return INSTANCES.size();
    }

    public static Map<String, ModInstance> getMods() {
        return INSTANCES;
    }
}
