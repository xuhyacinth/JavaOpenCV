package com.xu.opencv.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.ANN_MLP;
import org.opencv.ml.Boost;
import org.opencv.ml.DTrees;
import org.opencv.ml.EM;
import org.opencv.ml.KNearest;
import org.opencv.ml.LogisticRegression;
import org.opencv.ml.Ml;
import org.opencv.ml.NormalBayesClassifier;
import org.opencv.ml.RTrees;
import org.opencv.ml.SVM;
import org.opencv.ml.SVMSGD;
import org.opencv.ml.TrainData;

/**
 * @Title: ML.java
 * @Package com.xu.opencv.ml
 * @Description: OpenCV 机器学习
 * @author: hyacinth
 * @date: 2022年2月21日 下午22:10:14
 * @version: V-1.0.0
 * @Copyright: 2022 hyacinth
 */
public class ML {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws Exception {

        predict();
    }

    public static void predict() throws Exception {
        ANN_MLP ann = ANN_MLP.load("D:\\OneDrive\\桌面\\ann.xml");
        BufferedReader reader = new BufferedReader(new FileReader("D:\\OneDrive\\桌面\\predict.txt"));
        Mat train = new Mat(6, 28 * 28, CvType.CV_32FC1);
        Mat label = new Mat(6, 10, CvType.CV_32SC1);
        Map<String, Mat> map = new HashMap<>(2);
        int index = 0;
        String line = null;
        while ((line = reader.readLine()) != null) {
            int[] data = Arrays.asList(line.split("->")[1].split(",")).stream().mapToInt(Integer::parseInt).toArray();
            for (int i = 0; i < 28 * 28; i++) {
                train.put(index, i, (float) data[i]);
            }
            int col = Integer.parseInt(line.split("->")[0]);
            label.put(index, col, (float) col);
            index++;
            if (index >= 6) {
                break;
            }
        }
        Mat response = new Mat();
        ann.predict(train, response);
        for (int i = 0; i < response.height(); i++) {
            System.out.println(Arrays.toString(response.get(i, 0)));
        }
    }

    /**
     * Artificial Neural Networks -- Multi-Layer Perception 人工神经网络--多层感知器
     *
     * @throws Exception
     */
    public static void ANN_MLP() throws Exception {
        ANN_MLP ann = ANN_MLP.create();
        /**
         * 输入层：对应着每个图片的像素，所以是28*28
         * 隐含层：两个，神经元个数分别为 512 和 256
         * 输出层：和训练的标签对应，神经元为10个，即手写数字 0123456789
         */
        int[] layer = {28 * 28, 512, 256, 10};
        Mat layerSizes = new Mat(1, layer.length, CvType.CV_32FC1);
        for (int i = 0; i < layer.length; i++) {
            layerSizes.put(0, i, layer[i]);
        }
        ann.setLayerSizes(layerSizes);
        ann.setBackpropWeightScale(0.1);
        ann.setBackpropMomentumScale(0.1);
        ann.setTrainMethod(ANN_MLP.BACKPROP);
        ann.setActivationFunction(ANN_MLP.SIGMOID_SYM);
        ann.setTermCriteria(new TermCriteria(TermCriteria.MAX_ITER, 100000, 0.000001));

        Map<String, Mat> map = read("D:\\OneDrive\\桌面\\train.txt", CvType.CV_32FC1, CvType.CV_32FC1);
        TrainData data = TrainData.create(map.get("train"), Ml.ROW_SAMPLE, map.get("label"));
        ann.train(data);
        ann.save("D:\\OneDrive\\桌面\\ann.xml");

        Mat response = new Mat();
        ann.predict(map.get("train"), response);
        for (int i = 0; i < response.height(); i++) {
            System.out.println(response.get(i, 0)[0]);
        }

        ann.clear();
    }

    public static Map<String, Mat> read(String path, int train_type, int label_type) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        Mat train = new Mat(600, 28 * 28, train_type);
        Mat label = new Mat(600, 10, label_type);
        Map<String, Mat> map = new HashMap<>(2);
        int index = 0;
        String line = null;
        while ((line = reader.readLine()) != null) {
            int[] data = Arrays.asList(line.split("->")[1].split(",")).stream().mapToInt(Integer::parseInt).toArray();
            for (int i = 0; i < 28 * 28; i++) {
                train.put(index, i, (float) data[i]);
            }
            int col = Integer.parseInt(line.split("->")[0]);
            label.put(index, col, (float) col);
            index++;
            if (index >= 600) {
                break;
            }
        }
        map.put("train", train);
        map.put("label", label);
        reader.close();
        return map;
    }

    /**
     * 决策树 Boosted tree
     *
     * @throws Exception
     */
    public static void Boost() throws Exception {
        Boost boost = Boost.create();
        Map<String, Mat> map = read("D:\\OneDrive\\桌面\\data.txt");
        boost.train(map.get("train"), Ml.ROW_SAMPLE, map.get("label"));
        boost.save("D:\\OneDrive\\桌面\\Boost.xml");
    }

    /**
     * 决策树 Decision Tree
     *
     * @throws Exception
     */
    public static void DTrees() throws Exception {
        DTrees tree = DTrees.create();
        tree.setMaxCategories(10); // 设置分类数为 0-9
        tree.setMaxDepth(10);
        tree.setMinSampleCount(10);
        tree.setCVFolds(0);
        tree.setUseSurrogates(false);
        tree.setUse1SERule(false);
        tree.setTruncatePrunedTree(false);
        tree.setRegressionAccuracy(0);
        tree.setPriors(new Mat());
        Map<String, Mat> map = read("D:\\OneDrive\\桌面\\train.txt");
        tree.train(map.get("train"), Ml.ROW_SAMPLE, map.get("label"));
        tree.save("D:\\OneDrive\\桌面\\DTrees.xml");
    }

    /**
     * EM算法 Expectation - Maximization
     *
     * @throws Exception
     */
    public static void EM() throws Exception {
        EM em = EM.create();
        Map<String, Mat> map = read("D:\\OneDrive\\桌面\\data.txt");
        em.train(map.get("train"), Ml.ROW_SAMPLE, map.get("label"));
        em.save("D:\\OneDrive\\桌面\\em.xml");
    }


    /**
     * K-邻近算法 K-Nearest Neighbour Classifier
     *
     * @throws Exception
     */
    public static void KNN() throws Exception {
        KNearest knn = KNearest.create();
        knn.setDefaultK(5);
        knn.setIsClassifier(true);
        Map<String, Mat> map = read("D:\\OneDrive\\桌面\\train.txt");
        TrainData data = TrainData.create(map.get("train"), Ml.ROW_SAMPLE, map.get("label"));
        knn.train(data);
        knn.save("D:\\OneDrive\\桌面\\knn.xml");
    }

    /**
     * 逻辑回归 logistic regression
     *
     * @throws Exception
     */
    public static void LogisticRegression() throws Exception {
        LogisticRegression logic = LogisticRegression.create();
        logic.setLearningRate(0.00001);
        logic.setIterations(100);
        logic.setRegularization(LogisticRegression.REG_DISABLE);
        logic.setTrainMethod(LogisticRegression.MINI_BATCH);
        logic.setMiniBatchSize(1);
        Map<String, Mat> map = read("D:\\OneDrive\\桌面\\train.txt");
        logic.train(map.get("train"), Ml.ROW_SAMPLE, map.get("label"));
        logic.save("D:\\OneDrive\\桌面\\logic.xml");
    }

    /**
     * 贝叶斯分类 Normal Bayes Classifier
     *
     * @throws Exception
     */
    public static void NormalBayesClassifier() throws Exception {
        NormalBayesClassifier nbc = NormalBayesClassifier.create();
        Map<String, Mat> map = read("D:\\OneDrive\\桌面\\data.txt");
        nbc.train(map.get("train"), Ml.ROW_SAMPLE, map.get("label"));
        nbc.save("D:\\OneDrive\\桌面\\nbc.xml");
    }

    /**
     * 随机深林 Random Forest
     *
     * @throws Exception
     */
    public static void RTrees() throws Exception {
        RTrees tree = RTrees.create();
        Map<String, Mat> map = read("D:\\OneDrive\\桌面\\data.txt");
        tree.train(map.get("train"), Ml.ROW_SAMPLE, map.get("label"));
        tree.save("D:\\OneDrive\\桌面\\RTrees.xml");
    }

    /**
     * 支持向量机 Support Vector Machines
     *
     * @throws Exception
     */
    public static void SVM() throws Exception {
        SVM svm = SVM.create();
        svm.setC(1);
        svm.setP(0);
        svm.setNu(0);
        svm.setCoef0(0);
        svm.setGamma(1);
        svm.setDegree(0);
        svm.setType(SVM.C_SVC);
        svm.setKernel(SVM.LINEAR);
        svm.setTermCriteria(new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 1000, 0));
        Map<String, Mat> map = read("D:\\OneDrive\\桌面\\data.txt");
        svm.train(map.get("train"), Ml.ROW_SAMPLE, map.get("label"));
        svm.save("D:\\OneDrive\\桌面\\svm.xml");
    }

    /**
     * SVMSGD 随机梯度下降SVM分类器
     *
     * @throws Exception
     */
    public static void SVMSGD() throws Exception {
        SVMSGD svmsgd = SVMSGD.create();
        Map<String, Mat> map = read("D:\\OneDrive\\桌面\\data.txt");
        svmsgd.train(map.get("train"), Ml.ROW_SAMPLE, map.get("label"));
        svmsgd.save("D:\\OneDrive\\桌面\\svmsgd.xml");
    }

    public static Map<String, Mat> read(String path) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = null;
        Mat train = new Mat(60000, 28 * 28, CvType.CV_32FC1);
        Mat label = new Mat(1, 60000, CvType.CV_32SC1);
        Map<String, Mat> map = new HashMap<>(2);
        int index = 0;
        while ((line = reader.readLine()) != null) {
            int[] data = Arrays.asList(line.split("->")[1].split(",")).stream().mapToInt(Integer::parseInt).toArray();
            for (int i = 0; i < 28 * 28; i++) {
                train.put(index, i, data[i]);
            }
            label.put(index, 0, Integer.parseInt(line.split("->")[0]));
            index++;
        }
        map.put("train", train);
        map.put("label", label);
        reader.close();
        return map;
    }

}
