package com.xu.opencv.image;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @author hyacint
 */
public class Cropping {

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
        // 图片路径
        String path = "C:\\Users\\xuyq\\Desktop\\1.png";
        // 读取图片
        Mat src = Imgcodecs.imread(path);
        // 定义裁剪区域
        Rect rect = new Rect(100, 100, 300, 200);
        // 执行裁剪
        Mat dst = new Mat(src, rect);
        // 输出裁剪后的图片
        HighGui.imshow("src", resize1(src));
        HighGui.imshow("dst", dst);
        HighGui.waitKey();
    }

    /**
     * 图像缩放
     *
     * @param src 原始图片
     * @return 新图片
     */
    public static Mat resize(Mat src) {
        // 图像中心
        Point center = new Point((double) src.cols() / 4, (double) src.rows() / 4);
        // 获取 旋转 矩阵
        Mat dst = Imgproc.getRotationMatrix2D(center, 0, 0.5);
        // 进行 图像缩放
        Mat image = new Mat();
        Imgproc.warpAffine(src, image, dst, src.size());
        return image;
    }

    /**
     * 图像缩放
     *
     * @param src 原始图片
     * @return 新图片
     */
    public static Mat resize1(Mat src) {
        Size size = new Size((double) src.width() / 8, (double) src.height() / 8);
        // 执行缩小操作
        Mat dst = new Mat();
        Imgproc.resize(src, dst, size, 0, 0, Imgproc.INTER_AREA);
        return dst;
    }

}
