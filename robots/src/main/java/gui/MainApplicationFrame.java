package main.java.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



import main.java.log.Logger;


public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    LogWindow logWindow = createLogWindow();
    GameWindow gameWindow = createGameWindow();

    public MainApplicationFrame() {

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);

        setContentPane(desktopPane);

        addWindow(logWindow);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeMainWindow();
            }
        });

    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");

        return logWindow;
    }

    protected GameWindow createGameWindow()
    {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(600,  600);

        return gameWindow;
    }

    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }


    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");

        lookAndFeelMenu.add(createSystemLookAndFeelMenuItem());
        lookAndFeelMenu.add(createCrossPlatformLookAndFeelMenuItem());

        return lookAndFeelMenu;
    }

    private JMenuItem createSystemLookAndFeelMenuItem() {
        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        return systemLookAndFeel;
    }

    private JMenuItem createCrossPlatformLookAndFeelMenuItem() {
        JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        crossplatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return crossplatformLookAndFeel;
    }

    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        testMenu.add(createAddLogMessageMenuItem());

        return testMenu;
    }

    private JMenuItem createAddLogMessageMenuItem() {
        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
        });
        return addLogMessageItem;
    }

    private JMenu createExiteMenu() {
        JMenu exiteMenu = new JMenu("Выйти");
        exiteMenu.setMnemonic(KeyEvent.VK_E);
        exiteMenu.getAccessibleContext().setAccessibleDescription("Меню выхода из приложения");

        exiteMenu.add(createExiteItem());

        return exiteMenu;
    }

    private JMenuItem createExiteItem() {
        JMenuItem addExiteItem = new JMenuItem("Выйти из приложения", KeyEvent.VK_S);
        addExiteItem.addActionListener((event) -> {
            closeMainWindow();
        });
        return addExiteItem;
    }

    public void closeMainWindow(){
        if (WindowCloseConfirmation.showConfirmation(getTitle())) {
            dispose();
        }
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExiteMenu());
        return menuBar;
    }

    // установка внешнего вида
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException e)
        {

        }
    }
}
