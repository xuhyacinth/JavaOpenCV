package com.xu.opencv.canva;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @version V-1.0
 * @Title: Canvas.java
 * @Package com.xu.opencv.canvas
 * @Description: TODO
 * @author: hyacinth
 * @date: 2019年1月26日 下午5:08:18
 * @Copyright: 2019 hyacinth
 */
public class Canvas {

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
        fillPoly();
    }

    /**
     * OpenCV-4.0.0 图像文字
     *
     * @return: void
     * @date: 2019年1月26日 下午5:37:04
     */
    public static void putText() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        Imgproc.putText(src, "test text", new Point(60, 60), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(0, 255, 0), 1, Imgproc.LINE_AA, false);
        HighGui.imshow("putText", src);
        HighGui.waitKey(1);
    }

    /**
     * OpenCV-4.0.0 图像画线
     *
     * @return: void
     * @date: 2019年1月17日 下午8:32:41
     */
    public static void line() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        Imgproc.line(src, new Point(10, 50), new Point(100, 50), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
        HighGui.imshow("图像画线", src);
        HighGui.waitKey(1);
    }

    /**
     * OpenCV-4.0.0 图像椭圆
     *
     * @return: void
     * @date: 2019年1月17日 下午8:32:56
     */
    public static void ellipse() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        Imgproc.ellipse(src, new Point(200, 200), new Size(90, 50), 0, 0, 360, new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
        HighGui.imshow("图像椭圆", src);
        HighGui.waitKey(1);
    }

    /**
     * OpenCV-4.0.0 图像矩形
     *
     * @return: void
     * @date: 2019年1月17日 下午8:33:08
     */
    public static void rectangle() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        Imgproc.rectangle(src, new Point(10, 10), new Point(200, 200), new Scalar(0, 255, 0));
        HighGui.imshow("图像矩形", src);
        HighGui.waitKey(1);
    }

    /**
     * OpenCV-4.0.0 图像画圆
     *
     * @return: void
     * @date: 2019年1月17日 下午8:33:27
     */
    public static void circle() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        Imgproc.circle(src, new Point(50, 50), 20, new Scalar(0, 255, 0), 2, Imgproc.LINE_8);
        HighGui.imshow("图像画圆", src);
        HighGui.waitKey(1);
    }

    /**
     * OpenCV-4.0.0 图像填充
     *
     * @return: void
     * @date: 2019年1月26日 下午6:25:55
     */
    public static void fillPoly() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        // 1
        List<Point> points1 = new ArrayList<>();
        points1.add(new Point(0, 100));
        points1.add(new Point(60, 50));
        points1.add(new Point(120, 100));
        points1.add(new Point(180, 50));
        points1.add(new Point(240, 100));
        points1.add(new Point(120, 250));
        Point[] array = points1.stream().toArray(Point[]::new);
        MatOfPoint point = new MatOfPoint(array);
        Imgproc.fillConvexPoly(src, point, new Scalar(0, 255, 0), Imgproc.LINE_AA);
        // 2
        List<Point> points2 = new ArrayList<>();
        points2.add(new Point(120, 100));
        points2.add(new Point(180, 50));
        points2.add(new Point(240, 100));
        Imgproc.fillConvexPoly(src, new MatOfPoint(points2.stream().toArray(Point[]::new)), new Scalar(0, 255, 0), Imgproc.LINE_AA);

        HighGui.imshow("图像填充", src);
        HighGui.waitKey(1);
    }

}
