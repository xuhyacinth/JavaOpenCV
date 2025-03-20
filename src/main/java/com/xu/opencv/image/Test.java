package com.xu.opencv.image;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

/**
 * JavaCPP OpenCV
 *
 * @author hyacinth
 * @since 2025年3月20日12点28分
 */
public class Test {

    static {
        Loader.load(opencv_core.class);
        System.out.println(opencv_core.CV_VERSION());
    }

    public static void main(String[] args) {
        Mat src = opencv_imgcodecs.imread("C:\\Users\\xuyq\\Desktop\\1.png");
        opencv_highgui.imshow("T", src);
        opencv_highgui.waitKey(0);
    }

}
