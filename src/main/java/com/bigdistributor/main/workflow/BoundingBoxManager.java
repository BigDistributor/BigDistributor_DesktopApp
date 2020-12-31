package com.bigdistributor.main.workflow;

import mpicbg.spim.data.sequence.ViewId;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.process.boundingbox.BoundingBoxEstimation;
import net.preibisch.mvrecon.process.boundingbox.BoundingBoxMaximal;

import java.util.ArrayList;
import java.util.List;

public class BoundingBoxManager {

    private SpimData2 spimdata;

    public BoundingBoxManager(SpimData2 spimdata) {
        this.spimdata = spimdata;
    }

    public BoundingBox getMax() {
        final List<ViewId> viewIds = new ArrayList<ViewId>();
        viewIds.addAll(spimdata.getSequenceDescription().getViewDescriptions().values());
        return getMaxPerViews(viewIds);
    }

    public BoundingBox getMaxPerViews(List<ViewId> viewIds) {
        BoundingBox bbx = estimateBoundingBox(spimdata, viewIds);
        return bbx;
    }

    private static BoundingBox estimateBoundingBox(SpimData2 spimdata, List<ViewId> viewIds) {
        BoundingBoxEstimation estimation = new BoundingBoxMaximal(viewIds, spimdata);
        return estimation.estimate("Full Bounding Box");
    }
}
