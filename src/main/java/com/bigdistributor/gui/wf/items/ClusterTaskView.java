package com.bigdistributor.gui.wf.items;

import com.bigdistributor.aws.AWSWorkflow;
import com.bigdistributor.aws.job.JarParams;
import com.bigdistributor.aws.job.emr.EMRLambdaManagerParams;
import com.bigdistributor.core.task.JobID;
import com.bigdistributor.helpers.TASK_DEFAULT;
import fiji.util.gui.GenericDialogPlus;

public class ClusterTaskView {
    public EMRLambdaManagerParams show() {
        final GenericDialogPlus gd = new GenericDialogPlus("Start Cluster Task");

        gd.addChoice("Task Type: ", TASK_DEFAULT.TASKS, TASK_DEFAULT.TASKS[0]);
        gd.addNumericField("Work Instances: ", 2);
        gd.addMessage("");
        gd.addStringField("Output:", JobID.get() + ".n5", 30);
        gd.addMessage("");
        gd.addStringField("Job ID: ", JobID.get(), 40);
        gd.addStringField("Bucket Name:", AWSWorkflow.get().getBucketName(), 50);
        gd.addStringField("Task File:", AWSWorkflow.get().getFullPathClusterJar(), 100);
        gd.addStringField("Input:", AWSWorkflow.get().getClusterData(), 30);

        gd.addStringField("Metadata:", AWSWorkflow.get().getClusterMetada(), 30);
        gd.addStringField("Task Params:", "", 30);


        gd.addStringField("Cluster name:", "Task_" + JobID.get(), 30);


//        gd.addDirectoryField("Output maven: ", TASK_DEFAULT.outputMaven, 100);
        gd.addMessage("");

        gd.showDialog();

        if (gd.wasCanceled())
            return null;

//
        String taskType = gd.getNextChoice();
        int instances = (int) gd.getNextNumber();
        String output = gd.getNextString();
        String jobid = gd.getNextString();
        String bucketName = gd.getNextString();
        String taskFile = gd.getNextString();
        String input = gd.getNextString();
        String metadata = gd.getNextString();
        String taskParams = gd.getNextString();
        String clustername = gd.getNextString();
        JobID.set(jobid);
        EMRLambdaManagerParams params = new EMRLambdaManagerParams(taskFile, clustername,
                new JarParams(taskType, jobid, bucketName,
                        input,
                        output, metadata,
                        taskParams), instances);
        return params;

    }

    public static void main(String[] args) {
        EMRLambdaManagerParams params = new ClusterTaskView().show();
        System.out.println(params.toString());
    }
}
