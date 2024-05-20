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

//    public static void main(String[] args) {
//        ImageLoader loader = new ImageLoader();
//        BufferedImage img = loader.loadImage("/carrot.png");
//        if (img != null) {
//            System.out.println("Изображение успешно загружено!");
//        }
//    }
}

