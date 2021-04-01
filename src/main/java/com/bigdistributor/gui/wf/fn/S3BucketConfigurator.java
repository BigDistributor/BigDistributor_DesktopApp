package com.bigdistributor.gui.wf.fn;

import com.bigdistributor.gui.wf.items.S3BucketConfigurationView;

public class S3BucketConfigurator implements HeadlessFunction {
    @Override
    public void start() throws Exception {
        new S3BucketConfigurationView().show();
    }
}
