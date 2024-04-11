package com.xu.opencv.ml;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * SVM 测试
 *
 * @author hyacinth
 * @date 2024年4月6日22点36分
 * @since V1.0.0.0
 */
public class SvmTest {

    /**
     * 训练图片
     */
    private static final String IMG = "lib/opencv/img/number.png";

    /**
     * 训练结果文件
     */
    private static final String FILE = "lib/opencv/train/svm.xml";

    private static final String PRE = "lib/opencv/img/9831_t.png";

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
        //train(IMG, FILE);
        predict(PRE, FILE);
    }

    /**
     * 预测
     *
     * @param image 训练图片
     * @param file  训练文件
     * @date 2024年4月6日22点36分
     * @since V1.0.0.0
     */
    private static void predict(String image, String file) {
        // 加载训练文件
        SVM svm = SVM.load(file);

        // 预测图片
        Mat src = Imgcodecs.imread(image, Imgcodecs.IMREAD_GRAYSCALE);
        Rect rect = new Rect(0, 0, 20, 20);
        Mat dst = new Mat(src, rect);

        // 预测
        Mat img = new Mat(1, (int) dst.total() * dst.channels(), CvType.CV_32F);
        img.put(0, 0, pixel(dst));

        // 预测
        Mat result = new Mat();
        svm.predict(img, result);

        // 输出预测结果
        System.out.println("预测结果: " + (int) result.get(0, 0)[0]);
    }

    /**
     * 训练
     *
     * @param image 训练图片
     * @param file  训练文件
     * @date 2024年4月6日22点36分
     * @since V1.0.0.0
     */
    private static void train(String image, String file) {
        // 准备训练数据
        List<Mat> train = new LinkedList<>();
        List<Integer> label = new LinkedList<>();

        // 读取训练数据
        load(image, train, label);

        // 创建 SVM 分类器
        org.opencv.ml.SVM svm = org.opencv.ml.SVM.create();
        svm.setC(1);
        svm.setP(0);
        svm.setNu(0);
        svm.setCoef0(0);
        svm.setGamma(1);
        svm.setDegree(0);
        svm.setType(org.opencv.ml.SVM.C_SVC);
        svm.setKernel(org.opencv.ml.SVM.LINEAR);
        svm.setTermCriteria(new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 1000, 0));

        // 训练 SVM 模型
        Mat samples = new Mat(train.size(), (int) train.get(0).total(), CvType.CV_32FC1);
        Mat responses = new Mat(label.size(), 1, CvType.CV_32SC1);
        for (int i = 0; i < train.size(); i++) {
            samples.put(i, 0, pixel(train.get(i)));
            responses.put(i, 0, label.get(i));
        }
        svm.train(samples, Ml.ROW_SAMPLE, responses);

        // 保存模型
        svm.save(file);
    }

    /**
     * 图片转数组
     *
     * @param img 图片
     * @return 图像数组
     * @date 2024年4月6日22点36分
     * @since V1.0.0.0
     */
    private static float[] pixel(Mat img) {
        Mat mat = new Mat();
        img.convertTo(mat, CvType.CV_32F);

        int count = 0;
        float[] pixel = new float[(int) (mat.total() * mat.channels())];
        for (int i = 0, row = (int) mat.size().height; i < row; i++) {
            for (int j = 0, col = (int) mat.size().width; j < col; j++) {
                pixel[count] = (float) mat.get(i, j)[0];
                count++;
            }
        }

        return pixel;
    }

    /**
     * 加载文件
     *
     * @param image 训练图片
     * @param train 训练文件
     * @param label 训练标签
     * @date 2024年4月6日22点36分
     * @since V1.0.0.0
     */
    public static void load(String image, List<Mat> train, List<Integer> label) {
        Mat src = Imgcodecs.imread(image, Imgcodecs.IMREAD_GRAYSCALE);
        for (int i = 0; i <= 49; i++) {
            for (int j = 0; j <= 99; j++) {
                label.add((int) Math.floor(i / 5));
                Rect rect = new Rect(j * 20, i * 20, 20, 20);
                train.add(new Mat(src, rect));
            }
        }
    }

}

