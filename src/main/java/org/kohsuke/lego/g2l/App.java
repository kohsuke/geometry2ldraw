package org.kohsuke.lego.g2l;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;

import static java.awt.image.BufferedImage.*;

public class App {
    public static void main(String[] args) throws Exception {
        ArcAsciiData data = ArcAsciiData.read(new FileReader(args[0]));

        BufferedImage img = new BufferedImage(data.xx, data.yy, TYPE_INT_RGB);
        for (int y=0; y<data.yy; y++) {
            for (int x=0; x<data.xx; x++) {
                int v = data.scaleOf(x, y, 0, 256);
                img.setRGB(x,y, v);
            }
        }

        ImageIO.write(img, "PNG", new File(args[0] + ".png"));
    }
}
