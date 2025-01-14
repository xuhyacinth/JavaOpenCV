package com.xu.opencv.image;

import java.io.File;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point2f;

/**
 * @author Administrator
 */
public class NewImage {

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
        // 读取图像
        Mat src = opencv_imgcodecs.imread("C:\\Users\\xuyq\\Desktop\\1.png");
        if (src == null || src.empty()) {
            return;
        }

        System.out.println(src.rows());
        System.out.println(src.cols());

        Mat m1 = new Mat(4, 1, opencv_core.CV_32FC2);
        m1.ptr(0).put(new Point2f(0, 0));
        m1.ptr(1).put(new Point2f(src.cols(), 0));
        m1.ptr(2).put(new Point2f(0, src.rows()));
        m1.ptr(3).put(new Point2f(src.cols(), src.rows()));

        Mat m2 = new Mat(4, 1, opencv_core.CV_32FC2);
        m1.ptr(3).put(new Point2f(20f, 20f));
        m1.ptr(1).put(new Point2f(src.cols() * 0.9f, src.rows() * 0.1f));
        m1.ptr(2).put(new Point2f(src.cols() * 0.1f, src.rows() * 0.9f));
        m1.ptr(0).put(new Point2f(src.cols() * 0.8f, src.rows() * 0.8f));

        Mat matrix = opencv_imgproc.getPerspectiveTransform(m1, m2);

        Mat output = new Mat();
        opencv_imgproc.warpPerspective(src, output, matrix, src.size());

        opencv_highgui.imshow("SRC", src);
        opencv_highgui.imshow("DST", output);
        opencv_highgui.waitKey(0);
    }

}
