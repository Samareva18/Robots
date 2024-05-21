package main.java.gui;

import java.io.*;
import java.util.List;

public class WindowManager {

    public static void saveWindowStates(List<WindowState> states) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("windowState.dat"))) {
            out.writeObject(states);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении состояний окон: " + e.getMessage());
        }
    }

    public static List<WindowState> loadWindowStates() {
        List<WindowState> states = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("windowState.dat"))) {
            states = (List<WindowState>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при загрузке состояний окон: " + e.getMessage());
        }
        return states;
    }
}

