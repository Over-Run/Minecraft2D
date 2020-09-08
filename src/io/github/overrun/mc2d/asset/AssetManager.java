package io.github.overrun.mc2d.asset;

import io.github.overrun.mc2d.api.io.ReadFile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author squid233
 */
public class AssetManager {
    public static Path getAssets(String namespace, String... more) {
        return Paths.get("assets/" + namespace, more);
    }

    public static String getAsString(String namespace, String... more) {
        return getAssets(namespace, more).toString();
    }

    public static void copyToFile(Path source, Path target, boolean overwrite, boolean ifAbsent) {
        if (overwrite) {
            copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } else {
            if (ifAbsent && !source.toFile().exists()) {
                copy(source, target);
            } else {
                try (Writer w = new FileWriter(target.toFile(), true)) {
                    w.write(ReadFile.readFile(source.toFile()));
                    w.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void copy(Path source, Path target, CopyOption... copyOptions) {
        try {
            Files.copy(source, target, copyOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
