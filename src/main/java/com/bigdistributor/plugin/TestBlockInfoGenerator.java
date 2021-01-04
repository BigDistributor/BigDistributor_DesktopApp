package com.bigdistributor.plugin;

import com.bigdistributor.core.blockmanagement.blockinfo.BasicBlockInfo;
import com.bigdistributor.core.blockmanagement.blockinfo.BasicBlockInfoGenerator;
import com.bigdistributor.core.blockmanagement.blockinfo.BlockInfoGenerator;
import com.bigdistributor.io.mvrecon.SpimHelpers;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

//TODO ask Stephan for help, overlap this in BDV
public class TestBlockInfoGenerator {
    private final static long[] mins = new long[]{0, 0, 0};
    private final static long[] maxs = new long[]{800, 880, 1000};

    public static void main(String[] args) {

        Img<FloatType> img = ArrayImgs.floats(maxs);

        long[] blockSizes = new long[mins.length];
        Arrays.fill(blockSizes, BlockInfoGenerator.BLOCK_SIZE);

        BlockInfoGenerator generator = new BasicBlockInfoGenerator(mins, maxs, blockSizes);

        List<BasicBlockInfo> binfos = generator.divideIntoBlockInfo();

        for (BasicBlockInfo binfo : binfos) {
            int x = ThreadLocalRandom.current().nextInt(1, 50);
            BoundingBox bb = SpimHelpers.getBb(binfo);

            Cursor<FloatType> cursor = Views.interval(img, bb).cursor();
            while (cursor.hasNext()) {
                cursor.fwd();
                cursor.get().set(x);
            }
        }
        ImageJFunctions.show(img);
    }
}
