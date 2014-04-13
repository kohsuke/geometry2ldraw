package org.kohsuke.lego.g2l;

/**
 * @author Kohsuke Kawaguchi
 */
public class Array2D {
    public final int xx,yy;
    final int data[];

    public Array2D(int x, int y) {
        this.xx = x;
        this.yy = y;
        data = new int[x*y];
    }

    public int get(int x,int y) {
        return data[offset(x,y)];
    }

    public void set(int x, int y, int v) {
        data[offset(x,y)] = v;
    }

    private int offset(int x, int y) {
        if (x<0 || xx<=x
        ||  y<0 || yy<=y)
            throw new ArrayIndexOutOfBoundsException(String.format("x=%d,y=%d",x,y));

        return x + xx*y;
    }
}
