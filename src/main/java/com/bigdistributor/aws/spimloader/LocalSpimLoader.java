package com.bigdistributor.aws.spimloader;

import mpicbg.spim.data.SpimDataException;
import net.preibisch.mvrecon.fiji.spimdata.SpimData2;
import net.preibisch.mvrecon.fiji.spimdata.XmlIoSpimData2;

public class LocalSpimLoader implements SpimDataLoader {

    private final String path;

    public LocalSpimLoader(String path) {
        this.path = path;
    }

    @Override
    public SpimData2 getSpimdata() {
        SpimData2 spimdata = null;
        try {
            spimdata = new XmlIoSpimData2("").load(path);
        } catch (SpimDataException e) {
            e.printStackTrace();
        }
        return spimdata;
    }
}
