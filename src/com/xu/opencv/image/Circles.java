package com.xu.opencv.image;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Title: Circles.java
 * @Package com.xu.opencv.image
 * @Description: OpenCV 原型檢查
 * @author: hyacinth
 * @date: 2022年2月22日 下午22:10:14
 * @version: V-1.0.0
 * @Copyright: 2022 hyacinth
 */
public class Circles {

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
        houghCircles();
    }

    /**
     * OpenCV-4.0.0 霍夫变换-圆形检测
     *
     * @return: void
     * @date: 2019年7月10日 上午12:26:07
     */
    public static void houghCircles() {
        Mat src = Imgcodecs.imread("C:\\Users\\Administrator\\Downloads\\1.jpeg");
        Mat gary = new Mat();
        Mat dst = new Mat();
        // 1 中值模糊(滤波-->平滑)
        Imgproc.medianBlur(src, dst, 1);
        // 2 灰度图片
        Imgproc.cvtColor(dst, gary, Imgproc.COLOR_RGB2GRAY);
        // 3 霍夫变换-圆形检测
        Mat lines = new Mat();
        Imgproc.HoughCircles(gary, lines, Imgproc.HOUGH_GRADIENT, 1, 20, 50, 30, 20, 40);
        double[] date;
        for (int i = 0, len = lines.cols(); i < len; i++) {
            date = lines.get(0, i).clone();
            // 园周
            Imgproc.circle(src, new Point((int) date[0], (int) date[1]), (int) date[2], new Scalar(0, 0, 255), 2, Imgproc.LINE_AA);
            // 圆心
            Imgproc.circle(src, new Point((int) date[0], (int) date[1]), 2, new Scalar(0, 255, 0), 2, Imgproc.LINE_AA);
        }
        //Imgproc.resize(src, src, new Size(src.cols() / 2, src.rows() / 2));
        HighGui.imshow("霍夫变换-圆形检测", src);
        HighGui.waitKey(0);
    }

}
