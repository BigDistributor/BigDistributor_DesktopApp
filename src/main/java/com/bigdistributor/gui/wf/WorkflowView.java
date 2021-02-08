package com.bigdistributor.gui.wf;

import com.bigdistributor.biglogger.adapters.Log;
import com.bigdistributor.gui.wf.items.LogView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.invoke.MethodHandles;

public class WorkflowView extends JFrame {
    private static final Log logger = Log.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());


    private JMenuItem fileManagement;
    private JMenuItem workflowManagement;
    private JPanel mainPanel;

    public WorkflowView() {

        initUI();

    }

    private void initUI() {

        createMenuBar();

        setTitle("AWS Workflow");
        setSize(900, 800);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPanel, new LogView().getPane());
        getContentPane().add(splitPane);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        fileManagement = new JMenuItem("Send Files");
        fileManagement.setMnemonic(KeyEvent.VK_E);
        fileManagement.setToolTipText("Send files to AWS");
        fileManagement.addActionListener((event) -> sendFiles(event));

        workflowManagement = new JMenuItem("Workflow management");
        workflowManagement.setMnemonic(KeyEvent.VK_E);
        workflowManagement.setToolTipText("Manage workflow");
        workflowManagement.addActionListener((event) -> manageWorkflow(event));



        menuBar.add(fileManagement);
        menuBar.add(workflowManagement);
        setJMenuBar(menuBar);
        sendFiles(null);

    }


    private void manageWorkflow(ActionEvent event) {
        setActivate(workflowManagement, fileManagement);
    }

    private void setActivate(JMenuItem menu1, JMenuItem menu2) {
        menu1.getComponent().setBackground(Color.GRAY);
        menu2.getComponent().setBackground(Color.LIGHT_GRAY);
    }

    private void sendFiles(ActionEvent event) {
        setActivate(fileManagement, workflowManagement);
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(1000, 500));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new WorkflowView().setVisible(true);
        });
    }
}