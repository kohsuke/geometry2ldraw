package org.kohsuke.lego.g2l.ldraw;

/**
 * @author Kohsuke Kawaguchi
 */
public enum Part {
    BRICK1x1("3005"),
    PLATE1x1("3024"),

    /**
     * 1x1 brick stacked 4 or 5 times high
     */
    COLUMN1x1("2453")
    ;

    public final String id;

    Part(String id) {
        this.id = id;
    }
}
