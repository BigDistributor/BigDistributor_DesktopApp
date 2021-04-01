package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;

public class BucketCreator implements HeadlessFunction {
    @Override
    public void start() throws IllegalAccessException {
        S3BucketInstance.get().createBucket();
    }
}
