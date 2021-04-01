package com.bigdistributor.test;

import com.amazonaws.regions.Regions;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.utils.AWS_DEFAULT;
import com.bigdistributor.aws.spimloader.AWSSpimLoader;
import com.bigdistributor.biglogger.adapters.Log;
import com.bigdistributor.core.blockmanagement.blockinfo.BasicBlockInfo;
import com.bigdistributor.core.metadata.MetadataGenerator;
import com.bigdistributor.core.spim.SpimDataLoader;

import java.io.File;
import java.util.logging.Level;

public class MetadataGeneratorTest {
    private final static String input = "dataset-n5.xml";
    private final static String output = "test_output.n5";
    private final static String metadata = "metadata.xml";
    public static void main(String[] args) throws IllegalAccessException {

        Log.setLevel(Level.INFO);
        AWSCredentialInstance.init(AWS_DEFAULT.AWS_CREDENTIALS_PATH);
        S3BucketInstance.init(AWSCredentialInstance.get(), Regions.EU_CENTRAL_1, AWS_DEFAULT.bucket_name,"");
        SpimDataLoader spimLoader = new AWSSpimLoader(S3BucketInstance.get(), "", input);
        MetadataGenerator metadataGenerator = new MetadataGenerator(spimLoader);
        File metadataFile = new File(metadata);
        metadataGenerator.generate().save(metadataFile);

        System.out.println(metadataFile.getAbsolutePath());
        for(BasicBlockInfo binfo: metadataGenerator.getMetadata().getBlocksInfo()){
            System.out.println(binfo.getBlockId());
        }
//        metadataGenerator.generate().save(new File(metadata));

    }
}
