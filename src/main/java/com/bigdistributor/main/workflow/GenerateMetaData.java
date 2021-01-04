package com.bigdistributor.main.workflow;

import com.bigdistributor.aws.spimloader.SpimDataLoader;
import com.bigdistributor.controllers.blockmanagement.blockinfo.BasicBlockInfoGenerator;
import com.bigdistributor.controllers.blockmanagement.blockinfo.BlockInfoGenerator;
import com.bigdistributor.core.task.items.Metadata;
import com.bigdistributor.io.GsonIO;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

import java.io.File;

public class GenerateMetaData {

    private final SpimDataLoader loader;
    private BoundingBox bb;
    private Metadata md;

    public GenerateMetaData(SpimDataLoader loader) {
        this.loader = loader;
    }

    public void setBb(BoundingBox bb) {
        this.bb = bb;
    }

    public GenerateMetaData generate() {
        if (bb == null) {
            BoundingBoxManager manager = new BoundingBoxManager(loader.getSpimdata());
            bb = manager.getMax();
        }

        long[] blockSize = BlockInfoGenerator.getBlockSizes(bb.numDimensions());
        BasicBlockInfoGenerator generator = new BasicBlockInfoGenerator(bb, blockSize);

        this.md = new Metadata(bb, blockSize, generator.divideIntoBlockInfo());
        return this;
    }

    public void save(File f) {
        if (md == null) {
            generate();
        }
        GsonIO.toJson(md, f);
        reset();

    }

    private void reset() {
        md = null;
        bb = null;
    }
}
