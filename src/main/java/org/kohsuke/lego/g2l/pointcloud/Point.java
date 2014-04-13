package org.kohsuke.lego.g2l.pointcloud;

/**
 * @author Kohsuke Kawaguchi
 */
public class Point {
    public final int x,y,z;
    public final int r,g,b;

    public Point(int x, int y, int z, int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Point parse(String line) {
        String[] t = line.split(" ");
        if (t.length!=6)   throw new IllegalArgumentException(line);
        return new Point(i(t[0]),i(t[1]),i(t[2]),i(t[3]),i(t[4]),i(t[5]));
    }

    private static int i(String s) {
        return Integer.parseInt(s);
    }

    public int rgb() {
        return (r&0xFF)<<16 | (g&0xFF)<<8 | (b&0xFF);
    }
}
