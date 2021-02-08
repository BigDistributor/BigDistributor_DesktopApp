package com.bigdistributor.gui.wf.items;

import com.bigdistributor.helpers.TASK_DEFAULT;
import com.bigdistributor.io.mvn.MavenCompiler;
import fiji.util.gui.GenericDialogPlus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompileTaskView {
    public MavenCompiler show() {
        final GenericDialogPlus gd = new GenericDialogPlus("Compile");
        gd.addStringField("DependenciesProjects: ", TASK_DEFAULT.dependenciesProjects, 100);
        gd.addFileField("Main Project: ", TASK_DEFAULT.mainProject, 100);
        gd.addDirectoryField("Output maven: ", TASK_DEFAULT.outputMaven, 100);
        gd.addMessage("");

        gd.showDialog();

        if (gd.wasCanceled())
            return null;

        String dependencies = gd.getNextString();
        String mainProject = gd.getNextString();
        String outputMaven = gd.getNextString();
        List<String> dependenciesProjects = new ArrayList<>(Arrays.asList(dependencies.split(",")));
        return new MavenCompiler(dependenciesProjects, mainProject, outputMaven);

    }
}
