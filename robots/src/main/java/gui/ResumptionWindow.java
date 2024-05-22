package main.java.gui;

import javax.swing.*;
import java.util.Locale;

public class ResumptionWindow {
    public static boolean showResumption(String windowTitle) {

        Locale.setDefault(new Locale("ru"));

        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");

        int choice = JOptionPane.showConfirmDialog(null,
                "Возобновить?",
                windowTitle,
                JOptionPane.YES_NO_OPTION);

        return choice == JOptionPane.YES_OPTION;
    }

}
