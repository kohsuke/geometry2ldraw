package org.kohsuke.lego.g2l.pointcloud;

/**
 * @author Kohsuke Kawaguchi
 */
class Range {
    int max = Integer.MIN_VALUE;
    int min = Integer.MAX_VALUE;

    void add(int i) {
        if (i>max)  max=i;
        if (i<min)  min=i;
    }

    void add(String s) {
        add(Integer.parseInt(s));
    }

    /**
     * Middle.
     */
    int mid() {
        return max/2;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d] (range=%d)",min,max, max-min);
    }
}
