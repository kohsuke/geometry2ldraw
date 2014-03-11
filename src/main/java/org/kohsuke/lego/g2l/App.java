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
        int heightRange = 8*32; // 1 plate height = 8 LDU
        int color = 15; // see http://www.ldraw.org/article/547
        int scale = 20;

        System.out.printf("xx=%d,yy=%d\n", d.xx, d.yy)

        try (PrintWriter w = new PrintWriter(new FileWriter(input +".ldr"))) {
            w.printf("0 %s\n", input);

            for (int y=0; y<d.yy; y+=scale) {
                for (int x=0; x<d.xx; x+=scale) {
                    int h = d.scaleOf(x, y, 0, heightRange);
                    int c = (x==0 && y==0) ? RED : color;
                    w.printf("1 %d  %d %d %d   1 0 0   0 1 0   0 0 1  2453.DAT\n",
                            c, y*20/scale, -h, x*20/scale);
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
