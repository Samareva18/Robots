package main.java.gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;


public class GameVisualizer extends JPanel {

    private final int[][] maze = MazeDrawer.maze;

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    private volatile double m_robotPositionX = 70;
    private volatile double m_robotPositionY = 70;
    private volatile double m_robotDirection = 0;
    int robotSize = 30;
    int halfRobotSize = robotSize / 2;
    private final BufferedImage m_characterImage = ImageLoader.loadImage("/rabbit1.png");
    private int gameCount = 0;


    public GameVisualizer() {
        Timer m_timer = initTimer();
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //onModelUpdateEvent();
            }
        }, 0, 50);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        moveRobot(0, -5);
                        m_robotDirection = -Math.PI / 2;
                        break;
                    case KeyEvent.VK_DOWN:
                        moveRobot(0, 5);
                        m_robotDirection = Math.PI / 2;
                        break;
                    case KeyEvent.VK_LEFT:
                        moveRobot(-5, 0);
                        m_robotDirection = Math.PI;
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRobot(5, 0);
                        m_robotDirection = 0;
                        break;
                }
                repaint();
            }

        });

        setDoubleBuffered(true);
        setFocusable(true);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        float windowHeight = getHeight();
        float windowWidth = getWidth();

        MazeDrawer m = new MazeDrawer(windowHeight, windowWidth, m_robotPositionX, m_robotPositionY);
        m.drawMaze(g);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobotInBounds(g2d, m_robotDirection);
        drawRobotWithinPath(g2d, m_robotDirection);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }


    private void moveRobot(double dx, double dy) {
        m_robotPositionX += dx;
        m_robotPositionY += dy;
    }


    private static int round(double value) {
        return (int) (value + 0.5);
    }


    private void drawRobot(Graphics2D g, double direction) {

        int characterCenterX = round(m_robotPositionX);
        int characterCenterY = round(m_robotPositionY);

        AffineTransform t = AffineTransform.getRotateInstance(direction, characterCenterX, characterCenterY);
        g.setTransform(t);

        if (m_characterImage != null) {
            g.drawImage(m_characterImage, characterCenterX - m_characterImage.getWidth(null) / 2,
                    characterCenterY - m_characterImage.getHeight(null) / 2, null);

        }
    }


    private void drawRobotInBounds(Graphics2D g, double direction) {
        int robotCenterX = round(m_robotPositionX);
        int robotCenterY = round(m_robotPositionY);

        int robotSize = 30;
        int halfRobotSize = robotSize / 2;

        // Проверяем, не выходит ли робот за границы поля
        int windowHeight = getHeight();
        int windowWidth = getWidth();

        if (robotCenterX - halfRobotSize < 0) { //выход слева
            m_robotPositionX = halfRobotSize;
        } else if (robotCenterX + halfRobotSize > windowWidth) { //выход справа
            m_robotPositionX = windowWidth - halfRobotSize;
        }

        if (robotCenterY - halfRobotSize < 0) { //сверху
            m_robotPositionY = robotSize;
        } else if (robotCenterY + halfRobotSize > windowHeight) { //снизу
            m_robotPositionY = windowHeight - robotSize;
        } else {
            drawRobot(g, direction);
        }
    }

    private void drawRobotWithinPath(Graphics2D g, double direction) {

        int robotCenterX = round(m_robotPositionX);
        int robotCenterY = round(m_robotPositionY);

        int countOfCellsInWidth = maze[0].length;
        int countOfCellsInHeight = maze.length;

        float windowHeight = getHeight();
        float windowWidth = getWidth();

        int cellWidth = round(windowWidth / countOfCellsInWidth);
        int cellHeight = round(windowHeight / countOfCellsInHeight);

        int i1 = robotCenterY / cellHeight;
        int j1 = (robotCenterX + halfRobotSize) / cellWidth;

        int i2 = robotCenterY / cellHeight;
        int j2 = (robotCenterX - halfRobotSize) / cellWidth;

        int i3 = (robotCenterY - halfRobotSize) / cellHeight;
        int j3 = robotCenterX / cellWidth;

        int i4 = (robotCenterY + halfRobotSize) / cellHeight;
        int j4 = robotCenterX / cellWidth;


        if (maze[i1][j1] == 1 && direction == 0) { //вправо
            moveRobot(-5, 0);
        }
        if (maze[i2][j2] == 1 && direction == Math.PI) { //влево
            moveRobot(5, 0);
        }
        if (maze[i3][j3] == 1 && direction == -Math.PI / 2) { //вверх
            moveRobot(0, 5);
        }
        if (maze[i4][j4] == 1 && direction == Math.PI / 2) { //вниз
            moveRobot(0, -5);
        }

    }

    public double getM_robotPositionX() {
        return m_robotPositionX;
    }

    public double getM_robotPositionY() {
        return m_robotPositionY;
    }

    public void setM_robotPositionX(double m_robotPositionX) {
        this.m_robotPositionX = m_robotPositionX;
    }

    public void setM_robotPositionY(double m_robotPositionY) {
        this.m_robotPositionY = m_robotPositionY;
    }

}
