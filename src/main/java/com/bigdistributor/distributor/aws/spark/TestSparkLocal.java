package com.bigdistributor.distributor.aws.spark;

import com.bigdistributor.distributor.aws.spark.interf.SparkTask;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class TestSparkLocal {

    private static final int MAX_PARTITIONS = 15000;

    public static void main(String[] args) {
        Logger.getLogger("org").setLevel(Level.ERROR);
//	.setMaster("local[1]")
        SparkConf sparkConf = new SparkConf().setAppName("N5ConvertSpark");

        final JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

//        AmazonS3 s3 = S3ClientInstance.get();
//        JobID.set(DEFAULT.id);
//        ListObjectsV2Result result = s3.listObjectsV2(JobID.get());
//        List<S3ObjectSummary> objects = result.getObjectSummaries();
//        for (S3ObjectSummary os : objects) {
//            System.out.println("* " + os.getKey());
//        }

        final List<Long> outputBlockIndexes = LongStream.range(0, 1000).boxed().collect(Collectors.toList());

        sparkContext.parallelize(outputBlockIndexes, Math.min(outputBlockIndexes.size(), MAX_PARTITIONS)).foreach(new SparkTask());

    }

}