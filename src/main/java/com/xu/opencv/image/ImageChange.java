package com.xu.opencv.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @author Administrator
 */
public class ImageChange {

    static {
        String os = System.getProperty("os.name");
        String type = System.getProperty("sun.arch.data.model");
        if (os.toUpperCase().contains("WINDOWS")) {
            File lib;
            if (type.endsWith("64")) {
                lib = new File("lib\\OpenCV-455\\x64\\" + System.mapLibraryName("opencv_java455"));
            } else {
                lib = new File("lib\\OpenCV-455\\x86\\" + System.mapLibraryName("opencv_java455"));
            }
            System.load(lib.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        move();
    }

    /**
     * OpenCV 仿射变换
     *
     * @return void
     * @Author: hyacinth
     * @Title: warpAffine
     * @Description: TODO
     * @date: 2022年2月22日12点32分
     */
    public static void warpAffine() {
        Mat src = Imgcodecs.imread("C:\\Users\\Administrator\\Desktop\\1.png");
        MatOfPoint2f point1 = new MatOfPoint2f(new Point(0, 0), new Point(0, src.rows()), new Point(src.cols(), 0));
        MatOfPoint2f point2 = new MatOfPoint2f(new Point(src.cols() * 0.1, src.cols() * 0.1), new Point(src.cols() * 0.2, src.cols() * 0.7),
                new Point(src.cols() * 0.7, src.cols() * 0.2));
        // 获取 放射变换 矩阵
        Mat dst = Imgproc.getAffineTransform(point1, point2);
        // 进行 仿射变换
        Mat image = new Mat();
        Imgproc.warpAffine(src, image, dst, src.size());
        HighGui.imshow("原图", src);
        HighGui.imshow("仿射变换", image);
        HighGui.waitKey(0);
    }

    /**
     * OpenCV 透视变换
     *
     * @return void
     * @Author: hyacinth
     * @Title: warpPerspective
     * @Description: TODO
     * @date: 2022年2月22日12点32分
     */
    public static void warpPerspective() {
        Mat src = Imgcodecs.imread("C:\\Users\\Administrator\\Desktop\\1.png");
        MatOfPoint2f point1 = new MatOfPoint2f();
        List<Point> before = new ArrayList<>();
        before.add(new Point(0, 0));
        before.add(new Point(src.cols(), 0));
        before.add(new Point(0, src.rows()));
        before.add(new Point(src.cols(), src.rows()));
        point1.fromList(before);
        MatOfPoint2f point2 = new MatOfPoint2f();
        List<Point> after = new ArrayList<>();
        after.add(new Point(src.cols(), src.rows()));
        after.add(new Point(src.cols() * 0.1, src.rows() * 0.8));
        after.add(new Point(src.cols() * 0.7, src.rows() * 0.3));
        after.add(new Point(0, 0));
        point2.fromList(after);
        // 获取 透视变换 矩阵
        Mat dst = Imgproc.getPerspectiveTransform(point1, point2);
        // 进行 透视变换
        Mat image = new Mat();
        Imgproc.warpPerspective(src, image, dst, src.size());
        HighGui.imshow("原图", src);
        HighGui.imshow("透视变换", image);
        HighGui.waitKey(0);
    }

    /**
     * OpenCV 透视变换
     *
     * @return void
     * @Author: hyacinth
     * @Title: rotate
     * @Description: TODO
     * @date: 2022年2月22日12点32分
     */
    public static void rotate() {
        Mat src = Imgcodecs.imread("C:\\Users\\Administrator\\Desktop\\1.png");
        // 图像中心
        Point center = new Point(src.cols() / 2, src.rows() / 2);
        // 获取 旋转 矩阵
        Mat dst = Imgproc.getRotationMatrix2D(center, 45, 0.5);
        // 进行 图像旋转
        Mat image = new Mat();
        Imgproc.warpAffine(src, image, dst, src.size());
        HighGui.imshow("原图", src);
        HighGui.imshow("图像旋转", image);
        HighGui.waitKey(0);
    }

    /**
     * OpenCV 图像平移
     *
     * @return void
     * @Author: hyacinth
     * @Title: rotate
     * @Description: TODO
     * @date: 2022年2月22日12点32分
     */
    public static void move() {
        Mat src = Imgcodecs.imread("C:\\Users\\Administrator\\Desktop\\1.png");
        // 自定义 旋转 矩阵
        Mat dst = new Mat(2, 3, CvType.CV_32F);
        dst.put(0, 0, 1);
        dst.put(0, 1, 0);
        dst.put(0, 2, src.width() / 4);
        dst.put(1, 0, 0);
        dst.put(1, 1, 1);
        dst.put(1, 2, src.height() / 4);
        // 进行 图像平移
        Mat image = new Mat();
        Imgproc.warpAffine(src, image, dst, src.size());
        HighGui.imshow("原图", src);
        HighGui.imshow("图像平移", image);
        HighGui.waitKey(0);
    }

    /**
     * OpenCV 图像缩放
     *
     * @return void
     * @Author: hyacinth
     * @Title: rotate
     * @Description: TODO
     * @date: 2022年2月22日12点32分
     */
    public static void resize() {
        Mat src = Imgcodecs.imread("C:\\Users\\Administrator\\Desktop\\1.png");
        // 图像中心
        Point center = new Point(src.cols() / 2, src.rows() / 2);
        // 获取 旋转 矩阵
        Mat dst = Imgproc.getRotationMatrix2D(center, 0, 0.5);
        // 进行 图像缩放
        Mat image = new Mat();
        Imgproc.warpAffine(src, image, dst, src.size());
        HighGui.imshow("原图", src);
        HighGui.imshow("图像缩放", image);
        HighGui.waitKey(0);
    }

}
