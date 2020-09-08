package io.github.overrun.mc2d.api.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author squid233
 */
public class ReadFile {
    public static String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        try (Scanner sc = new Scanner(file)) {
            String tmp;
            while (sc.hasNextLine()) {
                tmp = sc.nextLine();
                sb.append(tmp).append('\n');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return sb.length() != 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    public static String readFile(String fileName) {
        return readFile(new File(fileName));
    }
}
