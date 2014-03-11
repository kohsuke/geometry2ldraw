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
    /**
     * Dimension of the data.
     */
    public final int xx, yy;
    private final int[] data;

    /**
     * Biggest value in the entire data
     */
    public final int max;
    /**
     * Smallest value in the entire data
     */
    public final int min;

    public ArcAsciiData(int x, int y, int[] data) {
        this.xx = x;
        this.yy = y;
        this.data = data;

        int mn=Integer.MAX_VALUE;
        int mx=Integer.MIN_VALUE;

        for (int i : data) {
            mn = Math.min(mn,i);
            mx = Math.max(mx,i);
        }
        max = mx;
        min = mn;
    }

    public int at(int x, int y) {
        return data[x+y*xx];
    }

    /**
     * Scale the data at the given position between [mn,mx)
     */
    public int scaleOf(int x, int y, int mn, int mx) {
        long v = at(x,y);
        long l = (v-min)*(mx-mn)/(max-min+1)+mn;
        assert mn<=l && l<mx;
        return (int)l;
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
