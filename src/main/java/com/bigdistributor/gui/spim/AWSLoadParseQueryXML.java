package com.bigdistributor.gui.spim;

import com.bigdistributor.aws.job.utils.Params;
import com.bigdistributor.aws.spimloader.AWSXmlIoSpimData2;
import fiji.util.gui.GenericDialogPlus;
import net.preibisch.mvrecon.fiji.plugin.queryXML.LoadParseQueryXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AWSLoadParseQueryXML extends LoadParseQueryXML {
    private final static String defaultBucketName = "bigstitcher";
    private final static String defaultKeyPath = "/Users/Marwan/Desktop/BigDistributer/aws_credentials/bigdistributer.csv";
    private final static String defaultPath = "";
    private final static String defaultFileName = "dataset-n5.xml";


    public boolean queryXML() {
        final GenericDialogPlus gd = new GenericDialogPlus("AWS Input");
        gd.addFileField("Key: ", defaultKeyPath, 45);
        gd.addMessage("");
        gd.addStringField("Bucket name: ", defaultBucketName, 30);
        gd.addStringField("Path: ", defaultPath, 30);
        gd.addStringField("XML File: ", defaultFileName, 30);
        gd.showDialog();

        if (gd.wasCanceled())
            return false;

        String keyPath = gd.getNextString();
        String bucketName = gd.getNextString();
        String path = gd.getNextString();
        String xmlFile = gd.getNextString();

        AWSXmlIoSpimData2 result = null;
        try {
            Params.init(keyPath, bucketName, path, xmlFile);
//            result = new AWSXmlIoSpimData2(keyPath, bucketName, path, xmlFile);
            result = new AWSXmlIoSpimData2();
        } catch (IllegalAccessException e) {
            System.out.println(e.toString());
            return false;
        }

        if (!result.queryXML()) {
            return false;
        }
        this.data  = result.getData();
        this.attributes = getAttributes(this.data, this.comparator);
        int numAttributes = this.attributes.size();
        this.allAttributeInstances = new HashMap();
        ArrayList<HashSet<Integer>> numEntitiesPerAttrib = this.entitiesPerAttribute();
        this.populateAttributesEntities(numAttributes, numEntitiesPerAttrib);
        this.message1 = goodMsg1;
        this.message2 = getSpimDataDescription(this.data, this.attributes, numEntitiesPerAttrib, numAttributes);

        this.xmlfilename = xmlFile;
        this.io = result.getIO();

        return true;


    }
    public static void main(String[] args) {
		new AWSLoadParseQueryXML().queryXML();
	}

}
