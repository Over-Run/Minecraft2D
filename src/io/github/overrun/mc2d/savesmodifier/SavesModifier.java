package io.github.overrun.mc2d.savesmodifier;

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.util.map.StorageBlock;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * @author squid233
 */
public class SavesModifier {
    private static final Scanner SC = new Scanner(System.in);
    public static void main(String[] args) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/save.dat"));
             ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/save.dat"))) {
            switch (modifier()) {
                case 1:
                    StorageBlock sb = (StorageBlock) ois.readObject();
                    System.out.println("Please enter world new height:");
                    int height = SC.nextInt();
                    System.out.println("Please enter world new width:");
                    int width = SC.nextInt();
                    sb.blocks = new Block[height][width];
                    oos.writeObject(sb);
                    break;
                case 2:
                    break;
                default:
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            /*System.out.println("Please put your save.dat to saves directory or enter the file path:");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SC.nextLine()));
                 ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SC.nextLine()))) {
                switch (modifier()) {
                    case 1:
                        StorageBlock sb = (StorageBlock) ois.readObject();
                        System.out.println("Please enter world new height:");
                        int height = SC.nextInt();
                        System.out.println("Please enter world new width:");
                        int width = SC.nextInt();
                        sb.blocks = new Block[height][width];
                        oos.writeObject(sb);
                        break;
                    case 2:
                        break;
                    default:
                }
            } catch (IOException | ClassNotFoundException ee) {
                ee.printStackTrace();
            }*/
        }
    }

    public static int modifier() {
        System.out.println("┌--------------------------------------┐");
        System.out.println("├---------Please choose a mode---------┤");
        System.out.println("├---------------------┬----------------┤");
        System.out.println("│ 1.change world size │ 2.change block │");
        System.out.println("└---------------------┴----------------┘");
        return SC.nextInt();
    }
}
