package com.xu.opencv.tracking;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

/**
 * @Title: Tracking.java
 * @Package com.xu.opencv.image
 * @Description: OpenCV 目标跟踪
 * @author: hyacinth
 * @date: 2022年2月21日 下午22:10:14
 * @version: V-1.0.0
 * @Copyright: 2022 hyacinth
 */
public class Tracking {

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
        objectsTrackingBaseOnColor();
    }

    /**
     * OpenCV-4.0.0 基于物体颜色的对象跟踪 多对象跟踪
     *
     * @return: void
     * @date 2019年7月19日 下午22:10:14
     */
    public static void objectsTrackingBaseOnColor() {
        // 1 创建 VideoCapture 对象
        VideoCapture capture = new VideoCapture();
        // 2 使用 VideoCapture 对象读取本地视频
        capture.open("C:\\Users\\Administrator\\Videos\\111.mp4");
        // 3 获取视频处理时的键盘输入 我这里是为了在 视频处理时如果按 Esc 退出视频对象跟踪
        int index = 0;
        // 4 使用 Mat video 保存视频中的图像帧 针对每一帧 做处理
        Mat video = new Mat();
        // 5 根据自己的需求如果不需要再使用原来视频中的图像帧可以只需要上面一个 Mat video 我这里未来展示 处理后图像跟踪的效果
        Mat dealvideo = new Mat();
        // 6 获取视频的形态学结构 用于图像 开操作 降噪使用
        Mat kernel1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3), new Point(-1, -1));
        // 7 获取视频的形态学结构 用于图像 膨胀 扩大跟踪物体图像轮廓
        Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5), new Point(-1, -1));
        while (capture.read(video)) {
            // 8 颜色过滤  (根据跟踪物体在图像中的特定颜色找出图像 Core.inRange 会将这个颜色处理为白色 其他为黑色) 我这里是找出视频中的绿色移动物体
            // 8 颜色的设置会非常影响对象跟踪的效果
            Core.inRange(video, new Scalar(0, 127, 0), new Scalar(120, 255, 120), dealvideo);
            // 9 开操作(移除其他小的噪点)
            Imgproc.morphologyEx(dealvideo, dealvideo, Imgproc.MORPH_OPEN, kernel1, new Point(-1, -1), 1);
            // 10 膨胀 (突出特定颜色物体轮廓)
            Imgproc.dilate(dealvideo, dealvideo, kernel2, new Point(-1, -1), 4);
            // 11 找出对应物体在图像中的坐标位置(X,Y)及宽、高(width,height)轮廓发现与位置标定
            List<Rect> rects = process(dealvideo);
            // 12.1 在物体轮廓外画矩形
            //Imgproc.rectangle(video,new Point(rects.x,rects.y), new Point(rects.x+rects.width,rects.y+rects.height),new Scalar(0, 0, 255), 3, 8, 0);// 在物体轮廓外画矩形
            // 12.2 在物体轮廓外画矩形
            System.out.println("发现 " + rects.size() + " 对象");
            for (int i = 0; i < rects.size(); i++) {
                Imgproc.rectangle(video, new Point(rects.get(i).x, rects.get(i).y), new Point(rects.get(i).x + rects.get(i).width, rects.get(i).y + rects.get(i).height), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA, 0);
            }
            //Imgproc.rectangle(dealvideo,rects,new Scalar(0, 0, 255), 3, 8, 0);
            //13 展示最终的效果
            HighGui.imshow("基于物体颜色的对象跟踪 多对象跟踪", video);
            index = HighGui.waitKey(100);
            if (index == 27) {
                capture.release();
                return;
            }
        }
    }

    /**
     * OpenCV-4.0.0 基于物体颜色的对象跟踪 单对象跟踪
     *
     * @return: void
     * @date 2019年7月19日 下午22:10:14
     */
    public static void objectTrackingBaseOnColor() {
        // 1 创建 VideoCapture 对象
        VideoCapture capture = new VideoCapture();
        // 2 使用 VideoCapture 对象读取本地视频
        capture.open("C:\\Users\\Administrator\\Videos\\111.mp4");
        // 3 获取视频处理时的键盘输入 我这里是为了在 视频处理时如果按 Esc 退出视频对象跟踪
        int index = 0;
        // 4 使用 Mat video 保存视频中的图像帧 针对每一帧 做处理
        Mat video = new Mat();
        // 5 根据自己的需求如果不需要再使用原来视频中的图像帧可以只需要上面一个 Mat video 我这里未来展示 处理后图像跟踪的效果
        Mat dealvideo = new Mat();
        // 6 获取视频的形态学结构 用于图像 开操作 降噪使用
        Mat kernel1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3), new Point(-1, -1));
        // 7 获取视频的形态学结构 用于图像 膨胀 扩大跟踪物体图像轮廓
        Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5), new Point(-1, -1));
        while (capture.read(video)) {
            // 8 颜色过滤  (根据跟踪物体在图像中的特定颜色找出图像 Core.inRange 会将这个颜色处理为白色 其他为黑色) 我这里是找出视频中的绿色移动物体
            // 8 颜色的设置会非常影响对象跟踪的效果
            Core.inRange(video, new Scalar(0, 127, 0), new Scalar(120, 255, 120), dealvideo);
            // 9 开操作(移除其他小的噪点)
            Imgproc.morphologyEx(dealvideo, dealvideo, Imgproc.MORPH_OPEN, kernel1, new Point(-1, -1), 1);
            // 10 膨胀 (突出特定颜色物体轮廓)
            Imgproc.dilate(dealvideo, dealvideo, kernel2, new Point(-1, -1), 4);
            // 11 找出对应物体在图像中的坐标位置(X,Y)及宽、高(width,height)轮廓发现与位置标定
            Rect rects = new Rect();
            rects = process(dealvideo, rects);
            // 12.1 在物体轮廓外画矩形
            //Imgproc.rectangle(video,new Point(rects.x,rects.y), new Point(rects.x+rects.width,rects.y+rects.height),new Scalar(0, 0, 255), 3, 8, 0);// 在物体轮廓外画矩形
            // 12.2 在物体轮廓外画矩形
            Imgproc.rectangle(video, rects, new Scalar(0, 0, 255), 3, 8, 0);
            //Imgproc.rectangle(dealvideo,rects,new Scalar(0, 0, 255), 3, 8, 0);
            //13 展示最终的效果
            HighGui.imshow("基于物体颜色的对象跟踪 单对象跟踪", video);
            index = HighGui.waitKey(100);
            if (index == 27) {
                capture.release();
                return;
            }
        }
    }

    /**
     * OpenCV-4.0.0  基于物体颜色的对象跟踪 单对象跟踪
     * <table border="1" cellpadding="10">
     * <tr><td colspan="2" align="center">Imgproc.findContours() 函数 mode 和 method 参数解释</td></tr>
     * <tr><th align="center">Mode 输入参数</th><th align="center">参数解释</th></tr>
     * <tr><td align="left">RETR_EXTERNAL</td><td align="left">只检测最外围轮廓，包含在外围轮廓内的内围轮廓被忽略</td></tr>
     * <tr><td align="left">RETR_LIST</td><td align="left">检测所有的轮廓，包括内围、外围轮廓，但是检测到的轮廓不建立等级关系，彼此之间独立，没有等级关系，这就意味着这个检索模式下不存在父轮廓或内嵌轮廓，所以hierarchy向量内所有元素的第3、第4个分量都会被置为-1</td></tr>
     * <tr><td align="left">RETR_CCOMP</td><td align="left"> 检测所有的轮廓，但所有轮廓只建立两个等级关系，外围为顶层，若外围内的内围轮廓还包含了其他的轮廓信息，则内围内的所有轮廓均归属于顶层</td></tr>
     * <tr><td align="left">RETR_TREE</td><td align="left">检测所有轮廓，所有轮廓建立一个等级树结构。外层轮廓包含内层轮廓，内层轮廓还可以继续包含内嵌轮廓。</td></tr>
     * <tr><th align="center">Mthod 输入参数</th><th align="center">参数解释</th></tr>
     * <tr><td align="left">CHAIN_APPROX_NONE</td><td align="left">保存物体边界上所有连续的轮廓点到contours向量内</td></tr>
     * <tr><td align="left">CHAIN_APPROX_SIMPLE</td><td align="left">仅保存轮廓的拐点信息，把所有轮廓拐点处的点保存入contours向量内，拐点与拐点之间直线段上的信息点不予保留</td></tr>
     * <tr><td align="left">CHAIN_APPROX_TC89_L1</td><td align="left">使用teh-Chinl chain 近</td></tr>
     * <tr><td align="left">CHAIN_APPROX_TC89_KCOS </td><td align="left">使用teh-Chinl chain 近</td></tr>
     *
     * @param dealvideo Mat
     * @param rects     Rect
     * @return: Rect
     * @date 2019年7月19日 下午22:10:14
     */
    public static Rect process(Mat dealvideo, Rect rects) {
        // 1 跟踪物体在图像中的位置
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        // 2
        Mat hierarchy = new Mat();
        // 3 找出图像中物体的位置
        //Imgproc.findContours(dealvideo, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0, 0));
        Imgproc.findContours(dealvideo, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        if (contours.size() > 0) {// 4.1 如果发现图像
            double maxarea = 0.0;
            for (int t = 0; t < contours.size(); t++) {
                double area = Imgproc.contourArea(contours.get(t));
                if (area > maxarea) {
                    maxarea = area;
                    rects = Imgproc.boundingRect(contours.get(t));
                }
            }
        } else {// 4.2 如果没有发现图像
            rects.x = rects.y = rects.width = rects.height = 0;
        }
        return rects;
    }

    /**
     * OpenCV-4.0.0 基于物体颜色的对象跟踪 多对象跟踪
     * <table border="1" cellpadding="10">
     * <tr><td colspan="2" align="center">Imgproc.findContours() 函数 mode 和 method 参数解释</td></tr>
     * <tr><th align="center">Mode 输入参数</th><th align="center">参数解释</th></tr>
     * <tr><td align="left">RETR_EXTERNAL</td><td align="left">只检测最外围轮廓，包含在外围轮廓内的内围轮廓被忽略</td></tr>
     * <tr><td align="left">RETR_LIST</td><td align="left">检测所有的轮廓，包括内围、外围轮廓，但是检测到的轮廓不建立等级关系，彼此之间独立，没有等级关系，这就意味着这个检索模式下不存在父轮廓或内嵌轮廓，所以hierarchy向量内所有元素的第3、第4个分量都会被置为-1</td></tr>
     * <tr><td align="left">RETR_CCOMP</td><td align="left"> 检测所有的轮廓，但所有轮廓只建立两个等级关系，外围为顶层，若外围内的内围轮廓还包含了其他的轮廓信息，则内围内的所有轮廓均归属于顶层</td></tr>
     * <tr><td align="left">RETR_TREE</td><td align="left">检测所有轮廓，所有轮廓建立一个等级树结构。外层轮廓包含内层轮廓，内层轮廓还可以继续包含内嵌轮廓。</td></tr>
     * <tr><th align="center">Mthod 输入参数</th><th align="center">参数解释</th></tr>
     * <tr><td align="left">CHAIN_APPROX_NONE</td><td align="left">保存物体边界上所有连续的轮廓点到contours向量内</td></tr>
     * <tr><td align="left">CHAIN_APPROX_SIMPLE</td><td align="left">仅保存轮廓的拐点信息，把所有轮廓拐点处的点保存入contours向量内，拐点与拐点之间直线段上的信息点不予保留</td></tr>
     * <tr><td align="left">CHAIN_APPROX_TC89_L1</td><td align="left">使用teh-Chinl chain 近</td></tr>
     * <tr><td align="left">CHAIN_APPROX_TC89_KCOS </td><td align="left">使用teh-Chinl chain 近</td></tr>
     *
     * @param video Mat
     * @return: List<Rect>
     * @date 2019年7月19日 下午22:10:14
     */
    public static List<Rect> process(Mat video) {
        // 1 跟踪物体在图像中的位置
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        // 2找出图像中物体的位置
        Imgproc.findContours(video, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(7, 7));
        // 3 对象结果
        List<Rect> rects = new ArrayList<Rect>();
        Rect rect;
        if (contours.size() > 0) {// 4.1 如果发现图像
            for (int i = 0; i < contours.size(); i++) {
                rect = new Rect();
                double area = Imgproc.contourArea(contours.get(i));
                if (area > 0.0) {
                    rect = Imgproc.boundingRect(contours.get(i));
                } else {
                    rect.x = rect.y = rect.width = rect.height = 0;
                }
                rects.add(rect);
            }
        } else {// 4.2 如果没有发现图像
            rect = new Rect();
            rect.x = rect.y = rect.width = rect.height = 0;
            rects.add(rect);
        }
        return rects;
    }

    /**
     * 稠密光流-HF
     *
     * @return: void
     * @date 2022年2月15日12点25分
     */
    public static void HF() {
        VideoCapture capture = new VideoCapture();
        capture.open("D:\\BaiduNetdiskDownload\\video_003.avi");
        Mat prev = new Mat();
        capture.read(prev);
        Imgproc.cvtColor(prev, prev, Imgproc.COLOR_BGR2GRAY);
        Mat next = new Mat();
        Mat flow = new Mat();
        Mat dest = new Mat();
        while (capture.read(next)) {
            Imgproc.cvtColor(next, next, Imgproc.COLOR_BGR2GRAY);
            if (!prev.empty()) {
                Video.calcOpticalFlowFarneback(prev, next, flow, 0.5, 3, 5, 3, 5, 1.2, 0);
            }
            Imgproc.cvtColor(prev, dest, Imgproc.COLOR_GRAY2BGR);
            drawOpticalFlowHF(flow, dest);
            HighGui.imshow("稠密光流-HF", dest);
            HighGui.waitKey(100);
            prev = next.clone();
        }
        capture.release();
    }

    public static Mat drawOpticalFlowHF(Mat flow, Mat dst) {
        for (int i = 0, row = dst.rows(); i < row; i++) {
            for (int j = 0, col = dst.cols(); j < col; j++) {
                if (flow.get(i, j)[0] > 1D || flow.get(i, j)[1] > 1D) {
                    Imgproc.line(dst, new Point(j, i), new Point(j + flow.get(i, j)[0], i + flow.get(i, j)[1]), new Scalar(255, 0, 0), 1, 8);
                    Imgproc.circle(dst, new Point(j, i), 1, new Scalar(0, 0, 255), 1, 8);
                }
            }
        }
        return dst;
    }

}
