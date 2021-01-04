//package com.bigdistributor.main.workflow;
//
//import com.bigdistributor.aws.spimloader.SpimDataLoader;
//import com.bigdistributor.controllers.blockmanagement.blockinfo.BasicBlockInfo;
//import com.bigdistributor.controllers.blockmanagement.blockinfo.BasicBlockInfoGenerator;
//import com.bigdistributor.core.task.items.Job;
//import com.bigdistributor.core.tools.ArrayHelpers;
//import com.bigdistributor.io.GsonIO;
//import mpicbg.spim.data.SpimDataException;
//import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
//import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
//import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class GenerateMetaData {
//
//	private final SpimDataLoader loader;
//	private BoundingBox bb;
//
//	public GenerateMetaData(SpimDataLoader loader) {
//    	this.loader = loader;
//    }
//
//	public void setBb(BoundingBox bb) {
//		this.bb = bb;
//	}
//
//	public BoundingBox generate(){
//		if( bb == null){
//			BoundingBoxManager manager = new BoundingBoxManager(loader.getSpimdata());
//			bb =  manager.getMax();
//		}
//
//		generator  = new BasicBlockInfoGenerator(bb)
//
//		Map<Integer, BasicBlockInfo> blocksInfo = ;
//		Metadata md = new Metadata(Job.get().getId(),inputCluster,outptutCluster,new BoundingBox(interval), bsizes,blocksInfo);
//
//		File metadataFile = Job.get().file(i + "_metadata.json");
//		MyLogger.log().info(md.toString());
//
//
//
//    }
//
//    public static void run(List<ParamsJsonSerialzer<?>> params, String input, TaskType type, Interval interval) throws SpimDataException, IOException, JSchException, SftpException {
//		Job.create();
//		String path = "";
//		SpimData2 spimdata = new XmlIoSpimData2("").load(path);
//		spimdata.getBoundingBoxes()
////		List<File> relatedFiles = XMLFile.initRelatedFiles(new File(xml));
//		XMLFile inputFile = XMLFile.XMLFile(input);
//		long[] bsizes = ArrayHelpers.fill(BasicBlockInfoGenerator.BLOCK_SIZE, inputFile.getDimensions().length);
////		PreviewUI ui = new PreviewUI(inputFile, params.getViewIds().size());
////		Connection.login();
//		// SCPManager.createClusterFolder(clusterFolderName);
//
//		File taskFile = new File(TaskType.getTaskFile(type));
//		inputFile.getRelatedFiles().add(taskFile);
//		String inputCluster = Job.get().subfile(inputFile.getName());
//		SCPManager.sendInput(inputFile, Job.get().getCluster());
//
//		for (int i = 0; i < params.size(); i++) {
//
//			String output_name = i + "_output.n5";
//			MyLogger.log().info(params.toString());
//
////			N5File outputFile = new N5File(Job.get().file(output_name).getAbsolutePath(), inputFile.getDims(),
////					(int)BasicBlockInfoGenerator.BLOCK_SIZE);
////			System.out.println("Blocks: " + Util.printCoordinates(outputFile.getBlocksize()));
//			String outptutCluster = Job.get().subfile(output_name);
//			Map<Integer, BasicBlockInfo> blocksInfo = BasicBlockInfoGenerator.divideIntoBlockInfo(inputFile.bb());
//			Metadata md = new Metadata(Job.get().getId(),inputCluster,outptutCluster,new BoundingBox(interval), bsizes,blocksInfo);
//
//			File metadataFile = Job.get().file(i + "_metadata.json");
//			MyLogger.log().info(md.toString());
//			Job.get().setTotalbBlocks(md.size());
//			// md.toJson(metadataFile);
//			GsonIO.toJson(md, metadataFile);
//			File paramFile = Job.get().file(i + "_param.json");
//			params.get(i).toJson(paramFile);
//
//			// Generate script
//
//			String taskScriptName = i + TASK_SHELL_NAME;
//			File scriptFile = Job.get().file(taskScriptName);
//
//			String metadataCluster =Job.get().subfile(metadataFile.getName());
//
//			String paramCluster = Job.get().subfile(paramFile.getName());
//			System.out.println("Param cluster: "+paramCluster);
//
//
//			SGEClusterScript.generateTaskScript(scriptFile, taskFile.getName(), metadataCluster,
//					inputCluster, outptutCluster,paramCluster,i);
//
//			// Task to prepare N5
//			String prepareScriptName = i + JobType.file(JobType.PREPARE);
//			File prepareShell = Job.get().file(prepareScriptName);
//			SGEClusterScript.generateTaskScript(JobType.PREPARE, prepareShell, taskFile.getName(),
//					metadataCluster, inputCluster, outptutCluster, paramCluster,i);
//
//			// Generate batch
//
//			String batchScriptName = i + BATCH_NAME;
//			File batchScriptFile = Job.get().file(batchScriptName);
//			SGESubmitFile.generate(batchScriptFile, Job.get().getCluster().getAbsolutePath(), md.size(), i, prepareScriptName,
//					taskScriptName); // md.getTotal()
//
//			// send all
//			List<File> toSend = new ArrayList<>();
//			toSend.add(metadataFile);
//			toSend.add(batchScriptFile);
//			toSend.add(paramFile);
//			toSend.add(scriptFile);
//			toSend.add(prepareShell);
//
//			SCPManager.send(toSend, Job.get().getCluster());
//
//			// Run
//
//			SCPManager.startBatch(new File(Job.get().getCluster().getPath(),batchScriptFile.getName()));
//
//		}
//		new KafkaMessageManager(Job.get().getId(), params.size());
//
//	}
//}
