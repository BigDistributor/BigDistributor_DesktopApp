package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.biglogger.adapters.Log;

public interface HeadlessFunction {
    Log logger = Log.getLogger(HeadlessFunction.class.getSimpleName());

    void start() throws Exception;
}
