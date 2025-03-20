package com.xu.opencv.image;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;


/**
 * Java OpenCV
 *
 * @author hyacinth
 * @since 2025年3月20日12点28分
 */
public class NewTest {

    static {
        String os = System.getProperty("os.name");
        String type = System.getProperty("sun.arch.data.model");
        if (os.toUpperCase().contains("WINDOWS")) {
            File lib;
            if (type.endsWith("64")) {
                lib = new File("lib\\opencv\\x64\\" + System.mapLibraryName("opencv_java490"));
            } else {
                lib = new File("lib\\opencv\\x86\\" + System.mapLibraryName("opencv_java490"));
            }
            System.load(lib.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        Mat src = Imgcodecs.imread("C:\\Users\\xuyq\\Desktop\\1.png");
        HighGui.imshow("T", src);
        HighGui.waitKey(0);
    }

}
