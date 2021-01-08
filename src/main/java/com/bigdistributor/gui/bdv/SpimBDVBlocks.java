package com.bigdistributor.gui.bdv;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvStackSource;
import com.bigdistributor.core.blockmanagement.blockinfo.BasicBlockInfo;
import com.bigdistributor.core.blockmanagement.blockinfo.BasicBlockInfoGenerator;
import com.bigdistributor.core.blockmanagement.blockinfo.BlockInfoGenerator;
import com.bigdistributor.io.mvrecon.SpimHelpers;
import net.imglib2.Cursor;
import net.imglib2.Localizable;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.position.FunctionRandomAccessible;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.view.Views;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.fiji.spimdata.explorer.util.ColorStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO ask Stephan for help, overlap this in BDV
public class SpimBDVBlocks {
    private final static int[] mins = new int[]{0, 0, 0};
    private final static int[] maxs = new int[]{800, 880, 1000};

    public static void main(String[] args)   {
        long[] blockSizes = new long[mins.length];
        Arrays.fill(blockSizes, BlockInfoGenerator.BLOCK_SIZE);

        BoundingBox bb = new BoundingBox(mins, maxs);

        BlockInfoGenerator generator = new BasicBlockInfoGenerator(bb, blockSizes);
        List<BasicBlockInfo> blockInfos = generator.divideIntoBlockInfo();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        Img<ARGBType> blocksImg = getBlocksImg(blockInfos, bb, executor);

        FunctionRandomAccessible<ARGBType> function = getFunction(blocksImg);

        BdvStackSource<ARGBType> progressImage = BdvFunctions.show(function, bb, "", Bdv.options());
        progressImage.setDisplayRange(0,1);

    }

    static Img<ARGBType> getBlocksImg(List<BasicBlockInfo> blockInfos, BoundingBox bb, ExecutorService executor) {
        ArrayImg<ARGBType, IntArray> img = ArrayImgs.argbs(bb.getDimensions(1));

        // to move bb
//        Views.translate()

//        no need translate here
        //no img needed, direct function

        Iterator<ARGBType> colorIterator = ColorStream.iterator();

        for (BasicBlockInfo blockInfo : blockInfos) {
            Runnable runnable = () -> {
//                ThreadLocalRandom.current().nextInt(1, 10);
//                int x = blockInfo.getBlockId();
                int x = colorIterator.next().get();
                BoundingBox localBb = SpimHelpers.getBb(blockInfo);

                Cursor<ARGBType> cursor = Views.interval(img, localBb).cursor();
                while (cursor.hasNext()) {
                    cursor.fwd();
                    cursor.get().set(x);
                }
            };
            executor.submit(runnable);
        }
        return img;
    }

    public static FunctionRandomAccessible<ARGBType> getFunction(RandomAccessibleInterval<ARGBType> img) {
        FunctionRandomAccessible<ARGBType> board = new FunctionRandomAccessible<>(
                3,
                (location, value) -> {
//                    BInfoFunctions
                    ARGBType v = new ARGBType(0);
                    if (locationValid(location, img))
                        v = img.getAt(location.getIntPosition(0), location.getIntPosition(2), location.getIntPosition(2));
                    value.set(v);
                },
                ARGBType::new);
        return board;

    }

    private List<ARGBType> getColors(int count) {
        Iterator<ARGBType> colorIterator = ColorStream.iterator();
        List<ARGBType> colors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            colors.add(colorIterator.next());
        }
        return colors;
    }

    private static boolean locationValid(Localizable location, RandomAccessibleInterval<ARGBType> img) {
        for (int i = 0; i < img.numDimensions(); i++) {
            if (location.getIntPosition(i) > img.dimension(i))
                return false;
            if (location.getIntPosition(i) < 0)
                return false;
        }
        return true;
    }
}
