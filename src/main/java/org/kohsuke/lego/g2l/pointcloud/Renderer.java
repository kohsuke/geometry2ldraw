package org.kohsuke.lego.g2l.pointcloud;

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

        Array3D data = new Array3D(80,80,100);
        try (LDrawWriter w = new LDrawWriter(new File("pointcloud.ldr"))) {
            for (Point p : r) {
                int x = quantitize((p.x - xx.min) / scale, 20);
                int y = quantitize((p.y - yy.min) / scale, 20);
                int z = quantitize((p.z - zz.min) / scale, 8);

                // duplicate checker array index
                int ix = x/20;
                int iy = y/20;
                int iz = z/8;

                if (data.get(ix,iy,iz)==0) {
                    data.set(ix,iy,iz, 1);
                    w.write(x, y, z, Part.PLATE1x1, Color.WHITE);
                }

                ixx.add(ix);
                iyy.add(iy);
                izz.add(iz);
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
