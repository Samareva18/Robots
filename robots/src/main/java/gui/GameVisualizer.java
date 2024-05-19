package main.java.gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.*;

import main.java.gui.MazeGenerator;
import main.java.gui.Maze;

import static main.java.gui.MazeGenerator.generateMaze;

public class GameVisualizer extends JPanel {

    private int[][] maze = {                          //TODO вынести в отдельный класс + функция для генерации лабиринта
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

    //private int[][] maze = generateMaze(14, 14);


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        float windowHeight = getHeight();
        float windowWidth = getWidth();
        int countOfCellsInWidth = maze[0].length;
        int countOfCellsInHeight = maze.length;
        int cellWidth = round(windowWidth / countOfCellsInWidth);
        int cellHeight = round(windowHeight / countOfCellsInHeight);

        BufferedImage grassImage = loadImage("C:\\Users\\user\\git\\Robots\\OOPRobots\\robots\\src\\main\\java\\resources\\grass.png");
        BufferedImage pathImage = loadImage("C:\\Users\\user\\git\\Robots\\OOPRobots\\robots\\src\\main\\java\\resources\\img.png");
        BufferedImage carrotImage = loadImage("C:\\Users\\user\\git\\Robots\\OOPRobots\\robots\\src\\main\\java\\resources\\carrot.png");

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 1) {
                    g.setColor(Color.PINK);
                    g.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);

                    if (grassImage != null) {
                        g.drawImage(grassImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight, this);
                    }
                } else {
                    g.setColor(Color.YELLOW);
                    g.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);

                    if (pathImage != null) {
                        g.drawImage(pathImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight, this);
                    }
                }

                drawCarrot(g, carrotImage);
                drawFinish(g);

            }
        }
    }



    //TODO переделать
    private void drawFinish(Graphics g) {
        BufferedImage img = loadImage("C:\\Users\\user\\git\\Robots\\OOPRobots\\robots\\src\\main\\java\\resources\\finish.png");
        g.drawImage(img, 550, 460, 30, 30, this);
    }


    private void drawCarrot(Graphics g, BufferedImage carrotImage) {
        float windowHeight = getHeight();
        float windowWidth = getWidth();
        int countOfCellsInWidth = maze[0].length;
        int countOfCellsInHeight = maze.length;
        int cellWidth = round(windowWidth / countOfCellsInWidth);
        int cellHeight = round(windowHeight / countOfCellsInHeight);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (!isPrizeCollected(cellWidth, cellHeight, j * cellWidth + 5, i * cellHeight + 5)) {
                    if (maze[i][j] == 0 && (i % 7 == 0 || j % 5 == 0 || i % 4 == 0) && (i + j) % 3 == 0) {
                        g.drawImage(carrotImage, j * cellWidth + 5, i * cellHeight + 5, 30, 30, this);
                    }
                } else {
                    maze[i][j] = -1; // приз собран
                    //gameCount++;
                    drawGameCount(g);
                }
            }
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


    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;

    int robotSize = 30;
    int halfRobotSize = robotSize / 2;

    private BufferedImage m_characterImage;
    private BufferedImage backgroundImage;
    private int gameCount = 0;


//    private Maze m_maze;


    public GameVisualizer() {
//        m_maze = new Maze(700, 700);
//        add(m_maze);
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
        }, 0, 10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //setTargetPosition(e.getPoint());
                repaint();
            }
        });

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

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

        });

        setDoubleBuffered(true); // для более плавного отображения графики
        setFocusable(true);
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

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobotInBounds(g2d, m_robotDirection);
        drawRobotWithinPath(g2d, m_robotDirection);
    }


    private void drawRobot(Graphics2D g, double direction) {

        loadCharacterImage();
        int characterCenterX = round(m_robotPositionX);
        int characterCenterY = round(m_robotPositionY);

        AffineTransform t = AffineTransform.getRotateInstance(direction, characterCenterX, characterCenterY);
        g.setTransform(t);

        if (m_characterImage != null) {
            g.drawImage(m_characterImage, characterCenterX - m_characterImage.getWidth(null) / 2,
                    characterCenterY - m_characterImage.getHeight(null) / 2, null);

        }
    }

    private void loadCharacterImage() {
        try {
            m_characterImage = ImageIO.read(new File("C:\\Users\\user\\git\\Robots\\OOPRobots\\robots\\src\\main\\java\\resources\\rabbit1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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


//    private void drawTarget(Graphics2D g, int x, int y) {
//        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
//        g.setTransform(t);
//        g.setColor(Color.GREEN);
//        fillOval(g, x, y, 5, 5);
//        g.setColor(Color.BLACK);
//        drawOval(g, x, y, 5, 5);
//    }


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
