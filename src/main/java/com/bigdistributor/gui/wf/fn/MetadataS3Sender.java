package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.aws.job.utils.AWSWorkflow;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;

import java.io.File;

public class MetadataS3Sender implements HeadlessFunction {
    @Override
    public void start() throws Exception {
        File metadataFile = new File(AWSWorkflow.get().getMetadataPath());
        S3BucketInstance.get().upload(metadataFile);
        AWSWorkflow.get().setClusterMetadata(metadataFile.getName());
    }
}
