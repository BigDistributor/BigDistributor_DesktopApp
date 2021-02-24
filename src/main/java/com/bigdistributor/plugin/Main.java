package com.bigdistributor.plugin;

import com.bigdistributor.biglogger.adapters.LoggerManager;
import com.bigdistributor.core.app.ApplicationMode;
import com.bigdistributor.core.app.BigDistributorApp;
import com.bigdistributor.core.app.BigDistributorMainApp;
import com.bigdistributor.core.generic.InvalidApplicationModeException;
import com.bigdistributor.gui.wf.AWSMenuView;

import java.awt.*;

@BigDistributorApp(mode = ApplicationMode.DistributionMasterFiji)
public class Main extends BigDistributorMainApp {
    public Main() throws InvalidApplicationModeException {
        super();
        LoggerManager.initLoggers();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> ((Runnable) () -> new AWSMenuView().setVisible(true)).run());
    }
}
