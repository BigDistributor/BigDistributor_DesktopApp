package com.bigdistributor.gui.wf.items;


import com.bigdistributor.biglogger.adapters.Log;
import com.bigdistributor.helpers.TASK_DEFAULT;
import fiji.util.gui.GenericDialogPlus;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpimView {
    private static final Log logger = Log.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    public List<String> show() {
        final GenericDialogPlus gd = new GenericDialogPlus("Spim Input");
        gd.addFileField("XML: ", TASK_DEFAULT.xml_data, 45);
        gd.addMessage("");
        gd.addDirectoryOrFileField("RAW: ", TASK_DEFAULT.raw_data, 45);
        gd.showDialog();

        if (gd.wasCanceled())
            return null ;

        String xml = gd.getNextString();
        String raw = gd.getNextString();


        return new ArrayList<>(Arrays.asList(xml,raw));
    }


}
