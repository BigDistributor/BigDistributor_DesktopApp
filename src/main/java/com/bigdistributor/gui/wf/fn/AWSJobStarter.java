package com.bigdistributor.gui.wf.fn;


import com.amazonaws.auth.AWSCredentials;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.job.aws.emr.EMRLambdaManager;
import com.bigdistributor.aws.job.aws.emr.EMRLambdaManagerParams;
import com.bigdistributor.gui.wf.items.ClusterTaskView;

public class AWSJobStarter implements HeadlessFunction {
    @Override
    public void start() throws Exception {
        EMRLambdaManagerParams params = new ClusterTaskView().show();
        AWSCredentials credentials = AWSCredentialInstance.get();

       new EMRLambdaManager(credentials, params).invoke();
    }
}
