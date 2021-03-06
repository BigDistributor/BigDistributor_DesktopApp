package com.bigdistributor.main.workflow;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.utils.AWS_DEFAULT;
import com.bigdistributor.aws.spimloader.AWSSpimLoader;
import com.bigdistributor.core.metadata.MetadataGenerator;
import com.bigdistributor.core.spim.SpimDataLoader;
import com.bigdistributor.core.task.JobID;

import java.io.File;

public class MetadataTest {

    private static final String path = "/Users/Marwan/Desktop/Task/TaskFiles/metadata.json";

    public static void main(String[] args) throws IllegalAccessException {

        JobID.createNew();
        // We get this information from user
        AWSCredentials cred = AWSCredentialInstance.init(AWS_DEFAULT.AWS_CREDENTIALS_PATH).get();

        S3BucketInstance s3 = S3BucketInstance.init(AWSCredentialInstance.get(), Regions.EU_CENTRAL_1, AWS_DEFAULT.bucket_name, "").get();

        // Init XML
        SpimDataLoader spimLoader = AWSSpimLoader.init(s3.getS3(), "s3://mzouink-test/dataset-n5.xml");

        MetadataGenerator metadataGenerator = new MetadataGenerator(spimLoader);

        metadataGenerator.generate().save(new File(path));

    }
}
