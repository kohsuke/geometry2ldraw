package org.kohsuke.lego.g2l.pointcloud;

import java.io.File;

/**
 * @author Kohsuke Kawaguchi
 */
public class Renderer {
    public static void main(String[] args) {
        for (Point p : new PointcloudReader(new File("pointcloud.asc"))) {

        }
    }
}
