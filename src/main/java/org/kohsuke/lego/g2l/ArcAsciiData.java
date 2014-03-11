package org.kohsuke.lego.g2l;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Two dimensional array that holds the height map.
 *
 * See http://en.wikipedia.org/wiki/Esri_grid
 *
 * @author Kohsuke Kawaguchi
 */
public class ArcAsciiData {
    private final int cols, rows;
    private final int[] data;

    public ArcAsciiData(int cols, int rows, int[] data) {
        this.cols = cols;
        this.rows = rows;
        this.data = data;
    }

    public static ArcAsciiData read(Reader r) throws IOException {
        try {
            BufferedReader b = new BufferedReader(r);

            int cols=0,rows=0;  // dimension of the array when we read it

            int[] data=null;    // actual data
            int ptr=0;

            while (true) {
                String line = b.readLine();
                if (line==null)
                    break;
                String[] tokens = line.trim().split("[ \t]+");
                if (tokens.length==2 && KEYWORDS.contains(tokens[0])) {
                    float n = Float.parseFloat(tokens[1]);
                    switch (tokens[0]) {
                    case "ncols":   cols=(int)n;break;
                    case "nrows":   rows=(int)n;break;
                    // ignore the rest
                    }
                    continue;
                } else {
                    if (data==null) {
                        if (cols==0 || rows==0)
                            throw new IllegalStateException("Invalid cols/rows");
                        data = new int[cols*rows];
                    }

                    for (String t : tokens) {
                        data[ptr++] = Integer.parseInt(t);
                    }
                }
            }

            if (ptr!=cols*rows)
                throw new IllegalStateException("Expected to read "+cols*rows+" data but found "+ptr);

            return new ArcAsciiData(cols,rows,data);
        } finally {
            r.close();
        }
    }

    private static final Set<String> KEYWORDS = new HashSet<String>(Arrays.asList(
            "ncols","nrows","xllcorner","yllcorner","cellsize","NODATA_value"
    ));
}
