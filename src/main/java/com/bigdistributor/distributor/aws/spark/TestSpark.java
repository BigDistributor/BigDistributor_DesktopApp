package com.bigdistributor.dataexchange.spark;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.bigdistributor.dataexchange.aws.s3.model.S3ClientInstance;
import com.bigdistributor.dataexchange.job.model.JobID;
import com.bigdistributor.dataexchange.spark.interf.SparkTask;
import com.bigdistributor.dataexchange.utils.DEFAULT;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class TestSpark {


    private static final int MAX_PARTITIONS = 15000;


    public static void main(String[] args) throws Exception {
        Logger.getLogger("org").setLevel(Level.ERROR);

        final JavaSparkContext sparkContext = new JavaSparkContext(new SparkConf()
                .setAppName("N5ConvertSpark")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        );

        AmazonS3 s3 = S3ClientInstance.get();
        JobID.set(DEFAULT.id);
        ListObjectsV2Result result = s3.listObjectsV2(JobID.get());
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            System.out.println("* " + os.getKey());
        }

        final List<Long> outputBlockIndexes = LongStream.range(0, 1000).boxed().collect(Collectors.toList());

        sparkContext.parallelize(outputBlockIndexes, Math.min(outputBlockIndexes.size(), MAX_PARTITIONS)).foreach(new SparkTask());

    }

}