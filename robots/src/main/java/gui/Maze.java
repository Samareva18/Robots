package main.java.gui;

import javax.swing.*;
import java.awt.*;

public class Maze extends JPanel {

    private float windowHeight;
    private float windowWidth;
    public Maze(float height, float width){
        this.windowHeight = height;
        this.windowWidth = width;
    }
    public int[][] maze = {                          //TODO вынести в отдельный класс + функция для генерации лабиринта
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1},
            {1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0},
            {1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1},
            {1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//
//        float windowHeight = getHeight();
//        float windowWidth = getWidth();
        int countOfCellsInWidth = maze[0].length;
        int countOfCellsInHeight = maze.length;
        int cellWidth = round(windowWidth / countOfCellsInWidth);
        int cellHeight = round(windowHeight / countOfCellsInHeight);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 1) {
                    g.setColor(Color.PINK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
            }
        }

    }

}
