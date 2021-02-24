package com.bigdistributor.gui.wf;


import com.bigdistributor.biglogger.adapters.Log;
import com.bigdistributor.core.task.JobID;
import com.bigdistributor.gui.bdv.BDVProgressive;

import javax.swing.*;
import java.awt.*;
import java.lang.invoke.MethodHandles;

public class AWSMenuView extends JFrame {
    private static final Log logger = Log.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    private JPanel mainPanel;

    public AWSMenuView() {
        JobID.createNew();
        initUI();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new AWSMenuView().setVisible(true));
    }

    private void initUI() {

        createMenuBar();

        setTitle("AWS Workflow");
        setSize(700, 400);

//        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPanel, new LogView().getPane());
//        getContentPane().add(new LogView().getPane());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JLabel config = new JLabel("Config:   ");
        menuBar.add(config);
        JMenu awsmenu = new JMenu("AWS");

        JMenuItem configAws = new JMenuItem("Config S3");
        configAws.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.configS3Bucket()).run());

        JMenuItem createBucket = new JMenuItem("Create bucket");
        createBucket.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.createBucket()).run());

        menuBar.add(awsmenu);
        awsmenu.add(configAws);
        awsmenu.add(createBucket);

        JMenu taskMenu = new JMenu("Task");
        JMenuItem configLocalTask = new JMenuItem("Config Local Task");
        configLocalTask.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.configLocalTask()).run());

        JMenuItem compileTask = new JMenuItem("Compile");
        compileTask.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.compileTask()).run());
        JMenuItem sendTaskToS3 = new JMenuItem("Send Task to S3");
        sendTaskToS3.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.sendTaskToS3()).run());

        JMenuItem configAwsTask = new JMenuItem("Config AWS Task");
        configAwsTask.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.configAWSTask()).run());

        menuBar.add(taskMenu);
        taskMenu.add(configLocalTask);
        taskMenu.add(compileTask);
        taskMenu.add(sendTaskToS3);
        taskMenu.add(new JSeparator());
        taskMenu.add(configAwsTask);

        JMenu dataMenu = new JMenu("Data");
        JMenuItem configLocalData = new JMenuItem("Config Local Data");
        configLocalData.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.configLocalData()).run());

        JMenuItem sendDataToS3 = new JMenuItem("Send Data to S3");
        sendDataToS3.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.sendDataToS3()).run());

        JMenuItem configAwsData = new JMenuItem("Config AWS Data");
        configAwsData.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.configAWSData()).run());

        JMenuItem generateMetadata = new JMenuItem("Generate metadata");
        generateMetadata.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.generateMetadata()).run());

        JMenuItem sendMetadata = new JMenuItem("Send metadata to S3");
        sendMetadata.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.sendMetadataToS3()).run());

        menuBar.add(dataMenu);
        dataMenu.add(configLocalData);
        dataMenu.add(sendDataToS3);
        dataMenu.add(new JSeparator());
        dataMenu.add(configAwsData);
        dataMenu.add(new JSeparator());
        dataMenu.add(generateMetadata);
        dataMenu.add(sendMetadata);

        JMenu jobMenu = new JMenu("Job ");

        JMenuItem startAWSJob = new JMenuItem("Start AWS Job");
        startAWSJob.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.startAWSJob()).run());
        JMenuItem startLocalJob = new JMenuItem("Start Local Job");
        startLocalJob.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.startLocalJob()).run());

        menuBar.add(jobMenu);
        jobMenu.add(startAWSJob);
        jobMenu.add(startLocalJob);

        JMenu bdvMenu = new JMenu("BDV");

        JMenuItem startBDV = new JMenuItem("Start BDV");
        startBDV.addActionListener(e -> ((Runnable) () -> HeadlessWorkflowFunctions.startBDV()).run());

        JMenuItem showProgressBdv = new JMenuItem("Show Progress");
        showProgressBdv.addActionListener(e -> ((Runnable) () -> BDVProgressive.get().showProgress()).run());

        menuBar.add(bdvMenu);
        bdvMenu.add(startBDV);
        bdvMenu.add(showProgressBdv);

        setJMenuBar(menuBar);

    }

}
