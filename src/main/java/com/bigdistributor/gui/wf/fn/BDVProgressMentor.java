package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.gui.bdv.BDVProgressive;

public class BDVProgressMentor implements HeadlessFunction {
    @Override
    public void start() throws Exception {
        BDVProgressive.get().showProgress();
    }
}
