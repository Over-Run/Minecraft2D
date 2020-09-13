package io.github.overrun.mc2d.client;

import io.github.overrun.mc2d.Mc2D;
import io.github.overrun.mc2d.api.util.UtilSystem;
import io.github.overrun.mc2d.block.BlockPos;
import io.github.overrun.mc2d.engine.RenderThread;
import io.github.overrun.mc2d.entity.Player;
import io.github.overrun.mc2d.image.Images;
import io.github.overrun.mc2d.init.Blocks;
import io.github.overrun.mc2d.input.Input;
import io.github.overrun.mc2d.input.Mouse;
import io.github.overrun.mc2d.screen.Screen;
import io.github.overrun.mc2d.text.LiteralText;
import io.github.overrun.mc2d.util.map.StorageBlock;
import io.github.overrun.mc2d.world.Dimensions;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @author Administrator
 */
public class Client extends JFrame implements Screen {
    private static boolean initialized;

    public Client() {
        super("Minecraft 2D " + Mc2D.VERSION + " [Esc:Quit Game] - Made by OverRun Organization");
        if (!initialized) {
            initialized = true;
            setSize(UtilSystem.getPropertyInt("width", 864), UtilSystem.getPropertyInt("height", 486));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setIconImage(new ImageIcon("icon.png").getImage());
            addKeyListener(new Input());
            Mouse mouse = new Mouse();
            addMouseListener(mouse);
            addMouseWheelListener(mouse);
            //////////
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/save.dat"))) {
                Dimensions.OVERWORLD.setStorageBlocks((StorageBlock) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 256; j++) {
                        Dimensions.OVERWORLD.setBlock(new BlockPos(j, i), Blocks.COBBLESTONE);
                    }
                }
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 256; j++) {
                        Dimensions.OVERWORLD.setBlock(new BlockPos(j, i + 4), Blocks.DIRT);
                    }
                }
                for (int i = 0; i < 256; i++) {
                    Dimensions.OVERWORLD.setBlock(new BlockPos(i, 8), Blocks.GRASS_BLOCK);
                }
            }
            //////////
            System.out.println("Max memory: " + (Runtime.getRuntime().maxMemory() >> 10 >> 10) + "MB");
            new RenderThread().start();
            setVisible(true);
        }
    }

    public void drawImage(Graphics g, Image img, int x, int y) {
        g.drawImage(img, x, y, null);
    }

    public void drawImage(Graphics g, Image img, int x, int y, int width, int height) {
        g.drawImage(img, x, y, width ,height, null);
    }

    @Override
    public void paint(Graphics g) {
        Image buf = createImage(getWidth(), getHeight());
        Graphics gg = buf.getGraphics();
        //////////
        drawBackground(gg, Color.decode("#66CCFf"));
        //////////
        Dimensions.OVERWORLD.getStorageBlocks().drawBlocks(gg);
        //////////
        drawText(gg, 9, 31, new LiteralText(Blocks.getByRawId(Player.handledBlock).getRegistryName().toString()));
        gg.drawRect(getWidth() - 42, 31, 32, 32);
        drawImage(gg, Images.getBlockTexture(Blocks.getByRawId(Player.handledBlock)), getWidth() - 40, 33, 29, 29);
        //////////
        drawImage(g, buf, 0, 0);
    }
}
