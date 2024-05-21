package main.java.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.*;


import main.java.log.Logger;


public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
//    LogWindow logWindow = createLogWindow();
//    GameWindow gameWindow = createGameWindow();
//
    LogWindow logWindow;
    GameWindow gameWindow;

    public MainApplicationFrame() {

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);

        setContentPane(desktopPane);

        createStateWindows();

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

    protected void createStateWindows(){
        List<WindowState> stateList = WindowManager.loadWindowStates();
        logWindow = createLogWindow();
        gameWindow = createGameWindow();
        if (stateList != null) {
            for (WindowState state : stateList) {
                int x = state.getX();
                int y = state.getY();
                int width = state.getWidth();
                int height = state.getHeight();
                String window = state.getWindowType();

                if (Objects.equals(window, "log")) {
                    logWindow.setLocation(x, y);
                    logWindow.setSize(width, height);
                }

                if (Objects.equals(window, "game")) {
                    gameWindow.setLocation(x, y);
                    gameWindow.setSize(width, height);
                }
            }
        }
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

            int logWindowWidth = logWindow.getWidth();
            int logWindowHeight = logWindow.getHeight();
            int logWindowX = logWindow.getX();
            int logWindowY = logWindow.getY();

            int gameWindowWidth = gameWindow.getWidth();
            int gameWindowHeight = gameWindow.getHeight();
            int gameWindowX = gameWindow.getX();
            int gameWindowY = gameWindow.getY();

            WindowState logState = new WindowState(logWindowX, logWindowY, logWindowWidth, logWindowHeight, "log");
            WindowState gameState = new WindowState(gameWindowX, gameWindowY, gameWindowWidth, gameWindowHeight, "game");

            List<WindowState> stateList = new ArrayList<>();
            stateList.add(gameState);
            stateList.add(logState);

            WindowManager.saveWindowStates(stateList);

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