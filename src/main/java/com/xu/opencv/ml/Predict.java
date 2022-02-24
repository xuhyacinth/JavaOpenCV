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
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;

/**
 * @Title: Predict.java
 * @Package com.xu.opencv.ml
 * @Description: OpenCV 机器学习
 * @author: hyacinth
 * @date: 2022年2月21日 下午22:10:14
 * @version: V-1.0.0
 * @Copyright: 2022 hyacinth
 */
public class Predict {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws Exception {
        predict();
    }

    public static void predict() throws Exception {
        SVM svm = SVM.load("D:\\OneDrive\\桌面\\ai.xml");
        BufferedReader reader = new BufferedReader(new FileReader("D:\\OneDrive\\桌面\\predict.txt"));
        Mat train = new Mat(6, 28 * 28, CvType.CV_32FC1);
        Mat label = new Mat(1, 6, CvType.CV_32SC1);
        Map<String, Mat> map = new HashMap<>(2);
        int index = 0;
        String line = null;
        while ((line = reader.readLine()) != null) {
            int[] data = Arrays.asList(line.split("->")[1].split(",")).stream().mapToInt(Integer::parseInt).toArray();
            for (int i = 0; i < 28 * 28; i++) {
                train.put(index, i, data[i]);
            }
            label.put(index, 0, Integer.parseInt(line.split("->")[0]));
            index++;
            if (index >= 6) {
                break;
            }
        }
        Mat response = new Mat();
        svm.predict(train, response);
        for (int i = 0; i < response.height(); i++) {
            System.out.println(response.get(i, 0)[0]);
        }
    }


    public static void svm() throws Exception {
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
        Map<String, Mat> map = read("D:\\OneDrive\\桌面\\data.txt", CvType.CV_32FC1, CvType.CV_32SC1);
        svm.train(map.get("train"), Ml.ROW_SAMPLE, map.get("label"));
        svm.save("D:\\OneDrive\\桌面\\ai.xml");
    }

    public static Map<String, Mat> read(String path, int train_type, int label_type) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = null;
        Mat train = new Mat(60000, 28 * 28, train_type);
        Mat label = new Mat(1, 60000, label_type);
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
