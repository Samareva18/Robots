package main.java.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DrawMaze extends JPanel {

    private volatile double m_robotPositionX;
    private volatile double m_robotPositionY;

    public static int[][] maze = {
            {1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {1, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1},
            {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1},
            {1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1},
            {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1},
            {1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1},
            {1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1},
            {0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0},
            {1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1},
            {1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1}
    };

    int countOfCellsInWidth = maze[0].length;
    int countOfCellsInHeight = maze.length;
    int cellWidth;
    int cellHeight;
    int gameCount = 0;


    public BufferedImage grassImage = ImageLoader.loadImage("/grass.png");
    public BufferedImage pathImage = ImageLoader.loadImage("/img.png");
    public BufferedImage carrotImage = ImageLoader.loadImage("/carrot.png");

    public DrawMaze(float windowHeight, float windowWidth, double posX, double posY) {
        this.m_robotPositionX = posX;
        this.m_robotPositionY = posY;
        this.cellWidth = round(windowWidth / countOfCellsInWidth);
        this.cellHeight = round(windowHeight / countOfCellsInHeight);

    }

    public void drawMaze(Graphics g) {

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 1) {
                    if (grassImage != null) {
                        g.drawImage(grassImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight, this);
                    }
                } else {
                    if (pathImage != null) {
                        g.drawImage(pathImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight, this);
                        drawCarrot(g, carrotImage, i, j);
                    }
                }
                drawFinish(g);
            }
        }
    }

    private void drawFinish(Graphics g) {
        BufferedImage img = loadImage("C:\\Users\\user\\git\\Robots\\OOPRobots\\robots\\src\\main\\java\\resources\\finish.png");
        g.drawImage(img, 550, 460, 30, 30, this);
    }


    private void drawCarrot(Graphics g, BufferedImage carrotImage, int i, int j) {

        if (!isPrizeCollected(cellWidth, cellHeight, j * cellWidth + 5, i * cellHeight + 5)) {
            if (maze[i][j] == 0 && (i % 7 == 0 || j % 5 == 0 || i % 4 == 0) && (i + j) % 3 == 0) {
                g.drawImage(carrotImage, j * cellWidth + 5, i * cellHeight + 5, 30, 30, this);
            }
        } else {
            maze[i][j] = -1; // приз собран
            gameCount += 1;
            drawGameCount(g);
        }
    }


    private boolean isPrizeCollected(int cellWidth, int cellHeight, int prizeX, int prizeY) {
        return m_robotPositionX >= prizeX && m_robotPositionX <= prizeX + cellWidth && m_robotPositionY >= prizeY
                && m_robotPositionY <= prizeY + cellHeight;
    }


    private void drawGameCount(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Счет: " + gameCount, 20, 25);
    }

    private BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

}
