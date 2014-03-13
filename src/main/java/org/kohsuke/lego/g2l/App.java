package org.kohsuke.lego.g2l;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static java.awt.image.BufferedImage.*;

public class App {

    // color code in LDraw data file
    // see  // see http://www.ldraw.org/article/547
    private static final int RED = 4;
    private static final int WHITE = 15;

    public static void main(String[] args) throws Exception {
        App app = new App();
        CmdLineParser p = new CmdLineParser(app);
        try {
            p.parseArgument(args);
            app.run();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            p.printUsage(System.err);
        }
    }

    public void run() throws IOException {
        for (File arg : args) {
            ArcAsciiData data = ArcAsciiData.read(new FileReader(arg));

            BufferedImage img = makeImage(data);
            ImageIO.write(img, "PNG", new File(arg.getPath() + ".png"));

            writeLDraw(arg, data);
        }
    }

    @Argument(required=true,metaVar="ASC")
    List<File> args = new ArrayList<>();

    @Option(name="-sx")
    float sx = 0f;

    @Option(name="-ex")
    float ex = 0f;

    @Option(name="-sy")
    float sy = 1f;

    @Option(name="-ey")
    float ey = 1f;

    @Option(name="height")
    int heightRange = 128;

    @Option(name="scale")
    int scale = 10;


    /**
     * Write data in the LDraw format
     */
    private void writeLDraw(File input, ArcAsciiData d) throws IOException {
        int color = WHITE;

        // parameter for yosemite
        /*
        int heightRange = 32;
        int scale = 10;
        float sx=0.5f, ex=1f;
        float sy=0.33f, ey=0.66f;
        */

        // parameter for small everest
        /*
        int heightRange = 128;
        int scale = 4;
        float sx=0.10f, ex=0.6f;
        float sy=0.35f, ey=0.70f;
        */

        // parameter for matterhorn
        /*
        int heightRange = 64;
        int scale = 6;
        float sx=0.3f, ex=0.85f;
        float sy=0.3f, ey=0.7f;
        */

        System.out.printf("xx=%d,yy=%d\n", d.xx, d.yy);
        System.out.printf("min=%d,max=%d\n",d.min, d.max);

        try (PrintWriter w = new PrintWriter(new FileWriter(input.getPath() +".ldr"))) {
            w.printf("0 %s\n", input);

            for (int y=d.yy(sy); y<d.yy(ey); y+=scale) {
                for (int x=d.xx(sx); x<d.xx(ex); x+=scale) {
                    int h = d.scaleOf(d.averageAt(x,y), 0, heightRange);
                    int c = (x==0 && y==0) ? RED : color;

                    for (int z=0; z<3; z++) {
                        w.printf("1 %d  %d %d %d   1 0 0   0 1 0   0 0 1  3005.DAT\n",
                                c, y*20/scale, -h*8+z*24, x*20/scale);
                    }
                }
            }
        }
    }

    /**
     * Build height map image.
     */
    private BufferedImage makeImage(ArcAsciiData data) {
        BufferedImage img = new BufferedImage(data.xx, data.yy, TYPE_INT_RGB);
        for (int y=0; y<data.yy; y++) {
            for (int x=0; x<data.xx; x++) {
                int v = data.scaleOf(data.at(x,y), 0, 256);
                img.setRGB(x,y, v);
            }
        }
        return img;
    }
}
