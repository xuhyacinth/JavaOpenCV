package com.xu.opencv.image;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * @Title: FaceDetect.java
 * @Package com.xu.opencv.image
 * @Description: OpenCV 人脸识别
 * @author: hyacinth
 * @date: 2022年2月23日 下午22:10:14
 * @version: V-1.0.0
 * @Copyright: 2022 hyacinth
 */
public class FaceDetect {

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
        face();
    }


    /**
     * OpenCV-4.0.0 人脸识别
     *
     * @return: void
     * @date: 2019年5月7日12:16:55
     */
    public static void face() {
        // 1 读取OpenCV自带的人脸识别特征XML文件 OpenCV 图像识别库一般位于 opencv\sources\data 下面
        CascadeClassifier facebook = new CascadeClassifier("D:\\Learn\\OpenCV\\OpenCV-4.5.5\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
        // 2 读取测试图片
        Mat image = Imgcodecs.imread("D:\\OneDrive\\桌面\\5.jpeg");
        HighGui.imshow("人脸识别 原图", image.clone());
        // 3 特征匹配
        MatOfRect face = new MatOfRect();
        facebook.detectMultiScale(image, face);
        // 4 匹配 Rect 矩阵 数组
        Rect[] rect = face.toArray();
        System.out.println("匹配到 " + rect.length + " 个人脸");
        // 5 为每张识别到的人脸画一个圈
        for (int i = 0; i < rect.length; i++) {
            Imgproc.rectangle(image, new Point(rect[i].x, rect[i].y), new Point(rect[i].x + rect[i].width, rect[i].y + rect[i].height), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
            Imgproc.putText(image, "Human", new Point(rect[i].x, rect[i].y), Imgproc.FONT_HERSHEY_SCRIPT_SIMPLEX, 1.0, new Scalar(0, 255, 0), 1, Imgproc.LINE_AA, false);
        }
        // 6 展示图片
        HighGui.imshow("人脸识别", image);
        HighGui.waitKey(0);
    }

}
