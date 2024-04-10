package test.java.gui;

import main.java.gui.CustomJInternalFrame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CustomJInternalFrameTest {

    @Test
    public void testCloseWindow() {
        CustomJInternalFrame frame = new CustomJInternalFrame("Test Frame", true, true, true, true);
        frame.closeWindow();
        assertTrue(frame.isClosed());
    }

}
