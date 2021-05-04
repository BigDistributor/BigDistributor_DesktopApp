package com.bigdistributor.gui.bdv;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import com.amazonaws.regions.Regions;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.spimloader.AWSSpimLoader;
import com.bigdistributor.aws.utils.AWS_DEFAULT;
import com.bigdistributor.core.blockmanagement.blockinfo.BasicBlockInfo;
import com.bigdistributor.core.metadata.MetadataGenerator;
import com.bigdistributor.core.remote.mq.MQLogReceiveDispatchManager;
import com.bigdistributor.core.remote.mq.entities.MQMessage;
import com.bigdistributor.core.remote.mq.entities.MQTopic;
import com.bigdistributor.core.remote.mq.entities.RemoteLogListener;
import com.bigdistributor.core.spim.SpimDataLoader;
import com.bigdistributor.core.task.JobID;
import com.bigdistributor.core.task.items.Metadata;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.Interval;
import net.imglib2.Localizable;
import net.imglib2.position.FunctionRandomAccessible;
import net.imglib2.type.numeric.ARGBType;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class BDVProgressive implements RemoteLogListener {
    private static BDVProgressive instance;
    private final SpimData2 spimdata;
    private final Map<Integer, ARGBType> blocksStatus;
    private final Metadata metadata;
    private BdvOptions options;

    private BDVProgressive(SpimData2 spimdata, Metadata metadata) {
        this.spimdata = spimdata;
        this.metadata = metadata;
        this.blocksStatus = generateBlockStatus(metadata);
    }

    public static BDVProgressive init(SpimData2 spimdata, Metadata metadata) {
        instance = new BDVProgressive(spimdata, metadata);
        return instance;
    }

    public static synchronized BDVProgressive get() {
        return instance;
    }

    public BDVProgressive show() {
        options = Bdv.options().numSourceGroups(2).frameTitle("Data with Progress");
        mainImages = BdvFunctions.show(spimdata, options);
        mainImages.forEach(bdvStackSource -> bdvStackSource.setDisplayRange(0, 1200));
        options.addTo(mainImages.get(0));
        return instance;
    }

    private static int[] getNumBlocks(Interval bb, long[] blocksize) {
        int[] numblocks = new int[blocksize.length];
        for (int i = 0; i < blocksize.length; i++) {
            long d = bb.max(i) - bb.min(i);
            numblocks[i] = (int) (d / blocksize[i]);
            if (blocksize[i] * numblocks[i] < d) {
                numblocks[i] = numblocks[i] + 1;
            }
        }
        return numblocks;
    }

    public BDVProgressive showProgress() {
        FunctionRandomAccessible<ARGBType> function = getFunction(metadata, blocksStatus);
        BdvStackSource<ARGBType> progressImage = BdvFunctions.show(function, metadata.getBb(), "", options);
        progressImage.setDisplayRange(0, 500);
        MQLogReceiveDispatchManager.addListener(this,true);
        return instance;
    }

    private Map<Integer, ARGBType> generateBlockStatus(Metadata metadata) {
        Map<Integer, ARGBType> binfoStats = new HashMap<>();

        for (BasicBlockInfo binfo : metadata.getBlocksInfo())
            binfoStats.put(binfo.getBlockId(), ProgressColor.NotStated.getRgb());

        return binfoStats;
    }

    private static List<BdvStackSource<?>> mainImages;

    private static void refresh() {
        mainImages.get(0).getBdvHandle().getViewerPanel().requestRepaint();
    }

    public static FunctionRandomAccessible<ARGBType> getFunction(Metadata md, Map<Integer, ARGBType> colors) {
        int[] numBlocks = getNumBlocks(md.getBb(), md.getBlocksize());
        FunctionRandomAccessible<ARGBType> board = new FunctionRandomAccessible<>(
                3,
                (location, value) -> {
                    ARGBType v = new ARGBType(0);
                    if (locationValid(location, md.getBb()))
                        v = colors.get(getBlockForPosition(md, location, numBlocks).getBlockId());
                    value.set(v);
                },
                ARGBType::new);
        return board;

    }

    public void setColor(Integer blockId, ARGBType color) {
        System.out.println("Set color "+blockId+" "+color.toString());
        blocksStatus.get(blockId).set(color);
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

    public static BasicBlockInfo getBlockForPosition(Metadata md, Localizable l, int[] numBlocks) {
        int[] gridPosition = new int[l.numDimensions()];
        for (int d = 0; d < l.numDimensions(); d++) {
            gridPosition[d] = (int) ((l.getIntPosition(d) - md.getBb().min(d)) / md.getBlocksize()[d]);
        }
        int position = 0;
        int totalBlocks = 1;
        for (int i = 0; i < l.numDimensions(); i++) {
            if (i > 0)
                totalBlocks *= numBlocks[i - 1];
            position += gridPosition[i] * totalBlocks;
        }
        return md.getBlocksInfo().get(position);
    }

    public static void main(String[] args) throws SpimDataException, IllegalAccessException {
        JobID.createNew();
        // We get this information from user
        AWSCredentialInstance.init(AWS_DEFAULT.AWS_CREDENTIALS_PATH);

        S3BucketInstance.init(AWSCredentialInstance.get(), Regions.EU_CENTRAL_1, "bigstitcher","data");

        // Init XML
        SpimDataLoader spimLoader =  AWSSpimLoader.init(S3BucketInstance.get().getS3(), "s3://bigstitcher/data/dataset-n5.xml");

        MetadataGenerator metadataGenerator = new MetadataGenerator(spimLoader);

        Metadata md = metadataGenerator.generate().getMetadata();

        BDVProgressive.init(spimLoader.getSpimdata(),md);

        BDVProgressive.get().show().showProgress();

        AtomicInteger pos = new AtomicInteger();
        new Thread(() -> {
            while(true) {
                int x = new Random().nextInt(14);
                try {
                    Thread.sleep(x);
                    BDVProgressive.get().onLogAdded(new MQMessage(MQTopic.TASK_DONE, "0", pos.get(), ""));
                    pos.getAndIncrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        System.out.println("Done");
    }

    @Override
    public void onLogAdded(MQMessage message) {
        if(!message.getTopic().equals(MQTopic.LOG)){
            if(message.getBlockId()>=0){
                int blockId = message.getBlockId();
                ARGBType color = ProgressColor.getColorFor(message.getTopic()).getRgb();
                setColor(blockId,color);
            }
        }
    }
}
