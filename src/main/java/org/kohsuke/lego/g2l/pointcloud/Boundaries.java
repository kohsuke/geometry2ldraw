package org.kohsuke.lego.g2l.pointcloud;

import java.io.File;

/**
 * Parses pointcloud dataset to compute 3D bounding box.
 *
 * @author Kohsuke Kawaguchi
 */
public class Boundaries {

    static class Range {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        void add(int i) {
            if (i>max)  max=i;
            if (i<min)  min=i;
        }

        void add(String s) {
            add(Integer.parseInt(s));
        }

        @Override
        public String toString() {
            return String.format("[%d,%d] (range=%d)",min,max, max-min);
        }
    }

    public static void main(String[] args) throws Exception {
        Range x = new Range();
        Range y = new Range();
        Range z = new Range();

        for (Point p : new PointcloudReader(new File("pointcloud.asc"))) {
            x.add(p.x);
            y.add(p.y);
            z.add(p.z);
        }

        System.out.println("x="+x);
        System.out.println("y="+y);
        System.out.println("z="+z);
    }
}
