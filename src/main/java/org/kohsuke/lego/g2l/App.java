package org.kohsuke.lego.g2l;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static java.awt.image.BufferedImage.*;

public class App {

    private static final int RED = 4;
    private static final int WHITE = 15;

    public static void main(String[] args) throws Exception {
        ArcAsciiData data = ArcAsciiData.read(new FileReader(args[0]));

        BufferedImage img = makeImage(data);
        ImageIO.write(img, "PNG", new File(args[0] + ".png"));

        writeLDraw(args[0], data);
    }

    /**
     * Write data in the LDraw format
     */
    private static void writeLDraw(String input, ArcAsciiData d) throws IOException {
        int color = WHITE; // see http://www.ldraw.org/article/547

        // parameter for yosemite
        /*
        int heightRange = 32;
        int scale = 10;
        float sx=0.5f, ex=1f;
        float sy=0.33f, ey=0.66f;
        */

        // parameter for small everest
        int heightRange = 128;
        int scale = 4;
        float sx=0.10f, ex=0.6f;
        float sy=0.35f, ey=0.70f;

        System.out.printf("xx=%d,yy=%d\n", d.xx, d.yy);

        try (PrintWriter w = new PrintWriter(new FileWriter(input +".ldr"))) {
            w.printf("0 %s\n", input);

            for (int y=d.yy(sy); y<d.yy(ey); y+=scale) {
                for (int x=d.xx(sx); x<d.xx(ex); x+=scale) {
                    int h = d.scaleOf(x, y, 0, heightRange);
                    int c = (x==0 && y==0) ? RED : color;
                    w.printf("1 %d  %d %d %d   1 0 0   0 1 0   0 0 1  2453.DAT\n",
                            c, y*20/scale, -h*8, x*20/scale);
                }
            }
        }
    }

    /**
     * Build height map image.
     */
    private static BufferedImage makeImage(ArcAsciiData data) {
        BufferedImage img = new BufferedImage(data.xx, data.yy, TYPE_INT_RGB);
        for (int y=0; y<data.yy; y++) {
            for (int x=0; x<data.xx; x++) {
                int v = data.scaleOf(x, y, 0, 256);
                img.setRGB(x,y, v);
            }
        }
        return img;
    }
}
