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
import com.bigdistributor.core.task.items.Metadata;
import com.bigdistributor.gui.bdv.BDVProgressive;
import com.bigdistributor.gui.wf.items.*;
import com.bigdistributor.helpers.TASK_DEFAULT;
import com.bigdistributor.io.mvn.JarLooker;
import com.bigdistributor.io.mvn.MavenCompiler;
import com.bigdistributor.local.JarExecutor;
import com.bigdistributor.local.LocalTaskParams;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

public class HeadlessWorkflowFunctions {

    private static final Log logger = Log.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    public static void createBucket() {
        try {
            S3BucketInstance.get().createBucket();
            PopupMessage.infoBox("Bucket created", "Success");
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }

    public static void startBDV() {
        try {
            SpimDataLoader spimLoader = new AWSSpimLoader(S3BucketInstance.get(), "", AWSWorkflow.get().getClusterData());
            Metadata md = Metadata.fromJsonString(new AWSReader(S3BucketInstance.get(), "", AWSWorkflow.get().getClusterMetada()).get());
            BDVProgressive.init(spimLoader.getSpimdata(), md);
            BDVProgressive.get().show();
        } catch (Exception err) {
            logger.error(err.toString());
            err.printStackTrace();
        }
    }

    public static void startAWSJob() {
        EMRLambdaManagerParams params = new ClusterTaskView().show();
        AWSCredentials credentials = AWSCredentialInstance.get();
        InvokeResult result = new EMRLambdaManager(credentials, params).invoke();
        PopupMessage.infoBox("Cluster started ! ", "Success " + result.getStatusCode());
    }

    public static void startLocalJob() {
        LocalTaskParams params = new LocalTaskView().show();
    new JarExecutor(params).run();
        PopupMessage.infoBox("Local started ! ", "Success ");
    }

    public static void sendMetadataToS3() {
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

    public static void generateMetadata() {
        try {
            logger.info("Cluster input:" + AWSWorkflow.get().getClusterData());
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
    }

    public static void configAWSData() {
        String file = new OneFileView("AWS Data", "Spim XML name: ", "", OneFileView.FileType.Text).show();
        AWSWorkflow.get().setClusterData(file);
    }

    public static void sendDataToS3() {
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

    public static void configLocalData() {
        List<String> files = new SpimView().show();
        AWSWorkflow.get().setLocalData(files);
    }

    public static void configAWSTask() {
        String file = new OneFileView("AWS Task", "Task name: ", AWS_DEFAULT.cloud_jar, OneFileView.FileType.Text).show();
        AWSWorkflow.get().setClusterJar(file);
    }

    public static void sendTaskToS3() {
        try {
            File file = new File(AWSWorkflow.get().getLocalJar());
            S3BucketInstance.get().uploadFile(file);
            AWSWorkflow.get().setClusterJar(file.getName());

            PopupMessage.infoBox("Task sent to S3 ! ", "Success ");
        } catch (IllegalAccessException | InterruptedException illegalAccessException) {
            logger.error(illegalAccessException.toString());
            illegalAccessException.printStackTrace();
        }
    }

    public static void compileTask() {
        try {
            MavenCompiler mavenCompiler = new CompileTaskView().show();
            if (mavenCompiler.start() == 0)
                PopupMessage.infoBox("Jar compiled", "Success");
            AWSWorkflow.get().setLocalJar(new JarLooker(mavenCompiler.getOutputPath(), mavenCompiler.getMainProject()).lookForMainJar().getAbsolutePath());
        } catch (Exception ex) {
            logger.error(ex.toString());
            ex.printStackTrace();
        }
    }

    public static void configLocalTask() {
        String file = new OneFileView("Local Task", "Task Path", TASK_DEFAULT.jar, OneFileView.FileType.File).show();
        AWSWorkflow.get().setLocalJar(file);
    }

    public static void configS3Bucket() {
        new S3BucketConfigurationView().show();
    }
}
