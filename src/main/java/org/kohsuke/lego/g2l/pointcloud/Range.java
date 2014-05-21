package org.kohsuke.lego.g2l.pointcloud;

import java.util.Iterator;

/**
 * @author Kohsuke Kawaguchi
 */
class Range implements Iterable<Integer> {
    int max = Integer.MIN_VALUE;
    int min = Integer.MAX_VALUE;

    Range() {
    }

    Range(int min, int max) {
        this.max = max;
        this.min = min;
    }

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

    int size() {
        return max-min;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int i = min;
            @Override
            public boolean hasNext() {
                return i<max;
            }

            @Override
            public Integer next() {
                return i++;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        return String.format("[%d,%d] (range=%d)",min,max, size());
    }
}
