package io.github.overrun.mc2d.image;

import io.github.overrun.mc2d.Mc2D;
import io.github.overrun.mc2d.asset.AssetManager;
import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.util.Identifier;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.util.HashMap;

/**
 * @author squid233
 */
public class Images {
    public static final HashMap<Character, Image> ASCII_IMAGE_MAP = new HashMap<>();

    static {
        puts();
    }

    public static Image getBlockTexture(Block block) {
        return new ImageIcon(AssetManager.getAsString(new Identifier(block.getModel().getProperty("texture")).getNamespace(),
                "textures", block.getModel().getProperty("texture") + ".png")).getImage();
    }

    public static Image getImagePart(Image img, int x, int y, int width, int height) {
        return Mc2D.getClient().createImage(new FilteredImageSource(img.getSource(), new CropImageFilter(x, y, width, height)));
    }

    public static Image getAsciiPart(int x, int y) {
        return getImagePart(new ImageIcon(AssetManager.getAsString(Mc2D.NAMESPACE, "textures", "font", "ascii.png")).getImage(), x, y, 7, 7);
    }

    public static Image getAsciiInMap(char symbol) {
        return ASCII_IMAGE_MAP.get(symbol);
    }

    private static void puts() {
        int rows = 17, cols = 16, code = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                ASCII_IMAGE_MAP.put((char) code, getAsciiPart(j * 8, i * 8));
                code++;
            }
        }
    }
}
