package main.java.gui;
import java.io.Serial;
import java.io.Serializable;

public class WindowState implements Serializable {
    @Serial
    private static final long serialVersionUID = 123456789L;

    private int x;
    private int y;
    private int width;
    private int height;

    private String windowType;

    public WindowState() {
    }

    WindowState(int x, int y, int width, int height, String windowType){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.windowType = windowType;
    }

//    private boolean isIcon;
//
//    public void setX(int x) {
//        this.x = x;
//    }
//
//    public void setY(int y) {
//        this.y = y;
//    }
//
//    public void setHeight(int height) {
//        this.height = height;
//    }
//
//    public void setWidth(int width) {
//        this.width = width;
//    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public String getWindowType(){
        return windowType;
    }
}

