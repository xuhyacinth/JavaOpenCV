package com.xu.opencv.bms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

/**
 * @Title: BSM.java
 * @Package com.xu.opencv.video.bms
 * @Description: OpenCV 背景消除
 * @author: hyacinth
 * @date: 2022年2月21日 下午22:10:14
 * @version: V-1.0.0
 * @Copyright: 2022 hyacinth
 */
public class BSM {

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
        BSM_KNN();
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

    /**
     * OpenCV-4.1.0 视频分析和对象跟踪 背景消除 GMM
     *
     * @return: void
     * @date: 2019年7月19日 下午22:10:14
     */
    public static void BSM_MOG2() {
        // 1 创建 VideoCapture 对象
        VideoCapture capture = new VideoCapture(0);
        // 2 使用 VideoCapture 对象读取本地视频
        capture.open("D:\\BaiduNetdiskDownload\\video_003.avi");
        // 3 获取视频处理时的键盘输入 我这里是为了在 视频处理时如果按 Esc 退出视频对象跟踪
        int index = 0;
        // 4 使用 Mat video 保存视频中的图像帧 针对每一帧 做处理
        Mat video = new Mat();
        // 5 获取形态学结构
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3), new Point(-1, -1));
        // 6 GMM
        BackgroundSubtractorMOG2 subtractor = Video.createBackgroundSubtractorMOG2();
        Mat fgmask = new Mat();
        while (capture.read(video)) {
            // 7  提取模型 BSM
            subtractor.apply(video, fgmask);
            // 8 形态学变换
            Imgproc.morphologyEx(fgmask, fgmask, Imgproc.MORPH_OPEN, kernel, new Point(-1, -1));
            // 9 效果展示
            Optional.ofNullable(process(fgmask)).orElse(new ArrayList<>())
                    .stream().filter(Objects::nonNull).forEach(rect -> {
                        Imgproc.rectangle(fgmask, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA, 0);
                    });
            HighGui.imshow("GMM 背景消除", fgmask);
            index = HighGui.waitKey(100);
            if (index == 27) {
                capture.release();
                break;
            }
        }
    }

    /**
     * OpenCV-4.0.0
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
        List<MatOfPoint> contours = new ArrayList<>();
        // 2 找出图像中物体的位置
        Imgproc.findContours(video, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(2, 2));
        return Optional.ofNullable(contours).orElse(new ArrayList<>())
                .stream().filter(Objects::nonNull)
                .map(item -> Imgproc.boundingRect(item)).collect(Collectors.toList());
    }

    /**
     * OpenCV-4.1.0 视频分析和对象跟踪 背景消除 KNN
     *
     * @return: void
     * @date: 2019年7月19日 下午22:10:14
     */
    public static void BSM_KNN() {
        // 1 创建 VideoCapture 对象
        VideoCapture capture = new VideoCapture(0);
        // 2 使用 VideoCapture 对象读取本地视频
        capture.open("D:\\BaiduNetdiskDownload\\video_003.avi");
        // 4 使用 Mat video 保存视频中的图像帧 针对每一帧 做处理
        Mat video = new Mat();
        // 5 设置形态学结构
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2), new Point(-1, -1));
        // 6 KNN 背景消除
        BackgroundSubtractorKNN knn = Video.createBackgroundSubtractorKNN();
        Mat bitmask = new Mat();
        while (capture.read(video)) {
            // 7 提取模型 BSM
            knn.apply(video, bitmask, -1);
            // 8 形态学变换
            Imgproc.morphologyEx(bitmask, bitmask, Imgproc.MORPH_OPEN, kernel, new Point(-1, -1));
            // 9 效果展示
            Optional.ofNullable(process(bitmask)).orElse(new ArrayList<>())
                    .stream().filter(Objects::nonNull).filter(rect -> rect.width > 10).forEach(rect -> {
                        Imgproc.rectangle(video, new Point(rect.x - 3, rect.y - 3), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA, 0);
                    });
            HighGui.imshow("KNN 背景消除", video);
            int index = HighGui.waitKey(100);
            if (index == 27) {
                capture.release();
                break;
            }
        }
    }

}