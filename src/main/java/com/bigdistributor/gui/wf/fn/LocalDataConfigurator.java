package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.aws.job.utils.AWSWorkflow;
import com.bigdistributor.gui.wf.items.SpimView;

import java.util.List;

public class LocalDataConfigurator implements HeadlessFunction {
    @Override
    public void start() throws Exception {
        List<String> files = new SpimView().show();
        AWSWorkflow.get().setLocalData(files);
    }
}
