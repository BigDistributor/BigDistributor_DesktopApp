package com.bigdistributor.gui.bdv;

import bdv.util.BdvFunctions;
import bdv.util.BdvStackSource;
import bdv.util.volatiles.SharedQueue;
import bdv.util.volatiles.VolatileViews;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.utils.AWS_DEFAULT;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;
import org.janelia.saalfeldlab.n5.s3.N5AmazonS3Reader;

import java.io.IOException;

public class BDVN5 {
//    "s3://bigstitcher/data/dataset.n5/setup11/timepoint0/s0/"
    public static <T extends NativeType<T> & RealType<T>>  void main(String[] args) throws IOException {
        String path = "data/dataset.n5";
        String n5Dataset = "setup11/timepoint0/s0/";
        AWSCredentialInstance.init(AWS_DEFAULT.AWS_CREDENTIALS_PATH);
        String bucketName = "bigstitcher";
        AmazonS3 s3 = S3BucketInstance.init(AWSCredentialInstance.get(), Regions.EU_CENTRAL_1, bucketName, "").getS3();

        N5Reader reader = new N5AmazonS3Reader(s3,bucketName,path);
        System.out.println(reader.getVersion());
//        System.out.println(reader.getGroupSeparator());
        final RandomAccessibleInterval<T> img = N5Utils.openVolatile(reader, n5Dataset);

        Cursor<T> cursor = Views.flatIterable(img).cursor();

//        while (cursor.hasNext()) {
//            cursor.next();
//            System.out.println(cursor.get().getMaxValue());
//        }


        final SharedQueue queue = new SharedQueue(Math.max(1, Runtime.getRuntime().availableProcessors() - 1));
//        bdv = BdvFunctions.show(
//                VolatileViews.wrapAsVolatile(
//                        img,
//                        queue),
//                n5Dataset,
//                BdvOptions.options());
//        final N5Reader n5 = N5Factory.openAWSS3Reader(n5Url);



        BdvStackSource<?> bdv = BdvFunctions.show(
                VolatileViews.wrapAsVolatile(img,queue),
                n5Dataset);
        bdv.setDisplayRange(0, 3000);
//        bdv.setDisplayRange(24000, 32000);

    }
}
