package main.java.gui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class ImageLoader {
    public static BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try {
            URL url = ImageLoader.class.getResource(path);
            if (url != null) {
                image = ImageIO.read(url);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return image;
    }
}

