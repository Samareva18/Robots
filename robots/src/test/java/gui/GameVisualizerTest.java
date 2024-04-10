package test.java.gui;

import main.java.gui.GameVisualizer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;



public class GameVisualizerTest {


    @Test
    public void testDrawRobotWithinBounds() {
        GameVisualizer gv = new GameVisualizer();
        int windowHeight = 800;
        int windowWidth = 800;
        gv.setSize(windowWidth, windowHeight);
        BufferedImage image = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        gv.setM_robotPositionX(100);
        gv.setM_robotPositionY(100);
        int robotCenterX = (int) (0.5+gv.getM_robotPositionX());
        int robotCenterY = (int) (0.5+gv.getM_robotPositionY());

        gv.paint(g); //здесь вызывается приватный метод drawRobot, который нужно проверить

        assertEquals(robotCenterX, gv.getM_robotPositionX(), 1);
        assertEquals(robotCenterY, gv.getM_robotPositionY(), 1);

        gv.setM_robotPositionX(400);
        gv.setM_robotPositionY(500);
        robotCenterX = (int) (0.5+gv.getM_robotPositionX());
        robotCenterY = (int) (0.5+gv.getM_robotPositionY());

        gv.paint(g);

        assertEquals(robotCenterX, gv.getM_robotPositionX(), 1);
        assertEquals(robotCenterY, gv.getM_robotPositionY(), 1);

        gv.setM_robotPositionX(700);
        gv.setM_robotPositionY(200);
        robotCenterX = (int) (0.5+gv.getM_robotPositionX());
        robotCenterY = (int) (0.5+gv.getM_robotPositionY());

        gv.paint(g);

        assertEquals(robotCenterX, gv.getM_robotPositionX(), 1);
        assertEquals(robotCenterY, gv.getM_robotPositionY(), 1);

    }

    @Test
    public void testDrawRobotOutOfBounds() {
        GameVisualizer gv = new GameVisualizer();
        int windowHeight = 800;
        int windowWidth = 800;
        gv.setSize(windowWidth, windowHeight);
        BufferedImage image = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        int robotSize = 30;
        int halfRobotSize = robotSize / 2;

        //слева
        gv.setM_robotPositionX(-100);
        gv.setM_robotPositionY(50);

        gv.paint(g);

        assertEquals(halfRobotSize, gv.getM_robotPositionX(), 1);

        //справа
        gv.setM_robotPositionX(900);
        gv.setM_robotPositionY(50);

        gv.paint(g);

        assertEquals(windowWidth - halfRobotSize, gv.getM_robotPositionX(), 1);

        //сверху
        gv.setM_robotPositionX(100);
        gv.setM_robotPositionY(-200);

        gv.paint(g);

        assertEquals(robotSize, gv.getM_robotPositionY(), 1);

        //снизу
        gv.setM_robotPositionX(100);
        gv.setM_robotPositionY(900);

        gv.paint(g);

        assertEquals(windowHeight - robotSize, gv.getM_robotPositionY(), 1);


        /*
        if (robotCenterX - halfRobotSize < 0) {
            assertEquals(halfRobotSize, gv.getM_robotPositionX(), 1);
        } else if (robotCenterX + halfRobotSize > windowWidth) {
            assertEquals(windowWidth - halfRobotSize, gv.getM_robotPositionX(), 1);
        }

        if (robotCenterY - halfRobotSize < 0) {
            assertEquals(robotSize, gv.getM_robotPositionY(), 1);
        } else if (robotCenterY + halfRobotSize > windowHeight) {
            assertEquals(windowHeight - robotSize, gv.getM_robotPositionY(), 1);
        }

         */

    }

}
