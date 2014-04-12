package org.kohsuke.lego.g2l.ldraw;

/**
 * Color code in LDraw data file
 *
 * @author Kohsuke Kawaguchi
 */
// see http://www.ldraw.org/article/547
public enum Color {
    RED(4), WHITE(15);

    public final int id;

    Color(int id) {
        this.id = id;
    }
}
