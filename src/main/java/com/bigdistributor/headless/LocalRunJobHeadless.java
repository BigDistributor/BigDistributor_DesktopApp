//package com.bigdistributor.headless;
//
//import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
//import com.bigdistributor.aws.dataexchange.utils.AWS_DEFAULT;
//import com.bigdistributor.aws.job.utils.JarParams;
//import com.bigdistributor.biglogger.adapters.Log;
//import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
//
//import java.io.IOException;
//
//public class LocalRunJobHeadless {
//
//    private static final Log logger = Log.getLogger(LocalRunJobHeadless.class.getSimpleName());
//
//    public static void main(String[] args) throws IOException, XmlPullParserException {
////        AWSWorkflow.get().setCredentialsKeyPath(AWS_DEFAULT.AWS_CREDENTIALS_PATH);
////        AWSWorkflow.get().setBucketName(AWS_DEFAULT.bucket_name);
//        AWSCredentialInstance.init(AWS_DEFAULT.AWS_CREDENTIALS_PATH);
////        S3BucketInstance.init(AWSCredentialInstance.get(), Regions.EU_CENTRAL_1, AWS_DEFAULT.bucket_name);
//
////        String jarFile = new JarLooker(TASK_DEFAULT.outputMaven, TASK_DEFAULT.mainProject).lookForMainJar().getAbsolutePath();
////        logger.info("Jar file: "+jarFile);
////        AWSWorkflow.get().setLocalJar(jarFile);
//
//        String input = "dataset-n5.xml";
//        String output = "test_output.n5";
//        String metadata = "metadata.xml";
//        JarParams params = new JarParams("fusion", "TestLocal", AWS_DEFAULT.bucket_name,
//                input,
//                output, metadata,
//                "", AWSCredentialInstance.get());
//
//        new Dispatcher
//
////        get jar
//
////        get data
//
////        start
//    }
//}
