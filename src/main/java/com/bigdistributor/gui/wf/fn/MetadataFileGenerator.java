package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.job.utils.AWSWorkflow;
import com.bigdistributor.aws.spimloader.AWSSpimLoader;
import com.bigdistributor.core.metadata.MetadataGenerator;
import com.bigdistributor.core.spim.SpimDataLoader;

import java.io.File;

public class MetadataFileGenerator implements HeadlessFunction {
    @Override
    public void start() throws Exception {
        SpimDataLoader spimLoader = AWSSpimLoader.init(S3BucketInstance.get().getS3(), "s3://" + S3BucketInstance.get().getBucketName() + "/" + AWSWorkflow.get().getClusterData());
        MetadataGenerator metadataGenerator = new MetadataGenerator(spimLoader);
        String MetadataPath = AWSWorkflow.get().setFile("metadata.json");
        metadataGenerator.generate().save(new File(MetadataPath));
        AWSWorkflow.get().setMetadataPath(MetadataPath);
    }
}
