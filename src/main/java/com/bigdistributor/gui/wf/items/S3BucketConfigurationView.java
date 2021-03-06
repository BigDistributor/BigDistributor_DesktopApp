package com.bigdistributor.gui.wf.items;

import com.amazonaws.regions.Regions;
import com.bigdistributor.aws.job.utils.AWSWorkflow;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.utils.AWS_DEFAULT;
import com.bigdistributor.biglogger.adapters.Log;
import fiji.util.gui.GenericDialogPlus;

public class S3BucketConfigurationView {
    private static final Log logger = Log.getLogger( S3BucketConfigurationView.class.getName());

    public boolean show() {
        final GenericDialogPlus gd = new GenericDialogPlus("AWS S3 Input");
        gd.addFileField("Key: ", AWS_DEFAULT.AWS_CREDENTIALS_PATH, 45);
        gd.addMessage("");
        gd.addStringField("Bucket name: ", AWS_DEFAULT.bucket_name, 30);
        gd.showDialog();

        if (gd.wasCanceled())
            return false;

        String credentialsKeyPath = gd.getNextString();
        String bucketName = gd.getNextString();
        logger.info("Bucket name: " + bucketName);
        logger.info("Credential path: " + credentialsKeyPath);

        AWSWorkflow.get().setCredentialsKeyPath(credentialsKeyPath);
        AWSWorkflow.get().setBucketName(bucketName);

        AWSCredentialInstance.init(credentialsKeyPath);
        S3BucketInstance.init(AWSCredentialInstance.get(), Regions.EU_CENTRAL_1, bucketName,"");

        return true;

    }
}
