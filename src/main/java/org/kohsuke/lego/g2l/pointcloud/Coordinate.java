package org.kohsuke.lego.g2l.pointcloud;

/**
 * @author Kohsuke Kawaguchi
 */
public class Coordinate {
    public final int x,y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate add(int dx, int dy) {
        return new Coordinate(x+dx,y+dy);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
