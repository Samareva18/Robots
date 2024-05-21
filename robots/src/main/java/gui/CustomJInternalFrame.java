package main.java.gui;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class CustomJInternalFrame extends JInternalFrame {

    public CustomJInternalFrame(String str, boolean b, boolean b1, boolean b2, boolean b3) {

        super(str, b, b1, b2, b3);
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                if (WindowCloseConfirmation.showConfirmation(getTitle())) {
                    //WindowManager.saveWindowState(getWindowState());
                    closeWindow();
                }
            }
        });
    }

    public void closeWindow() {
        dispose();
    }

//    public WindowState getWindowState(){
//        WindowState state = new WindowState(getX(), getY(), getWidth(), getHeight());
//        return state;
//    }


}
