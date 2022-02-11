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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author squid233
 * @since 2021/01/27
 */
public final class ModLoader {
    private static final Map<String, ModInitializer> MODS = new LinkedHashMap<>();
    private static final Map<String, ClassLoader> MOD_LOADERS = new LinkedHashMap<>();
    private static final Map<String, String> MODS_NAME = new LinkedHashMap<>();
    private static final Map<String, String> MODS_VERSION = new LinkedHashMap<>();
    private static final Logger logger = LogManager.getLogger("Minecraft2D ModLoader");

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
                    String modid = null, name, version, main;
                    try {
                        var loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
                        var is = loader.getResourceAsStream("mc2d.mod.prop");
                        if (is != null) {
                            try (is) {
                                var prop = new Properties(4);
                                prop.load(is);
                                modid = prop.getProperty("modid");
                                if (modid == null) {
                                    throw new RuntimeException("Mod ID is null; this is not allowed!");
                                }
                                name = prop.getProperty("name", modid);
                                version = prop.getProperty("version", "0.0.0");
                                main = prop.getProperty("main");
                                try {
                                    var clazz = Class.forName(main, true, loader);
                                    MOD_LOADERS.put(modid, loader);
                                    MODS_NAME.put(modid, name);
                                    MODS_VERSION.put(modid, version);
                                    try {
                                        var modInitializer = (ModInitializer) clazz.getDeclaredConstructor().newInstance();
                                        MODS.put(modid, modInitializer);
                                        try {
                                            for (Field field : clazz.getFields()) {
                                                if (field.getDeclaredAnnotation(Mod.Instance.class) != null) {
                                                    field.set(modInitializer, modInitializer);
                                                }
                                                if (field.getDeclaredAnnotation(Mod.Modid.class) != null) {
                                                    field.set(modInitializer, modid);
                                                }
                                                if (field.getDeclaredAnnotation(Mod.Name.class) != null) {
                                                    field.set(modInitializer, name);
                                                }
                                                if (field.getDeclaredAnnotation(Mod.Version.class) != null) {
                                                    field.set(modInitializer, version);
                                                }
                                            }
                                            clazz.getDeclaredConstructor().setAccessible(false);
                                        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException ignored) { }
                                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                                        try {
                                            clazz.getMethod(main.split("@", 2)[1]).invoke(null);
                                        } catch (ArrayIndexOutOfBoundsException | NoSuchMethodException ee) {
                                            var field = clazz.getField(main.split("#", 2)[1]);
                                            field.getType().getMethod("onInitialize").invoke(field.get(null));
                                        }
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException("Could not execute entrypoint due to errors, provided by '" + modid + "'!");
                                }
                            } catch (IOException e) {
                                logger.catching(e);
                            }
                        }
                    } catch (MalformedURLException e) {
                        logger.warn("Error while loading mod: {}", e.getMessage());
                        logger.catching(Level.WARN, e);
                    }
                    if (modid == null || modid.isEmpty() || modid.isBlank()) {
                        throw new RuntimeException("Mod ID is null or empty; this is not allowed!");
                    }
                    try {
                        if (!MODS.isEmpty()) {
                            for (var initializer : MODS.values()) {
                                logger.info("Loading mod {}@{}!", getName(modid), getVersion(modid));
                                initializer.onInitialize();
                            }
                        }
                    } catch (Throwable t) {
                        throw new RuntimeException("Could not execute entrypoint due to errors, provided by '" + modid + "'!");
                    }
                }
            }
        }
    }

    /**
     * Get the mod's {@link ClassLoader}.
     *
     * @param modid The mod identifier.
     * @return The ClassLoader.
     */
    public static ClassLoader getLoader(String modid) {
        return MOD_LOADERS.get(modid);
    }

    /**
     * Get the mod's friendly name.
     *
     * @param modid The mod identifier.
     * @return The mod's friendly name.
     */
    public static String getName(String modid) {
        return MODS_NAME.get(modid);
    }

    public static String getVersion(String modid) {
        return MODS_VERSION.get(modid);
    }

    /**
     * Get loaded mod count.
     *
     * @return Mod count.
     */
    public static int getModCount() {
        return MODS.size();
    }

    public static Map<String, ModInitializer> getMods() {
        return Map.copyOf(MODS);
    }
}
