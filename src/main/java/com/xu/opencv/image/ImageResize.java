package com.xu.opencv.image;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @author hyacinth
 */
public class ImageResize {

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
        String path = "C:\\Users\\xuyq\\Desktop\\2.jpg";
        resize3(path);
    }

    public static void resize1(String path) {
        Mat src = Imgcodecs.imread(path);
        Mat dst = new Mat();
        // 目标尺寸
        Size size = new Size(300, 300);
        // 使用双线性插值
        Imgproc.resize(src, dst, size, 0, 0, Imgproc.INTER_AREA);

        HighGui.imshow("src", src);
        HighGui.imshow("dst", dst);
        HighGui.waitKey();
    }

    public static void resize2(String path) {
        Mat src = Imgcodecs.imread(path);
        Mat dst = new Mat();

        Point[] srcPoints = {new Point(0, 0), new Point(src.cols() - 1, 0), new Point(0, src.rows() - 1)};
        Point[] dstPoints = {new Point(0, src.rows() * 0.33), new Point(src.cols() * 0.85, src.rows() * 0.25), new Point(src.cols() * 0.15, src.rows() * 0.7)};

        MatOfPoint2f srcMat = new MatOfPoint2f(srcPoints);
        MatOfPoint2f dstMat = new MatOfPoint2f(dstPoints);

        Mat affineMatrix = Imgproc.getAffineTransform(srcMat, dstMat);
        Imgproc.warpAffine(src, dst, affineMatrix, new Size(src.cols(), src.rows()));

        HighGui.imshow("src", src);
        HighGui.imshow("dst", dst);
        HighGui.waitKey();
    }

    public static void resize3(String path) {
        Mat src = Imgcodecs.imread(path);
        Mat dst = new Mat();

        Point[] srcPoints = {new Point(0, 0), new Point(src.cols() - 1, 0), new Point(src.cols() - 1, src.rows() - 1), new Point(0, src.rows() - 1)};
        Point[] dstPoints = {new Point(src.cols() * 0.05, src.rows() * 0.33), new Point(src.cols() * 0.9, src.rows() * 0.25), new Point(src.cols() * 0.8, src.rows() * 0.9), new Point(src.cols() * 0.2, src.rows() * 0.7)};

        MatOfPoint2f srcMat = new MatOfPoint2f(srcPoints);
        MatOfPoint2f dstMat = new MatOfPoint2f(dstPoints);

        Mat perspectiveMatrix = Imgproc.getPerspectiveTransform(srcMat, dstMat);
        Imgproc.warpPerspective(src, dst, perspectiveMatrix, new Size(src.cols(), src.rows()));

        HighGui.imshow("src", src);
        HighGui.imshow("dst", dst);
        HighGui.waitKey();

    }

}
