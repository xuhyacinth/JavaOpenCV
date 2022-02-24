package com.xu.opencv.image;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Title: Watershed.java
 * @Package com.xu.opencv.image
 * @Description: OpenCV 分水岭算法
 * @author: hyacinth
 * @date: 2022年2月21日 下午22:10:14
 * @version: V-1.0.0
 * @Copyright: 2022 hyacinth
 */
public class Watershed {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        watershed();
    }

    /**
     * OpenCV 分水岭算法
     *
     * @return: void
     * @date: 2022年2月22日14点22分
     */
    public static void watershed() {
        // 1 读取图片
        Mat source = Imgcodecs.imread("D:\\OneDrive\\桌面\\1.png", Imgcodecs.IMREAD_COLOR);
        // 2 转为回去图片
        Mat gray = new Mat(source.rows(), source.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(source, gray, Imgproc.COLOR_BGR2GRAY);
        // 3 图片二值化
        Mat binary = Mat.zeros(gray.rows(), gray.cols(), CvType.CV_8UC1);
        Imgproc.threshold(gray, binary, 100, 255, Imgproc.THRESH_BINARY);

        // 4 获取前景
        Mat fg = new Mat(source.size(), CvType.CV_8U);
        Imgproc.erode(binary, fg, new Mat(), new Point(-1, -1), 2);

        // 5 获取背景
        Mat bg = new Mat(source.size(), CvType.CV_8U);
        Imgproc.dilate(binary, bg, new Mat(), new Point(-1, -1), 3);
        Imgproc.threshold(bg, bg, 1, 128, Imgproc.THRESH_BINARY_INV);

        // 6 初始化一个空白图片
        Mat temp1 = new Mat(binary.size(), CvType.CV_8U, new Scalar(0));
        Mat temp2 = new Mat(binary.size(), CvType.CV_8U, new Scalar(0));
        Mat temp3 = new Mat(binary.size(), CvType.CV_8U, new Scalar(0));
        // 7 前景和背景融合
        Mat mask = new Mat(binary.size(), CvType.CV_8U, new Scalar(0));
        Core.add(fg, bg, mask);

        mask.convertTo(temp1, CvType.CV_32S);
        Imgproc.watershed(source, temp1);
        temp1.convertTo(temp2, CvType.CV_8UC1);
        temp1.convertTo(temp3, CvType.CV_8U, 255, 255);
        temp1.convertTo(temp1, CvType.CV_8UC1);

        HighGui.imshow("mask", mask);
        HighGui.imshow("temp1", temp1);
        HighGui.imshow("temp2", temp2);
        HighGui.imshow("temp3", temp3);
        HighGui.waitKey();
        HighGui.destroyAllWindows();
    }

}
