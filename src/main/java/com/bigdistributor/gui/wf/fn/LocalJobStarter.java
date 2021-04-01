package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.gui.wf.items.LocalTaskView;
import com.bigdistributor.aws.job.local.JarExecutor;
import com.bigdistributor.aws.job.local.LocalTaskParams;

public class LocalJobStarter implements HeadlessFunction {
    @Override
    public void start() throws Exception {
        LocalTaskParams params = new LocalTaskView().show();
        new JarExecutor(params).run();
    }
}
