package com.bigdistributor.gui.wf;


import com.bigdistributor.biglogger.adapters.Log;
import com.bigdistributor.core.task.JobID;
import com.bigdistributor.gui.wf.fn.*;

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
        configAws.addActionListener(e -> run(new S3BucketConfigurator()));

        JMenuItem createBucket = new JMenuItem("Create bucket");
        createBucket.addActionListener(e -> run(new BucketCreator()));

        menuBar.add(awsmenu);
        awsmenu.add(configAws);
        awsmenu.add(createBucket);

        JMenu taskMenu = new JMenu("Task");
        JMenuItem configLocalTask = new JMenuItem("Config Local Task");
        configLocalTask.addActionListener(e -> run(new LocalTaskConfigurator()));

        JMenuItem compileTask = new JMenuItem("Compile");
        compileTask.addActionListener(e -> run(new TaskCompiler()));
        JMenuItem sendTaskToS3 = new JMenuItem("Send Task to S3");
        sendTaskToS3.addActionListener(e -> run(new TaskS3Sender()));

        JMenuItem configAwsTask = new JMenuItem("Config AWS Task");
        configAwsTask.addActionListener(e -> run(new AWSTaskConfigurator()));
        menuBar.add(taskMenu);
        taskMenu.add(configLocalTask);
        taskMenu.add(compileTask);
        taskMenu.add(sendTaskToS3);
        taskMenu.add(new JSeparator());
        taskMenu.add(configAwsTask);

        JMenu dataMenu = new JMenu("Data");
        JMenuItem configLocalData = new JMenuItem("Config Local Data");
        configLocalData.addActionListener(e -> run(new LocalDataConfigurator()));

        JMenuItem sendDataToS3 = new JMenuItem("Send Data to S3");
        sendDataToS3.addActionListener(e -> run(new DataS3Sender()));

        JMenuItem configAwsData = new JMenuItem("Config AWS Data");
        configAwsData.addActionListener(e -> run(new AWSDataConfigurator()));

        JMenuItem generateMetadata = new JMenuItem("Generate metadata");
        generateMetadata.addActionListener(e -> run(new MetadataFileGenerator()));

        JMenuItem sendMetadata = new JMenuItem("Send metadata to S3");
        sendMetadata.addActionListener(e -> run(new MetadataS3Sender()));

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

        startAWSJob.addActionListener(e -> run(new AWSJobStarter()));
        JMenuItem startLocalJob = new JMenuItem("Start Local Job");
        startLocalJob.addActionListener(e -> run(new LocalJobStarter()));

        menuBar.add(jobMenu);
        jobMenu.add(startAWSJob);
        jobMenu.add(startLocalJob);

        JMenu bdvMenu = new JMenu("BDV");

        JMenuItem startBDV = new JMenuItem("Start BDV");
        startBDV.addActionListener(e -> run(new BDVCreator()));

        JMenuItem showProgressBdv = new JMenuItem("Show Progress");
        showProgressBdv.addActionListener(e -> run(new BDVProgressMentor()));

        menuBar.add(bdvMenu);
        bdvMenu.add(startBDV);
        bdvMenu.add(showProgressBdv);

        setJMenuBar(menuBar);

    }

    public void run(HeadlessFunction function) {
        FunctionsExecutor.run(function);
    }

}
