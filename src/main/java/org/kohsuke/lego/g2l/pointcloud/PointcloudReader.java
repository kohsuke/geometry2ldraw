package org.kohsuke.lego.g2l.pointcloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Kohsuke Kawaguchi
 */
public class PointcloudReader implements Iterable<Point> {
    private final File source;

    public PointcloudReader(File source) {
        this.source = source;
    }

    @Override
    public Iterator<Point> iterator() {
        try {
            final BufferedReader r = new BufferedReader(new FileReader(source));

            return new Iterator<Point>() {
                Point next;

                @Override
                public boolean hasNext() {
                    fetch();
                    return next!=null;
                }

                private void fetch() {
                    if (next==null) {
                        try {
                            String line = r.readLine();
                            if (line!=null)
                                next = Point.parse(line);
                        } catch (IOException e) {
                            throw new IOError(e);
                        }
                    }
                }

                @Override
                public Point next() {
                    fetch();
                    Point p = next;
                    next = null;
                    if (p==null)    throw new NoSuchElementException();
                    return p;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        } catch (IOException e) {
            throw new IOError(e);
        }
    }
}
