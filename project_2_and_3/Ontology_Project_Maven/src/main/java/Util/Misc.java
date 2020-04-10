package Util;

import org.apache.jena.base.Sys;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Misc {
    private static final String deliverableDirName = "deliverables";

    public static String getFileInDeliverables(String fileName) {
        Path root = Paths.get(System.getProperty("user.dir"));
        return root.resolve(deliverableDirName).resolve(fileName).toString();
    }
}
