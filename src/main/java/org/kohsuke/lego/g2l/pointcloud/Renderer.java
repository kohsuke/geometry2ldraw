package org.kohsuke.lego.g2l.pointcloud;

import org.kohsuke.lego.g2l.Array2D;
import org.kohsuke.lego.g2l.ldraw.Color;
import org.kohsuke.lego.g2l.ldraw.FloatRgb;
import org.kohsuke.lego.g2l.ldraw.LDrawWriter;
import org.kohsuke.lego.g2l.ldraw.Part;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public class Renderer {

    static class Tag {
        int z;
        List<Integer> rgbs = new ArrayList<>();

        Tag(int z, int rgb) {
            this.z = z;
            this.rgbs.add(rgb);
        }

        public void merge(int z, int rgb) {
            if (z > this.z) {
                this.z = z;
            }
            this.rgbs.add(rgb);
        }

        public int rgb() {
            long r=0,g=0,b=0;
            for (int rgb : rgbs) {
                r += (rgb>>16)&0xFF;
                g += (rgb>>8)&0xFF;
                b += (rgb   )&0xFF;
            }

            r /= rgbs.size();
            g /= rgbs.size();
            b /= rgbs.size();

            return (int)((r<<16)|(g<<8)|b);
        }
    }

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

        Array2D<Tag> height = new Array2D<Tag>(Tag.class, 100,100);
        try (LDrawWriter w = new LDrawWriter(new File("pointcloud.ldr"))) {
            for (Point p : r) {
                int x = (p.x - xx.min) / scale / 20;
                int y = (p.y - yy.min) / scale / 20;
                int z = (p.z - zz.min) / scale / 8;

                ixx.add(x);
                iyy.add(y);
                izz.add(z);

                Tag t = height.get(x,y);
                if (t==null) {
                    t = new Tag(z, p.rgb());
                    height.set(x, y, t);
                } else
                    t.merge(z, p.rgb());
            }

            // convert to color map
            Array2D<FloatRgb> colors = new Array2D<>(FloatRgb.class, height.xx, height.yy);
            for (int x=0; x<colors.xx; x++) {
                for (int y=0; y<colors.yy; y++) {
                    Tag t = height.get(x, y);
                    if (t!=null) {
                        colors.set(x,y, new FloatRgb(t.rgb()));
                    } else {
                        colors.set(x, y, new FloatRgb(0));
                    }
                }
            } 
            dither(colors);

            for (int x=0; x<height.xx; x++) {
                for (int y=0; y<height.yy; y++) {
                    Tag t = height.get(x, y);
                    if (t!=null) {
                        w.write(x*20, y*20, t.z*8, Part.COLUMN1x1, Color.nearest(colors.get(x,y).toInt()));
                    }
                }
            }
        }

        System.out.println("Dimension");
        System.out.println("x="+ixx);
        System.out.println("y="+iyy);
        System.out.println("z="+izz);
    }

    public static void dither(Array2D<FloatRgb> a) {
        for (int y=0; y<a.yy; y++) {
            for (int x=0; x<a.xx; x++) {
                FloatRgb rgb = a.get(x, y);
                Color c = Color.nearest(rgb.toInt());
                FloatRgb target = new FloatRgb(c.rgb);
                a.set(x,y,target);

                FloatRgb error = rgb.minus(target);

                propagate(a, x+1,y,   error, 7/16f);
                propagate(a, x-1,y+1, error, 3/16f);
                propagate(a, x  ,y+1, error, 5/16f);
                propagate(a, x+1,y+1, error, 1/16f);
            }
        }
    }

    private static void propagate(Array2D<FloatRgb> a, int x, int y, FloatRgb error, float v) {
        if (x<0 || x>=a.xx || y>=a.yy)        return;

        FloatRgb d = a.get(x, y);
        d = d.plus(error.times(v));
        a.set(x, y, d);
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
