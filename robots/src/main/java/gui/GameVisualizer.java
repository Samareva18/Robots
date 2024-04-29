package main.java.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel {

    private int[][] maze = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        float windowHeight = getHeight();
        float windowWidth = getWidth();
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
    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public GameVisualizer() {
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        moveRobot(0, -1);
                        m_robotDirection = -Math.PI/2;
                        break;
                    case KeyEvent.VK_DOWN:
                        moveRobot(0, 1);
                        m_robotDirection = Math.PI/2;
                        break;
                    case KeyEvent.VK_LEFT:
                        moveRobot(-1, 0);
                        m_robotDirection = Math.PI;
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRobot(1, 0);
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

    protected void setTargetPosition(Point p) {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    //метод обеспечивает движение робота к целевой позиции
    //с учетом его текущего положения, скорости и направления
    protected void onModelUpdateEvent() {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection) {
            angularVelocity = -maxAngularVelocity;
        }

        //moveRobot(1, 1);
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }


//    private void moveRobot(double velocity, double angularVelocity, double duration) {
//        velocity = applyLimits(velocity, 0, maxVelocity);
//        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
//        double newX = m_robotPositionX + velocity / angularVelocity *
//                (Math.sin(m_robotDirection + angularVelocity * duration) -
//                        Math.sin(m_robotDirection));
//        if (!Double.isFinite(newX)) {
//            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
//        }
//        double newY = m_robotPositionY - velocity / angularVelocity *
//                (Math.cos(m_robotDirection + angularVelocity * duration) -
//                        Math.cos(m_robotDirection));
//        if (!Double.isFinite(newY)) {
//            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
//        }
//        m_robotPositionX = newX;
//        m_robotPositionY = newY;
//        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
//        m_robotDirection = newDirection;
//    }


    private void moveRobot(double dx, double dy){
        m_robotPositionX += dx;
        m_robotPositionY += dy;
    }


    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, m_robotDirection);
        drawTarget(g2d, m_targetPositionX, m_targetPositionY);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, double direction) {
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

            AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
            g.setTransform(t);
            g.setColor(Color.MAGENTA);
            fillOval(g, robotCenterX, robotCenterY, 30, 10);
            g.setColor(Color.BLACK);
            drawOval(g, robotCenterX, robotCenterY, 30, 10);
            g.setColor(Color.WHITE);
            fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
            g.setColor(Color.BLACK);
            drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        }
    }


    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
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
