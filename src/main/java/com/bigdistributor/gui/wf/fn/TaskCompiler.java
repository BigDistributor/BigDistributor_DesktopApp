package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.aws.job.utils.AWSWorkflow;
import com.bigdistributor.gui.wf.items.CompileTaskView;
import com.bigdistributor.gui.wf.items.PopupMessage;
import com.bigdistributor.io.mvn.JarLooker;
import com.bigdistributor.io.mvn.MavenCompiler;

public class TaskCompiler implements HeadlessFunction{
    @Override
    public void start() throws Exception {
        MavenCompiler mavenCompiler = new CompileTaskView().show();
        if (mavenCompiler.start() == 0)
            PopupMessage.infoBox("Jar compiled", "Success");
        AWSWorkflow.get().setLocalJar(new JarLooker(mavenCompiler.getOutputPath(), mavenCompiler.getMainProject()).lookForMainJar().getAbsolutePath());
    }
}
