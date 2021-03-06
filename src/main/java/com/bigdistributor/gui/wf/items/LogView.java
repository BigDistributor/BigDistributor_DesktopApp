package com.bigdistributor.gui.wf.items;

import com.bigdistributor.biglogger.adapters.LoggerManager;
import com.bigdistributor.biglogger.handlers.TerminalLogHandler;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogView extends TerminalLogHandler {
    private final static Level level = Level.INFO;
    private JTextArea textArea = null;
    private JScrollPane pane = null;

    public LogView() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        pane = new JScrollPane(textArea);
        this.setLevel(level);
        pane.setMaximumSize(new Dimension(1000,300));
        LoggerManager.addHandler(this);
    }

    public JScrollPane getPane() {
        return pane;
    }

    @Override
    public void publish(LogRecord record) {
        new Thread(() -> {
            final String msg;
            if (getFormatter() == null) {
                msg = defaultFormatter.format(record);
            } else {
                msg = getFormatter().format(record);
            }
            try {
                textArea.getDocument().insertString(0, msg + "\n", null);
            } catch (BadLocationException e) {
                textArea.append(msg);
                e.printStackTrace();
            }
            textArea.update(textArea.getGraphics());
//            textArea.validate();
//            pane.validate();
//            pane.getRootPane().validate();
        }).start();
    }

}
