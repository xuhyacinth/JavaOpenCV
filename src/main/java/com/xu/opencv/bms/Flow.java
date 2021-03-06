package com.xu.opencv.bms;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

/**
 * @Title: Flow.java
 * @Package com.xu.opencv.video.bms
 * @Description: OpenCV 光流对象跟踪
 * @author: hyacinth
 * @date: 2022年2月21日 下午22:10:14
 * @version: V-1.0.0
 * @Copyright: 2022 hyacinth
 */
public class Flow {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        HF();
    }

    /**
     * OpenCV 视频分析和对象跟踪 背景消除 稠密光流(HF)
     *
     * @return: void
     * @date 2022年2月15日12点25分
     */
    public static void HF() {
        VideoCapture capture = new VideoCapture();
        capture.open("lib\\video\\video_001.avi");
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
            deal(flow, dest);
            HighGui.imshow("稠密光流(HF) 背景消除", dest);
            HighGui.waitKey(100);
            prev = next.clone();
        }
        capture.release();
    }

    /**
     * 染色
     *
     * @param flow
     * @param dst
     * @return
     */
    public static Mat deal(Mat flow, Mat dst) {
        for (int i = 0, row = dst.rows(); i < row; i++) {
            for (int j = 0, col = dst.cols(); j < col; j++) {
                if (flow.get(i, j)[0] > 1D || flow.get(i, j)[1] > 1D) {
                    Imgproc.circle(dst, new Point(j, i), 1, new Scalar(0, 0, 255), 2, Imgproc.LINE_AA);
                }
            }
        }
        return dst;
    }

}
