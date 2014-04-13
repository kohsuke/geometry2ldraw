package org.kohsuke.lego.g2l;

import java.lang.reflect.Array;

/**
 * @author Kohsuke Kawaguchi
 */
public class Array2D<T> {
    public final int xx,yy;
    final T[] data;

    public Array2D(Class<T> type, int x, int y) {
        this.xx = x;
        this.yy = y;
        data = (T[])Array.newInstance(type,x*y);
    }

    public T get(int x,int y) {
        return data[offset(x,y)];
    }

    public void set(int x, int y, T v) {
        data[offset(x,y)] = v;
    }

    private int offset(int x, int y) {
        if (x<0 || xx<=x
        ||  y<0 || yy<=y)
            throw new ArrayIndexOutOfBoundsException(String.format("x=%d,y=%d",x,y));

        return x + xx*y;
    }
}
