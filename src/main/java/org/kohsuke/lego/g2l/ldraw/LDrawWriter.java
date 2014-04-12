package org.kohsuke.lego.g2l.ldraw;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Kohsuke Kawaguchi
 */
public class LDrawWriter implements Closeable {
    PrintWriter w;

    public LDrawWriter(File f) throws IOException {
        this.w = new PrintWriter(new FileWriter(f));
    }

    public void write(int x, int y, int z, Part part, Color c) {
        w.printf("1 %d  %d %d %d   1 0 0   0 1 0   0 0 1  %s.DAT\n",
                c.id, x, y, z, part.id);
    }

    @Override
    public void close() throws IOException {
        w.close();
    }
}
