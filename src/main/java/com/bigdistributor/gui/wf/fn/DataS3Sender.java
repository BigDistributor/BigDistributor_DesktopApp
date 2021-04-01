package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.aws.job.utils.AWSWorkflow;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;

import java.io.File;

public class DataS3Sender implements HeadlessFunction {
    @Override
    public void start() throws Exception {
        for (String f : AWSWorkflow.get().getLocalData())
            S3BucketInstance.get().upload(new File(f));
        AWSWorkflow.get().setClusterData(new File(AWSWorkflow.get().getLocalData().get(0)).getName());
    }
}
