package com.bigdistributor.gui.bdv;


import net.imglib2.type.numeric.ARGBType;

import java.util.Iterator;

public class ColorStream {
    protected static final double goldenRatio = 0.5D * Math.sqrt(5.0D) + 0.5D;
    protected static final double stepSize;
    protected static final double[] rs;
    protected static final double[] gs;
    protected static final double[] bs;
    static long i;

    public ColorStream() {
    }

    protected static final int interpolate(double[] xs, int k, int l, double u, double v) {
        return (int)((v * xs[k] + u * xs[l]) * 255.0D + 0.5D);
    }

    protected static final int argb(int r, int g, int b) {
        return (r << 8 | g) << 8 | b | -16777216;
    }

    public static final int get(long index) {
        double x = goldenRatio * (double)index;
        x -= (double)((long)x);
        x *= 6.0D;
        int k = (int)x;
        int l = k + 1;
        double u = x - (double)k;
        double v = 1.0D - u;
        int r = interpolate(rs, k, l, u, v);
        int g = interpolate(gs, k, l, u, v);
        int b = interpolate(bs, k, l, u, v);
        System.out.println("r:"+r+" g:"+g+" b:"+b);
        return argb(r, g, b);
    }

    public static final int next() {
        return get(++i);
    }

    public static final Iterator<ARGBType> iterator() {
        return new Iterator<ARGBType>() {
            long i = -1L;

            public boolean hasNext() {
                return true;
            }

            public ARGBType next() {
                return new ARGBType(ColorStream.get(++this.i));
            }

            public void remove() {
            }
        };
    }

    static {
        stepSize = 6.0D * goldenRatio;
        rs = new double[]{1.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D};
        gs = new double[]{0.0D, 1.0D, 1.0D, 1.0D, 0.0D, 0.0D, 0.0D};
        bs = new double[]{0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 0.0D};
        i = -1L;
    }
}
