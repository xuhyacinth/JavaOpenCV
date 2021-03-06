package com.xu.opencv.image;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class Test {

    static {
        //在使用OpenCV前必须加载Core.NATIVE_LIBRARY_NAME类,否则会报错
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        videoFace();
    }

    /**
     * OpenCV-4.0.0 实时人脸识别
     *
     * @return: void
     * @date: 2019年5月7日12:16:55
     */
    public static void videoFace() {
        VideoCapture capture = new VideoCapture(0);
        Mat image = new Mat();
        int index = 0;
        if (capture.isOpened()) {
            while (true) {
                capture.read(image);
                HighGui.imshow("实时人脸识别", getFace(image));
                index = HighGui.waitKey(1);
                if (index == 27) {
                    break;
                }
            }
        }
        return;
    }

    /**
     * OpenCV-4.0.0 人脸识别
     *
     * @param image 待处理Mat图片(视频中的某一帧)
     * @return 处理后的图片
     * @date: 2019年5月7日12:16:55
     */
    public static Mat getFace(Mat image) {
        // 1 读取OpenCV自带的人脸识别特征XML文件
        CascadeClassifier facebook = new CascadeClassifier("lib\\OpenCV-455\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
        // 2 特征匹配类
        MatOfRect face = new MatOfRect();
        // 3 特征匹配
        facebook.detectMultiScale(image, face);
        Rect[] rect = face.toArray();
        System.out.println("匹配到 " + rect.length + " 个人脸");
        // 4 为每张识别到的人脸画一个圈
        for (int i = 0, len = rect.length; i < len; i++) {
            Imgproc.rectangle(image, new Point(rect[i].x, rect[i].y), new Point(rect[i].x + rect[i].width, rect[i].y + rect[i].height), new Scalar(0, 255, 0), 1, Imgproc.LINE_AA);
            Imgproc.putText(image, "Human", new Point(rect[i].x, rect[i].y), Imgproc.FONT_HERSHEY_SCRIPT_SIMPLEX, 1.0, new Scalar(0, 255, 0), 1, Imgproc.LINE_AA, false);
        }
        return image;
    }

}
