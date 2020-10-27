package com.bigdistributor.distributor.aws.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import com.bigdistributor.distributor.aws.spark.interf.DistributedTask;
import com.bigdistributor.distributor.aws.spark.interf.TaskParams;

import javafx.concurrent.Task;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class JobRunner {

	private static final int MAX_PARTITIONS = 15000;

	private final String appName;
	private final int maxPartitions;
	private final DistributedTask task;
	private final List<Long> blockIndexes;

	public JobRunner(String appName, int max_partitions, DistributedTask task,
			List<Long> blockIndexes) {
		this.maxPartitions = max_partitions;
		this.task = task;
		this.blockIndexes = blockIndexes;
		this.appName = appName;
	}

	public void run() {
		Logger.getLogger("org").setLevel(Level.ERROR);
		// .setMaster("local[1]")
		SparkConf sparkConf = new SparkConf().setAppName(appName);

		final JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

		sparkContext.parallelize(blockIndexes, Math.min(blockIndexes.size(), maxPartitions)).foreach(task);
	}

}