package com.bigdistributor.main.workflow;

import com.bigdistributor.biglogger.adapters.Log;
import com.bigdistributor.core.config.RemoteFile;
import com.bigdistributor.core.task.TaskConfig;

import java.io.File;
import java.lang.invoke.MethodHandles;

public class GenerateConfig {

    private static final Log logger = Log.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    public static void main(String[] args) throws IllegalAccessException {
        String metadataPath = "/Users/Marwan/Desktop/Task/TaskFiles/metadata.json";
         String taskParamsPath = "";
        RemoteFile input = new RemoteFile("mzouink-test","/","dataset-n5.xml");
        RemoteFile output = new RemoteFile("mzouink-test","/","new_output.n5");
        String awsCredentialPath = "/Users/Marwan/Desktop/Task/TaskFiles/bigdistributer.csv";
        String jobId = "testing";
            TaskConfig config = new TaskConfig(metadataPath,taskParamsPath,input,output,awsCredentialPath,jobId);
            config.save(new File("/Users/Marwan/Desktop/Task/TaskFiles/config.json"));
    }


}
