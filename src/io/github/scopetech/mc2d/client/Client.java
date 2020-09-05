package io.github.scopetech.mc2d.client;

import io.github.scopetech.mc2d.Mc2D;
import io.github.scopetech.mc2d.api.mod.Mod;
import io.github.scopetech.mc2d.api.util.UtilSystem;
import io.github.scopetech.mc2d.engine.RenderThread;
import io.github.scopetech.mc2d.input.Input;
import io.github.scopetech.mc2d.input.Mouse;

import javax.swing.*;
import java.awt.*;

/**
 * @author Administrator
 */
@Mod(modid = Mc2D.NAMESPACE)
public class Client extends JFrame {
    private static boolean initialized;

    public Client() {
        super("Minecraft 2D " + Mc2D.VERSION);
        if (!initialized) {
            initialized = true;
            setSize(UtilSystem.getPropertyInt("width", 864), UtilSystem.getPropertyInt("height", 486));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            addKeyListener(new Input());
            addMouseListener(new Mouse());
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

        drawImage(g, buf, 0, 0);
    }
}
