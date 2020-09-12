package io.github.overrun.mc2d.client;

import io.github.overrun.mc2d.Mc2D;
import io.github.overrun.mc2d.api.util.UtilSystem;
import io.github.overrun.mc2d.block.BlockPos;
import io.github.overrun.mc2d.engine.RenderThread;
import io.github.overrun.mc2d.init.Blocks;
import io.github.overrun.mc2d.input.Input;
import io.github.overrun.mc2d.input.Mouse;
import io.github.overrun.mc2d.screen.Screen;
import io.github.overrun.mc2d.world.Dimensions;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

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
            addMouseListener(new Mouse());
            //////////
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 53; j++) {
                    Dimensions.OVERWORLD.setBlock(new BlockPos(j, i), Blocks.DIRT);
                }
            }
            for (int i = 0; i < 53; i++) {
                Dimensions.OVERWORLD.setBlock(new BlockPos(i, 4), Blocks.GRASS_BLOCK);
            }
            //////////
            new RenderThread().start();
            setVisible(true);
        }
    }

    public void drawImage(Graphics g, Image img, int x, int y) {
        g.drawImage(img, x, y, null);
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
        drawImage(g, buf, 0, 0);
    }

    @Override
    public int width() {
        return getWidth();
    }

    @Override
    public int height() {
        return getHeight();
    }
}
