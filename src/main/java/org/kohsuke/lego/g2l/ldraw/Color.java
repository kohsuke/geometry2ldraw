package org.kohsuke.lego.g2l.ldraw;

/**
 * Color code in LDraw data file
 *
 * @author Kohsuke Kawaguchi
 */
// see http://www.ldraw.org/article/547
public enum Color {
    BLACK       ( 0,"05131D"),
    BLUE        ( 1,"0055BF"),
    GREEN       ( 2,"257A3E"),
    RED         ( 4,"C91A09"),
    BROWN       ( 6,"583927"),
    LIGHT_GRAY  ( 7,"9BA19D"),
    DARK_GRAY   ( 8,"6D6E5C"),
    LIGHT_BLUE  ( 9,"B4D2E3"),
    WHITE       (15,"FFFFFF")
    ;

    public final int id;
    public final int c;

    Color(int id, String code) {
        this.id = id;
        this.c = Integer.parseInt(code, 16);
    }
}
