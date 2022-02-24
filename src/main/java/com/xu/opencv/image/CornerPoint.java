package com.xu.opencv.image;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;
import org.opencv.features2d.SIFT;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Title: CornerPoint.java
 * @Package com.xu.opencv.image
 * @Description: OpenCV 角点检测
 * @author: hyacinth
 * @date: 2022年2月21日 下午22:10:14
 * @version: V-1.0.0
 * @Copyright: 2022 hyacinth
 */
public class CornerPoint {

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
        harris();
    }

    /**
     * OpenCV FAST 角点检测
     *
     * @return void
     * @Author: hyacinth
     * @Title: fast
     * @Description: TODO
     * @date: 2022年2月22日12点32分
     */
    public static void fast() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        HighGui.imshow("原图", src.clone());
        FastFeatureDetector fd = FastFeatureDetector.create(FastFeatureDetector.THRESHOLD);
        MatOfKeyPoint regions = new MatOfKeyPoint();
        fd.detect(src, regions);
        Features2d.drawKeypoints(src, regions, src, new Scalar(0, 0, 255), Features2d.DrawMatchesFlags_DRAW_RICH_KEYPOINTS);
        HighGui.imshow("FAST 角点检测", src);
        HighGui.waitKey(0);
    }

    /**
     * OpenCV ORB 角点检测
     *
     * @return void
     * @Author: hyacinth
     * @Title: orb
     * @Description: TODO
     * @date: 2022年2月22日12点32分
     */
    public static void orb() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        HighGui.imshow("原图", src.clone());
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        ORB orb = ORB.create(800, 1.2f, 8, 31, 0, 2, ORB.HARRIS_SCORE, 3, 3);
        MatOfKeyPoint point = new MatOfKeyPoint();
        orb.detect(gray, point);
        Features2d.drawKeypoints(src, point, src, new Scalar(0, 0, 255), Features2d.DrawMatchesFlags_DRAW_RICH_KEYPOINTS);
        HighGui.imshow("ORB 角点检测", src);
        HighGui.waitKey(0);
    }

    /**
     * OpenCV SIFT 角点检测
     *
     * @return void
     * @Author: hyacinth
     * @Title: sift
     * @Description: TODO
     * @date: 2022年2月22日12点32分
     */
    public static void sift() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        HighGui.imshow("原图", src.clone());
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        SIFT sift = SIFT.create(0, 3, 0.04, 10, 1.6);
        MatOfKeyPoint point = new MatOfKeyPoint();
        sift.detect(gray, point);
        Features2d.drawKeypoints(src, point, src, new Scalar(0, 0, 255), Features2d.DrawMatchesFlags_DRAW_RICH_KEYPOINTS);
        HighGui.imshow("SIFT 角点检测", src);
        HighGui.waitKey(0);
    }

    /**
     * OpenCV Harris 角点检测
     *
     * @return void
     * @Author: hyacinth
     * @Title: harris
     * @Description: TODO
     * @date: 2019年8月26日 下午10:13:10
     */
    public static void harris() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        HighGui.imshow("原图", src.clone());
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Mat dst = new Mat();
        Imgproc.cornerHarris(gray, dst, 3, 15, 0.04);
        for (int i = 0, row = dst.rows(); i < row; i++) {
            for (int j = 0, col = dst.cols(); j < col; j++) {
                if (dst.get(i, j)[0] > 130) {
                    Imgproc.circle(src, new Point(i - 3, j + 2), 1, new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
                }
            }
        }
        HighGui.imshow("Harris 角点检测", src);
        HighGui.waitKey(0);
    }


    /**
     * OpenCV Shi-Tomasi 角点检测
     *
     * @return void
     * @Author: hyacinth
     * @Title: shi_tomasi
     * @Description: TODO
     * @date: 2019年8月26日 下午10:11:02
     */
    public static void shi_tomasi() {
        Mat src = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        HighGui.imshow("原图", src.clone());
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        MatOfPoint corners = new MatOfPoint();
        Imgproc.goodFeaturesToTrack(gray, corners, 200, 0.01, 10, new Mat(), 3, 5, false, 0.04);
        int[] cornersData = new int[(int) (corners.total() * corners.channels())];
        corners.get(0, 0, cornersData);
        for (int i = 0; i < corners.rows(); i++) {
            Imgproc.circle(src, new Point(cornersData[i * 2], cornersData[i * 2 + 1]), 3, new Scalar(0, 0, 255), Imgproc.FILLED);
        }
        HighGui.imshow("Shi-Tomasi 角点检测", src);
        HighGui.waitKey(0);
    }

}
