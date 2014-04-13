package org.kohsuke.lego.g2l.pointcloud;

import org.kohsuke.lego.g2l.Array2D;
import org.kohsuke.lego.g2l.Array3D;
import org.kohsuke.lego.g2l.ldraw.Color;
import org.kohsuke.lego.g2l.ldraw.LDrawWriter;
import org.kohsuke.lego.g2l.ldraw.Part;

import java.io.File;
import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public class Renderer {

    public static void main(String[] args) throws IOException {
        int scale = 4;

        PointcloudReader r = new PointcloudReader(new File("pointcloud.asc"));

        Range xx = new Range();
        Range yy = new Range();
        Range zz = new Range();

        for (Point p : r) {
            xx.add(p.x);
            yy.add(p.y);
            zz.add(p.z);
        }

        Range ixx = new Range();
        Range iyy = new Range();
        Range izz = new Range();

        Array2D height = new Array2D(100,100);
        try (LDrawWriter w = new LDrawWriter(new File("pointcloud.ldr"))) {
            for (Point p : r) {
                int x = (p.x - xx.min) / scale / 20;
                int y = (p.y - yy.min) / scale / 20;
                int z = (p.z - zz.min) / scale / 8;

                ixx.add(x);
                iyy.add(y);
                izz.add(z);

                height.set(x, y, Math.max(height.get(x, y), z));
            }

            for (int x=0; x<height.xx; x++) {
                for (int y=0; y<height.yy; y++) {
                    int z = height.get(x,y);
                    if (z>0) {
                        w.write(x*20, y*20, z*8, Part.COLUMN1x1, Color.WHITE);
                    }
                }
            }
        }

        System.out.println("Dimension");
        System.out.println("x="+ixx);
        System.out.println("y="+iyy);
        System.out.println("z="+izz);
    }

    /**
     * Round the value to the nearby multiple of the given unit.
     */
    private static int quantitize(int v, int unit) {
        v = (v+unit-1)/unit;
        v *= unit;
        return v;
    }
}
