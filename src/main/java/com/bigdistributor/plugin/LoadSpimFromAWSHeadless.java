package com.bigdistributor.plugin;

import bdv.img.awsspim.AWSSpimImageLoader;
import com.amazonaws.regions.Regions;
import com.bigdistributor.aws.dataexchange.aws.s3.func.auth.AWSCredentialInstance;
import com.bigdistributor.aws.dataexchange.aws.s3.func.bucket.S3BucketInstance;
import com.bigdistributor.aws.dataexchange.utils.DEFAULT;
import com.bigdistributor.aws.spimloader.AWSSpimLoader;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;

public class LoadSpimFromAWSHeadless {

    public static void main(String[] args) throws IllegalAccessException {

          // Init S3
        AWSCredentialInstance.init(DEFAULT.AWS_CREDENTIALS_PATH);

        S3BucketInstance.init(AWSCredentialInstance.get(), Regions.EU_CENTRAL_1, DEFAULT.bucket_name);

        // Init XML
        AWSSpimLoader reader = new AWSSpimLoader(S3BucketInstance.get(),"","dataset-n5.xml");

        //Init Spim

        SpimData2 data = reader.getSpimdata();

        System.out.println("Class: " + data.getSequenceDescription().getImgLoader().getClass());

//
        AWSSpimImageLoader loader = (AWSSpimImageLoader) data.getSequenceDescription().getImgLoader();

        RandomAccessibleInterval<UnsignedShortType> img = (RandomAccessibleInterval<UnsignedShortType>) loader.getSetupImgLoader(0).getImage(1, null);
        ImageJFunctions.show(img);

    }
}