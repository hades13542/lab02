/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lab02;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Adam
 */
public class Main {

    public static void main(String[] args) {
        try {
            BufferedImage in = ImageIO.read(new File("01.jpg"));
            
            Lab02.run(in);

        } catch (IOException e) {
            System.out.println("Coś poszło nie tak!");
        }
    }

    

}
