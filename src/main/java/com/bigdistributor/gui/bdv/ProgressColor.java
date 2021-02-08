package com.bigdistributor.gui.bdv;

import net.imglib2.type.numeric.ARGBType;

public enum ProgressColor {
    Error(231, 0, 0,0),
    Success(0, 255, 0,0),
    Processing(241, 196, 0,0),
    NotStated(20, 20, 20,0);
    private final int code;
    private final int r,g,b,a;

    ProgressColor(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        this.code = argb(r, g, b);
    }

    public int getCode() {
        return code;
    }

    public ARGBType getRgb() {
        return new ARGBType(ARGBType.rgba( r, g, b, a ));
    }

    protected static final int argb(int r, int g, int b) {
        return (r << 8 | g) << 8 | b | -16777216;
    }
}
