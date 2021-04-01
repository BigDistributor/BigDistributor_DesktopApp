package com.bigdistributor.gui.wf;

import com.bigdistributor.biglogger.adapters.Log;
import com.bigdistributor.gui.wf.fn.HeadlessFunction;
import com.bigdistributor.gui.wf.items.PopupMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FunctionsExecutor {

    private static final Log logger = Log.getLogger(FunctionsExecutor.class.getSimpleName());
    static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void run(HeadlessFunction function) {
        executor.submit(() -> {
            try {
                String functionName = function.getClass().getSimpleName();
                logger.info("Starting: " + functionName);
                function.start();
                PopupMessage.infoBox(functionName + ": done!", "Success ");
            } catch (Exception e) {
                logger.error(e.toString());
            }
        });
    }
}
