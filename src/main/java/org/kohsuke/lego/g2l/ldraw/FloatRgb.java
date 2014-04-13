package org.kohsuke.lego.g2l.ldraw;

/**
 * @author Kohsuke Kawaguchi
 */
public final class FloatRgb {
    final float r,g,b;

    public FloatRgb(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public FloatRgb(int rgb) {
        this(
                (float)((rgb>>16)&0xFF),
                (float)((rgb>>8)&0xFF),
                (float)(rgb&0xFF)
        );
    }

    public FloatRgb times(float f) {
        return new FloatRgb(r*f,g*f,b*f);
    }

    public FloatRgb plus(FloatRgb that) {
        return new FloatRgb(this.r+that.r, this.g+that.g, this.b+that.b);
    }

    public FloatRgb minus(FloatRgb that) {
        return new FloatRgb(this.r-that.r, this.g-that.g, this.b-that.b);
    }

    public int toInt() {
        return (to256(r)<<16) | (to256(g)<<8) | to256(b);
    }

    private int to256(float r) {
        int i = (int) r;
        if (i<0)    return 0;
        if (i>255)  return 255;
        return i;
    }
}
