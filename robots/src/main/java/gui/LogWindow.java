package main.java.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import javax.swing.JPanel;
import main.java.log.LogChangeListener;
import main.java.log.LogEntry;
import main.java.log.LogWindowSource;

public class LogWindow extends CustomJInternalFrame implements LogChangeListener
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;

    public LogWindow(LogWindowSource logSource)
    {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n"); // все сообщения из записей логов
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate(); //перерисовка
    }

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent); //постановки задачи в очередь событий в главном потоке
    }

    @Override
    public void closeWindow()
    {
        m_logSource.unregisterListener(this);
        dispose();
    }

}
