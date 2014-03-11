package org.kohsuke.lego.g2l;

import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        ArcAsciiData.read(new FileReader(args[0]));
    }
}
