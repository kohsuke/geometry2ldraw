package org.kohsuke.lego.g2l.pointcloud;

/**
 * 3D array.
 *
 * @author Kohsuke Kawaguchi
 */
class ThreeDimensionArray {
    final int xx,yy,zz;
    final int data[];

    ThreeDimensionArray(int x, int y, int z) {
        this.xx = x;
        this.yy = y;
        this.zz = z;
        data = new int[x*y*z];
    }

    public int get(int x,int y, int z) {
        return data[offset(x,y,z)];
    }

    public void set(int x, int y, int z, int v) {
        data[offset(x,y,z)] = v;
    }

    private int offset(int x, int y, int z) {
        if (x<0 || xx<=x
        ||  y<0 || yy<=y
        ||  z<0 || zz<=z)
            throw new ArrayIndexOutOfBoundsException(String.format("x=%d,y=%d,z=%d",x,y,z));

        return x + xx*y + xx*yy*z;
    }
}
