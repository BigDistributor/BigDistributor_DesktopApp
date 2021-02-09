package com.bigdistributor.gui.wf;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.bigdistributor.aws.AWSWorkflow;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.read.AWSReader;
import com.bigdistributor.aws.dataexchange.utils.AWS_DEFAULT;
import com.bigdistributor.aws.job.emr.EMRLambdaManager;
import com.bigdistributor.aws.job.emr.EMRLambdaManagerParams;
import com.bigdistributor.aws.spimloader.AWSSpimLoader;
import com.bigdistributor.biglogger.adapters.Log;
import com.bigdistributor.core.metadata.MetadataGenerator;
import com.bigdistributor.core.spim.SpimDataLoader;
import com.bigdistributor.core.task.JobID;
import com.bigdistributor.core.task.items.Metadata;
import com.bigdistributor.gui.bdv.BDVProgressive;
import com.bigdistributor.gui.wf.items.*;
import com.bigdistributor.helpers.TASK_DEFAULT;
import com.bigdistributor.io.mvn.JarLooker;
import com.bigdistributor.io.mvn.MavenCompiler;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

public class AWSMenuView extends JFrame {
    private static final Log logger = Log.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    private JPanel mainPanel;

    public AWSMenuView() {
        JobID.createNew();
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
        JLabel config = new JLabel("Config:   ");
        menuBar.add(config);
        JMenu awsmenu = new JMenu("AWS");

        JMenuItem configAws = new JMenuItem("Config S3");
        configAws.addActionListener(e -> {
            new S3BucketConfigurationView().show();
        });

        JMenuItem createBucket = new JMenuItem("Create bucket");
        createBucket.addActionListener(e -> {
            try {
                S3BucketInstance.get().createBucket();
                PopupMessage.infoBox("Bucket created", "Success");
            } catch (IllegalAccessException illegalAccessException) {
                logger.error(e.toString());
                illegalAccessException.printStackTrace();
            }
        });

        menuBar.add(awsmenu);
        awsmenu.add(configAws);
        awsmenu.add(createBucket);

        JMenu taskMenu = new JMenu("Task");
        JMenuItem configLocalTask = new JMenuItem("Config Local Task");
        configLocalTask.addActionListener(e -> {
            String file = new OneFileView("Local Task", "Task Path", TASK_DEFAULT.jar, OneFileView.FileType.File).show();
            AWSWorkflow.get().setLocalJar(file);
        });
        JMenuItem compileTask = new JMenuItem("Compile");
        compileTask.addActionListener(e -> {
            try {
                MavenCompiler mavenCompiler = new CompileTaskView().show();
                if (mavenCompiler.start() == 0)
                    PopupMessage.infoBox("Jar compiled", "Success");
                AWSWorkflow.get().setLocalJar(new JarLooker(mavenCompiler.getOutputPath(), mavenCompiler.getMainProject()).lookForMainJar().getAbsolutePath());
            } catch (Exception ex) {
                logger.error(ex.toString());
                ex.printStackTrace();
            }
        });
        JMenuItem sendTaskToS3 = new JMenuItem("Send Task to S3");
        sendTaskToS3.addActionListener(e -> {
            try {
                File file = new File(AWSWorkflow.get().getLocalJar());
                S3BucketInstance.get().uploadFile(file);
                AWSWorkflow.get().setClusterJar(file.getName());

                PopupMessage.infoBox("Task sent to S3 ! ", "Success ");
            } catch (IllegalAccessException | InterruptedException illegalAccessException) {
                logger.error(illegalAccessException.toString());
                illegalAccessException.printStackTrace();
            }
        });

        JMenuItem configAwsTask = new JMenuItem("Config AWS Task");
        configAwsTask.addActionListener(e -> {
            String file = new OneFileView("AWS Task", "Task name: ", AWS_DEFAULT.cloud_jar, OneFileView.FileType.Text).show();
            AWSWorkflow.get().setClusterJar(file);
        });

        menuBar.add(taskMenu);
        taskMenu.add(configLocalTask);
        taskMenu.add(compileTask);
        taskMenu.add(sendTaskToS3);
        taskMenu.add(new JSeparator());
        taskMenu.add(configAwsTask);

        JMenu dataMenu = new JMenu("Data");
        JMenuItem configLocalData = new JMenuItem("Config Local Data");
        configLocalData.addActionListener(e -> {
            List<String> files = new SpimView().show();
            AWSWorkflow.get().setLocalData(files);

        });
        JMenuItem sendDataToS3 = new JMenuItem("Send Data to S3");
        sendDataToS3.addActionListener(e -> {
                    try {
                        for (String f : AWSWorkflow.get().getLocalData())
                            S3BucketInstance.get().upload(new File(f));
                        AWSWorkflow.get().setClusterData(new File(AWSWorkflow.get().getLocalData().get(0)).getName());
                        PopupMessage.infoBox("Files sent ! " + AWSWorkflow.get().getLocalData().size(), "Success");
                    } catch (IllegalAccessException | InterruptedException | IOException illegalAccessException) {
                        logger.error(illegalAccessException.toString());
                        illegalAccessException.printStackTrace();
                    }
                }

        );
        JMenuItem configAwsData = new JMenuItem("Config AWS Data");
        configAwsData.addActionListener(e -> {
            String file = new OneFileView("AWS Data", "Spim XML name: ", "", OneFileView.FileType.Text).show();
            AWSWorkflow.get().setClusterData(file);
        });

        JMenuItem generateMetadata = new JMenuItem("Generate metadata");
        generateMetadata.addActionListener(e -> {
            try {
                logger.info("Cluster input:"+AWSWorkflow.get().getClusterData());
                SpimDataLoader spimLoader = new AWSSpimLoader(S3BucketInstance.get(), "", AWSWorkflow.get().getClusterData());
                MetadataGenerator metadataGenerator = new MetadataGenerator(spimLoader);
                String MetadataPath = AWSWorkflow.get().setFile("metadata.xml");
                metadataGenerator.generate().save(new File(MetadataPath));
                AWSWorkflow.get().setMetadataPath(MetadataPath);
                PopupMessage.infoBox("Metadata created ! ", "Success");
            } catch (IllegalAccessException illegalAccessException) {
                logger.error(illegalAccessException.toString());
                illegalAccessException.printStackTrace();
            }
        });

        JMenuItem sendMetadata = new JMenuItem("Send metadata to S3");
        sendMetadata.addActionListener(e -> {
                    try {
                        logger.info("start sending metadata");
                        File metadataFile = new File(AWSWorkflow.get().getMetadataPath());
                        S3BucketInstance.get().upload(metadataFile);
                        PopupMessage.infoBox("Metadata sent ! ", "Success");
                        AWSWorkflow.get().setClusterMetadata(metadataFile.getName());
                    } catch (IllegalAccessException | InterruptedException | IOException illegalAccessException) {
                        logger.error(illegalAccessException.toString());
                        illegalAccessException.printStackTrace();
                    }
                }
        );

        menuBar.add(dataMenu);
        dataMenu.add(configLocalData);
        dataMenu.add(sendDataToS3);
        dataMenu.add(new JSeparator());
        dataMenu.add(configAwsData);
        dataMenu.add(new JSeparator());
        dataMenu.add(generateMetadata);
        dataMenu.add(sendMetadata);

        JMenu jobMenu = new JMenu("Job ");
        JMenuItem startJob = new JMenuItem("Start Job");
        startJob.addActionListener(e -> {
            EMRLambdaManagerParams params = new ClusterTaskView().show();
            AWSCredentials credentials = AWSCredentialInstance.get();
            InvokeResult result = new EMRLambdaManager(credentials, params).invoke();
            PopupMessage.infoBox("Cluster started ! ", "Success "+result.getStatusCode());
        });

        menuBar.add(jobMenu);
        jobMenu.add(startJob);


        JMenu bdvMenu = new JMenu("BDV");
        JMenuItem startBDV = new JMenuItem("Start BDV");
        startBDV.addActionListener(e -> {
            Thread thread = new Thread(() ->  {
                    SpimDataLoader spimLoader = null;
            try {
                spimLoader = new AWSSpimLoader(S3BucketInstance.get(), "", AWSWorkflow.get().getClusterData());
                Metadata md = Metadata.fromJsonString(new AWSReader(S3BucketInstance.get(),"",AWSWorkflow.get().getClusterMetada()).get());
                BDVProgressive.init(spimLoader.getSpimdata(),md);
                BDVProgressive.get().show();
            } catch (Exception err) {
                logger.error(err.toString());
                err.printStackTrace();
            }
            });

            thread.start();

        });
        JMenuItem showProgressBdv = new JMenuItem("Show Progress");
        showProgressBdv.addActionListener(e -> {
            BDVProgressive.get().showProgress();
        });
        menuBar.add(bdvMenu);
        bdvMenu.add(startBDV);
        bdvMenu.add(showProgressBdv);

        setJMenuBar(menuBar);

    }


    private void setActivate(JMenuItem menu1, JMenuItem menu2) {
        menu1.getComponent().setBackground(Color.GRAY);
        menu2.getComponent().setBackground(Color.LIGHT_GRAY);
    }

//    private void sendFiles(ActionEvent event) {
//        setActivate(fileManagement, workflowManagement);
//        mainPanel = new JPanel();
//        mainPanel.setPreferredSize(new Dimension(1000, 500));
//    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new AWSMenuView().setVisible(true);
        });
    }
}
