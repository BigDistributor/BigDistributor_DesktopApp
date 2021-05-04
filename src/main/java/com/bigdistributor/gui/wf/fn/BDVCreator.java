package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.aws.job.utils.AWSWorkflow;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.read.AWSReader;
import com.bigdistributor.aws.spimloader.AWSSpimLoader;
import com.bigdistributor.core.spim.SpimDataLoader;
import com.bigdistributor.core.task.items.Metadata;
import com.bigdistributor.gui.bdv.BDVProgressive;

public class BDVCreator implements HeadlessFunction {
    @Override
    public void start() throws Exception {
        SpimDataLoader spimLoader = AWSSpimLoader.init(S3BucketInstance.get().getS3(), "s3://"+S3BucketInstance.get().getBucketName()+"/"+ AWSWorkflow.get().getClusterData());
        Metadata md = Metadata.fromJsonString(new AWSReader(S3BucketInstance.get(), "", AWSWorkflow.get().getClusterMetada()).get());
        BDVProgressive.init(spimLoader.getSpimdata(), md);
        BDVProgressive.get().show();
    }
}
