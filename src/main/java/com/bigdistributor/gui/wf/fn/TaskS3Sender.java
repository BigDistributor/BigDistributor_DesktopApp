package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.aws.job.utils.AWSWorkflow;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;

import java.io.File;

public class TaskS3Sender implements HeadlessFunction {
    @Override
    public void start() throws Exception {
        File file = new File(AWSWorkflow.get().getLocalJar());
        S3BucketInstance.get().uploadFile(file);
        AWSWorkflow.get().setClusterJar(file.getName());
    }
}
