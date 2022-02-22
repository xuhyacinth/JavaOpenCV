package com.xu.opencv.image;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Title: Operator.java
 * @Package com.xu.opencv.image
 * @Description: OpenCV-4.1.0 自定义滤波(降噪)
 * @author: hyacinth
 * @date: 2022年2月21日 下午22:10:14
 * @version: V-1.0.0
 * @Copyright: 2022 hyacinth
 */
public class Operator {

    static {
        String os = System.getProperty("os.name");
        String type = System.getProperty("sun.arch.data.model");
        if (os.toUpperCase().contains("WINDOWS")) {
            File lib;
            if (type.endsWith("64")) {
                lib = new File("lib\\x64\\" + System.mapLibraryName("opencv_java455"));
            } else {
                lib = new File("lib\\x86\\" + System.mapLibraryName("opencv_java455"));
            }
            System.load(lib.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        kernel3();
    }

    /**
     * OpenCV 自定义滤波(降噪)(Robert算子)
     *
     * @return: void
     * @date: 2019年5月7日12:16:55
     */
    public static void kernel1() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\1.jpg");
        HighGui.imshow("Robert算子 原图", src.clone());
        Mat dst_x = new Mat();
        Mat dst_y = new Mat();

        //Robert算子-X轴
        Mat kernel_x = new Mat(2, 2, 1);
        kernel_x.put(0, 0, 1);
        kernel_x.put(0, 1, 0);
        kernel_x.put(1, 0, 0);
        kernel_x.put(1, 1, -1);
        Imgproc.filter2D(src, dst_x, -1, kernel_x, new Point(-1, -1), 0.0);

        //Robert算子-Y轴
        Mat kernel_y = new Mat(2, 2, 1);
        kernel_y.put(0, 0, 0);
        kernel_y.put(0, 1, 1);
        kernel_y.put(1, 0, -1);
        kernel_y.put(1, 1, 0);
        Imgproc.filter2D(src, dst_y, -1, kernel_y, new Point(-1, -1), 0.0);

        HighGui.imshow("Robert算子 Y", dst_y);
        HighGui.imshow("Robert算子 X", dst_x);
        Mat dst = new Mat();
        Core.addWeighted(dst_x, 0.5, dst_y, 0.5, 0, dst);
        HighGui.imshow("Robert算子 融合", dst);
        HighGui.waitKey(10);
    }

    /**
     * OpenCV 自定义滤波(降噪)(Sable算子)
     *
     * @return: void
     * @date: 2019年5月7日12:16:55
     */
    public static void kernel2() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\1.jpg");
        HighGui.imshow("Sable算子 原图", src.clone());
        Mat dst_x = new Mat();
        Mat dst_y = new Mat();

        //Soble算子-X轴
        Mat kernel_x = new Mat(3, 3, 1);
        kernel_x.put(0, 0, -1);
        kernel_x.put(0, 1, 0);
        kernel_x.put(0, 2, 1);
        kernel_x.put(1, 0, -2);
        kernel_x.put(1, 1, 0);
        kernel_x.put(1, 2, 2);
        kernel_x.put(2, 0, -1);
        kernel_x.put(2, 1, 0);
        kernel_x.put(2, 2, 1);
        Imgproc.filter2D(src, dst_x, -1, kernel_x, new Point(-1, -1), 0.0);

        //Soble算子-Y轴
        Mat kernel_y = new Mat(3, 3, 1);
        kernel_y.put(0, 0, -1);
        kernel_y.put(0, 1, 2);
        kernel_y.put(0, 2, -1);
        kernel_y.put(1, 0, 0);
        kernel_y.put(1, 1, 0);
        kernel_y.put(1, 2, 0);
        kernel_y.put(2, 0, 1);
        kernel_y.put(2, 1, 2);
        kernel_y.put(2, 2, 1);
        Imgproc.filter2D(src, dst_y, -1, kernel_y, new Point(-1, -1), 0.0);

        HighGui.imshow("Sable算子 X", dst_x);
        HighGui.imshow("Sable算子 Y", dst_y);
        Mat dst = new Mat();
        Core.addWeighted(dst_x, 0.5, dst_y, 0.5, 0, dst);
        HighGui.imshow("Sable算子 融合", dst);
        HighGui.waitKey(1);
    }

    /**
     * OpenCV 自定义滤波(降噪)(Laplace算子)
     *
     * @return: void
     * @date: 2019年5月7日12:16:55
     */
    public static void kernel3() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\1.jpg");
        HighGui.imshow("Laplace 算子 原图", src.clone());
        Mat dst = new Mat();

        //拉普拉斯算子
        Mat kernel = new Mat(3, 3, 1);
        kernel.put(0, 0, 0);
        kernel.put(0, 1, -1);
        kernel.put(0, 2, 0);
        kernel.put(1, 0, -1);
        kernel.put(1, 1, 4);
        kernel.put(1, 2, -1);
        kernel.put(2, 0, 0);
        kernel.put(2, 1, -1);
        kernel.put(2, 2, 0);
        Imgproc.filter2D(src, dst, -1, kernel, new Point(-1, -1), 0.0);

        HighGui.imshow("Laplace 算子", dst);
        HighGui.waitKey(0);
    }

}