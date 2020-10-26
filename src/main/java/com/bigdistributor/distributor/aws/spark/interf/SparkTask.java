package com.bigdistributor.dataexchange.spark.interf;

import com.amazonaws.util.EC2MetadataUtils;
import org.apache.spark.api.java.function.VoidFunction;

public class SparkTask implements VoidFunction<Long>{
    @Override
    public void call(Long blockIndex) throws Exception {
        String instanceId = EC2MetadataUtils.getInstanceId();
        System.out.println(blockIndex + "- "+instanceId);

    }
}
