package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.aws.job.utils.AWSWorkflow;
import com.bigdistributor.gui.wf.items.OneFileView;
import com.bigdistributor.helpers.TASK_DEFAULT;

public class LocalTaskConfigurator implements HeadlessFunction{
    @Override
    public void start() throws Exception {
        String file = new OneFileView("Local Task", "Task Path", TASK_DEFAULT.jar, OneFileView.FileType.File).show();
        AWSWorkflow.get().setLocalJar(file);
    }
}
