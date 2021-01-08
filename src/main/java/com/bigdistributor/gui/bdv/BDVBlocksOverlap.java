package com.bigdistributor.gui.bdv;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import com.bigdistributor.core.blockmanagement.BoundingBoxManager;
import com.bigdistributor.core.blockmanagement.blockinfo.BasicBlockInfo;
import com.bigdistributor.core.blockmanagement.blockinfo.BasicBlockInfoGenerator;
import com.bigdistributor.core.blockmanagement.blockinfo.BlockInfoGenerator;
import com.bigdistributor.io.mvrecon.SpimHelpers;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.Cursor;
import net.imglib2.Localizable;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.position.FunctionRandomAccessible;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.view.Views;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import net.preibisch.mvrecon.fiji.spimdata.explorer.util.ColorStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

//TODO ask Stephan for help, overlap this in BDV
public class BDVBlocksOverlap {
    private final static int[] mins = new int[]{0, 0, 0};
    private final static int[] maxs = new int[]{800, 880, 1000};

    public static void main(String[] args) throws SpimDataException {
        String spimPath = "/Users/Marwan/Desktop/Task/data/hdf5/dataset.xml";
        SpimData2 spimdata = new XmlIoSpimData2("").load(spimPath);
        BdvOptions options = Bdv.options().numSourceGroups( 2 ).frameTitle( "Data with Progress" );
        List<BdvStackSource<?>> mainImages = BdvFunctions.show(spimdata, options);
        mainImages.forEach(bdvStackSource -> bdvStackSource.setDisplayRange(0,100));

        mainImages.get(1).setColor( new ARGBType( ARGBType.rgba( 0, 255, 0, 0 ) ) );

        options.addTo(mainImages.get(0));

        long[] blockSizes = new long[mins.length];
        Arrays.fill(blockSizes, BlockInfoGenerator.BLOCK_SIZE);

        BoundingBox bb = new BoundingBoxManager(spimdata).getMax();

        BlockInfoGenerator generator = new BasicBlockInfoGenerator(bb, blockSizes);
        generator.divideIntoBlockInfo();


        FunctionRandomAccessible<ARGBType> function = getFunction(generator,bb);

        BdvStackSource<ARGBType> progressImage = BdvFunctions.show(function, bb, "", options);
        progressImage.setDisplayRange(0,50);

        System.out.println("Done");
    }

    static Img<ARGBType> getBlocksImg(List<BasicBlockInfo> blockInfos, BoundingBox bb, ExecutorService executor) {
        ArrayImg<ARGBType, IntArray> img = ArrayImgs.argbs(bb.getDimensions(1));


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

    public static FunctionRandomAccessible<ARGBType> getFunction(BlockInfoGenerator generator, BoundingBox bb) {
        FunctionRandomAccessible<ARGBType> board = new FunctionRandomAccessible<>(
                3,
                (location, value) -> {
                    List<ARGBType> colors = getColors(1000);
                    ARGBType v = new ARGBType(0);
                    if (locationValid(location, bb))
                        v = colors.get(generator.getBlockForPosition(location).getBlockId());
                    value.set(v);
                },
                ARGBType::new);
        return board;

    }

    private static List<ARGBType> getColors(int count) {
        Iterator<ARGBType> colorIterator = ColorStream.iterator();
        List<ARGBType> colors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            colors.add(colorIterator.next());
        }
        return colors;
    }

    private static boolean locationValid(Localizable location, BoundingBox bb) {

        for (int i = 0; i < bb.numDimensions(); i++) {
            if (location.getIntPosition(i) > bb.max(i))
                return false;
            if (location.getIntPosition(i) < bb.min(i))
                return false;
        }
        return true;
    }
}
