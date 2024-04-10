package main.java.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он 
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено 
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений 
 * ограниченного размера) 
 */

//класс реализует логику добавления сообщений в лог,
//управление слушателями и оповещение об изменениях в логе
public class LogWindowSource
{
    private int m_iQueueLength;

    private final BlockingQueue<LogEntry> m_messages; // Хранит сообщения логов
    private final BlockingQueue<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners; //может быть изменено несколькими потоками одновременно

    public LogWindowSource(int iQueueLength)
    {
        m_iQueueLength = iQueueLength;
        m_messages = new ArrayBlockingQueue<>(iQueueLength);
        m_listeners = new ArrayBlockingQueue<>(iQueueLength);
    }

    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners) //обеспечение потокобезопасности при доступе к списку
        {
            m_listeners.add(listener);
            m_activeListeners = null; //обновление массива активных слушателей
        }
    }

    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    //добавляет новую запись журнала с указанным уровнем логирования и сообщением,
    //уведомляет всех активных слушателей об изменении журнала
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage); //создается новая запись

        synchronized(m_messages) {

            if (m_messages.size() >= m_iQueueLength) {
                m_messages.poll(); //удаление самой старой записи
            }

        }

        m_messages.add(entry);

        LogChangeListener [] activeListeners = m_activeListeners; //присвоение переменной activeListeners ссылки на массив m_activeListeners
        if (activeListeners == null)
        {
            synchronized (m_listeners)
            {
                if (m_activeListeners == null)
                {
                    activeListeners = m_listeners.toArray(new LogChangeListener [0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged(); //уведомление всех активных слушателей об изменении журнала
        }
    }

    public int size()
    {
        return m_messages.size();
    }

    //Возвращается подсписок записей журнала, начиная с индекса startFrom и заканчивая индексом indexTo
    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= m_messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        return m_messages.stream().toList().subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all()
    {
        return m_messages;
    }

    public BlockingQueue<LogEntry> getM_messages() {
        return m_messages;
    }
    public BlockingQueue<LogChangeListener> getM_listeners() {
        return m_listeners;
    }

    public LogChangeListener[] getM_activeListeners(){
        return m_activeListeners;
    }

    public int getM_iQueueLength() {
        return m_iQueueLength;
    }
}
