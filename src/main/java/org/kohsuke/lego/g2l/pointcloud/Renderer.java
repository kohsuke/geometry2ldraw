package org.kohsuke.lego.g2l.pointcloud;

import org.apache.commons.io.IOUtils;
import org.kohsuke.lego.g2l.Array2D;
import org.kohsuke.lego.g2l.ldraw.Color;
import org.kohsuke.lego.g2l.ldraw.FloatRgb;
import org.kohsuke.lego.g2l.ldraw.LDrawWriter;
import org.kohsuke.lego.g2l.ldraw.Part;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

        /**
         * Adds another color data point.
         */
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
        // scale factor. smaller the number, bigger the model.
        float scale = 2f;

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

        Coordinate peak = null;
        int peakHeight = -1;

        Array2D<Tag> height = new Array2D<Tag>(Tag.class, 150,150);
        try (LDrawWriter w = new LDrawWriter(new File("pointcloud.ldr"))) {
            for (Point p : r) {
                int x = (int)((p.x - xx.min) / scale / 20);
                int y = (int)((p.y - yy.min) / scale / 20);
                int z = (int)((p.z - zz.min) / scale / 8);

                ixx.add(x);
                iyy.add(y);
                izz.add(z);

                Tag t = height.get(x,y);
                if (t==null) {
                    t = new Tag(z, p.rgb());
                    height.set(x, y, t);
                } else
                    t.merge(z, p.rgb());

                if (z > peakHeight) {
                    peakHeight = z;
                    peak = new Coordinate(x,y);
                }
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


            // range around the peak to be modeled
            Range modelRX = new Range(peak.x-48, peak.x+48);
            Range modelRY = new Range(peak.y-48, peak.y+48);

            // figure out the min(z) in the model range
            int minz = Integer.MAX_VALUE;
            for (int x : modelRX) {
                for (int y : modelRY) {
                    Tag t = height.get(x, y);
                    if (t!=null) {
                        minz= Math.min(minz,t.z);
                    }
                }
            }
            System.out.println("minz="+minz);

            // have the smallest height be height=1
            for (int x : modelRX) {
                for (int y : modelRY) {
                    Tag t = height.get(x, y);
                    if (t!=null) {
                        t.z -= minz-1;
                    }
                }
            }

            // build the model
            for (int x : modelRX) {
                for (int y : modelRY) {
// used this with scale=3 to capture a bigger area (but with less details around the peak)
//            for (int x=0; x<height.xx; x++) {
//                for (int y=0; y<height.yy; y++) {
                    Tag t = height.get(x, y);
                    if (t!=null) {
                        w.write(x*20, -y*20, t.z*8, Part.COLUMN1x1, Color.WHITE); // Color.nearest(colors.get(x,y).toInt()));
                        minz= Math.min(minz,t.z);
                    }
                }
            }
        }

        blueprint48x48(height, peak.add(-48, -48), "area1");
        blueprint48x48(height, peak.add(-48,   0), "area2");
        blueprint48x48(height, peak.add(  0, -48), "area3");
        blueprint48x48(height, peak.add(  0,   0), "area4");



        System.out.println("Dimension");
        System.out.println("x="+ixx);
        System.out.println("y="+iyy);
        System.out.println("z="+izz);
        System.out.println("peak="+peak);
    }

    /**
     * Produces 48x48 blueprint of a particular section.
     *
     * @param sp
     *      top-left corner in the height map to start producing blue print.
     */
    private static void blueprint48x48(Array2D<Tag> height, Coordinate sp, String name) throws IOException {
        try (PrintWriter w = new PrintWriter(new File(name+".html"))) {
            w.println(IOUtils.toString(Renderer.class.getResourceAsStream("blueprint-header.html")));
            w.println("<table>");
            for (int x : new Range(sp.x, sp.x+48)) {
                w.println("<tr>");
                for (int y : new Range(sp.y, sp.y+48)) {
                    Tag t = height.get(x,y);
                    if (t!=null)
                        w.printf("<td v='%d'></td>", t.z);
                    else
                        w.printf("<td v='0'></td>");
                }
                w.println("</tr>");
            }
            w.println("</table></body></html>");
        }
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

        v -= 1/32f;

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
