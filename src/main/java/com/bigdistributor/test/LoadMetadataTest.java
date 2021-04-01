package com.bigdistributor.test;

import com.amazonaws.regions.Regions;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.read.AWSReader;
import com.bigdistributor.aws.utils.AWS_DEFAULT;
import com.bigdistributor.biglogger.adapters.Log;
import com.bigdistributor.core.blockmanagement.blockinfo.BasicBlockInfo;
import com.bigdistributor.core.task.items.Metadata;

import java.util.logging.Level;

public class LoadMetadataTest {
    private final static String input = "dataset-n5.xml";
    private final static String output = "test_output.n5";
    private final static String metadata = "metadata.json";
    public static void main(String[] args) throws Exception {

        Log.setLevel(Level.INFO);
        AWSCredentialInstance.init(AWS_DEFAULT.AWS_CREDENTIALS_PATH);
        S3BucketInstance.init(AWSCredentialInstance.get(), Regions.EU_CENTRAL_1, AWS_DEFAULT.bucket_name,"");
        Metadata md = Metadata.fromJsonString(new AWSReader(S3BucketInstance.get(), "", metadata).get());

        for(BasicBlockInfo binfo: md.getBlocksInfo()){
            System.out.println(binfo.getBlockId());
        }

    }
}
