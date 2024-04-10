package test.java.gui;

import main.java.gui.WindowCloseConfirmation;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

public class WindowCloseConfirmationTest {

    @Test
    public void testShowConfirmation_YesOption() {

        Locale.setDefault(new Locale("ru"));
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");

        boolean result = WindowCloseConfirmation.showConfirmation("Тестовое окно");

        assertTrue(result);
    }

    @Test
    public void testShowConfirmation_NoOption() {

        Locale.setDefault(new Locale("ru"));
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");

        boolean result = WindowCloseConfirmation.showConfirmation("Тестовое окно");

        assertFalse(result);
    }
}

