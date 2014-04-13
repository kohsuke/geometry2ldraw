package org.kohsuke.lego.g2l.ldraw;

/**
 * @author Kohsuke Kawaguchi
 */
public enum Part {
    BRICK1x1("3005"),
    PLATE1x1("3024")
    ;

    public final String id;

    Part(String id) {
        this.id = id;
    }
}
