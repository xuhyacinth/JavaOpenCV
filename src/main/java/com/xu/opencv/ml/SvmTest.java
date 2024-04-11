package com.xu.opencv.ml;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
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
     * SVM训练图片宽
     */
    private static final int WIDTH = 28;

    /**
     * SVM训练图片高
     */
    private static final int HEIGHT = 28;

    /**
     * SVM训练结果文件
     */
    private static final String FILE = "lib/opencv/train/svm.xml";

    /**
     * SVM训练图片地址
     */
    private static final String TRAIN_FILE_PATH = "C:\\Users\\xuyq\\Desktop\\train\\";

    /**
     * SVM预测图片地址
     */
    private static final String PREDICT_FILE_PATH = "C:\\Users\\xuyq\\Desktop\\perdict\\";

    /**
     * SVM预测图片
     */
    private static final String TEST = "lib/opencv/img/9831_t.png";

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

    public static void main(String[] args) throws Exception {
        train(TRAIN_FILE_PATH, FILE);
        //predict(TEST, FILE);
        count();
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
        // 预测图片
        Mat src = Imgcodecs.imread(image, Imgcodecs.IMREAD_GRAYSCALE);
        Mat dst = resize(src, WIDTH, HEIGHT);

        // 预测
        Mat img = new Mat(1, WIDTH * HEIGHT, CvType.CV_32F);
        img.put(0, 0, pixel(dst));

        // 加载训练文件
        SVM svm = SVM.load(file);

        // 预测
        Mat result = new Mat();
        svm.predict(img, result);

        // 输出预测结果
        System.out.println("预测结果: " + (int) result.get(0, 0)[0]);
    }

    /**
     * 训练
     *
     * @param path 训练图片地址
     * @param file 训练文件
     * @date 2024年4月6日22点36分
     * @since V1.0.0.0
     */
    private static void train(String path, String file) throws Exception {
        // 准备训练数据
        List<Mat> train = new LinkedList<>();
        List<Integer> label = new LinkedList<>();

        // 读取训练数据
        loadData(path, train, label);

        // 创建 SVM 分类器
        SVM svm = SVM.create();
        svm.setC(1);
        svm.setP(0);
        svm.setNu(0);
        svm.setCoef0(0);
        svm.setGamma(1);
        svm.setDegree(0);
        svm.setType(SVM.C_SVC);
        svm.setKernel(SVM.RBF);
        svm.setTermCriteria(new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 100000, 0));

        // 训练 SVM 模型
        Mat samples = new Mat(train.size(), WIDTH * HEIGHT, CvType.CV_32FC1);
        Mat responses = new Mat(label.size(), 1, CvType.CV_32SC1);
        for (int i = 0; i < train.size(); i++) {
            samples.put(i, 0, pixel(train.get(i)));
            responses.put(i, 0, label.get(i));
        }
        svm.train(samples, Ml.ROW_SAMPLE, responses);

        // 保存模型
        svm.save(file);
    }

    private static void count() throws Exception {
        // 准备训练数据
        List<Mat> train = new LinkedList<>();
        List<Integer> label = new LinkedList<>();

        // 读取训练数据
        loadData(PREDICT_FILE_PATH, train, label);

        // 加载训练文件
        SVM svm = SVM.load(FILE);

        int numCorrect = 0;
        for (int i = 0; i < train.size(); i++) {
            Mat dst = resize(train.get(i), WIDTH, HEIGHT);

            // 预测
            Mat img = new Mat(1, WIDTH * HEIGHT, CvType.CV_32F);
            img.put(0, 0, pixel(dst));
            int predictLabel = (int) svm.predict(img);
            if (predictLabel == label.get(i)) {
                numCorrect++;
            }
        }

        double accuracy = (double) numCorrect / train.size() * 100;
        System.out.println("Accuracy: " + accuracy + "%");

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
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
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

    /**
     * 加载文件
     *
     * @param path  训练图片文件地址
     * @param train 训练文件
     * @param label 训练标签
     * @date 2024年4月6日22点36分
     * @since V1.0.0.0
     */
    private static void loadData(String path, List<Mat> train, List<Integer> label) throws Exception {
        File[] folders = new File(path).listFiles();
        for (File folder : folders) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (!file.exists() || file.isDirectory()) {
                    continue;
                }
                String number = file.getName().split("_")[0];
                label.add(Integer.parseInt(number));
                Mat src = Imgcodecs.imread(file.getCanonicalPath(), Imgcodecs.IMREAD_GRAYSCALE);
                train.add(src);
            }
        }
    }

    public static Mat resize(Mat src, int width, int height) {
        Mat img = new Mat();
        Size dst = new Size(width, height);
        Imgproc.resize(src, img, dst);
        return img;
    }

}

