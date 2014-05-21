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

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
