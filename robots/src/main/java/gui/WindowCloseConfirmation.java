package main.java.gui;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.util.Locale;

public class WindowCloseConfirmation {

    public static boolean showConfirmation(String windowTitle) {

        Locale.setDefault(new Locale("ru"));

        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");

        int choice = JOptionPane.showConfirmDialog(null,
                "Вы уверены, что хотите выйти?",
                windowTitle,
                JOptionPane.YES_NO_OPTION);

        return choice == JOptionPane.YES_OPTION;
    }

}
