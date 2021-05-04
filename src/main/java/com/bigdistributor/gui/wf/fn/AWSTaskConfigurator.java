package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.aws.job.utils.AWSWorkflow;
import com.bigdistributor.gui.wf.items.OneFileView;

public class AWSTaskConfigurator implements HeadlessFunction {
    @Override
    public void start() throws Exception {
        String file = new OneFileView("AWS Task", "Task name: ", "", OneFileView.FileType.Text).show();
        AWSWorkflow.get().setClusterJar(file);
    }
}
