package com.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.FaceDetector;

import com.example.administrator.myapplication.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.CvSVM;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.HOGDescriptor;

import java.io.IOException;

import static org.opencv.core.CvType.CV_32FC1;

/**
 * Created by Administrator on 2017/11/21.
 */

public class CvGender {
  private   CvSVM svm;
    private Context context;
    private CascadeClassifier cascadeClassifier;
    public CvGender(Context context) {
        this.context=context;
        svm=new  CvSVM();
        cascadeClassifier=new CascadeClassifier();
        cascadeClassifier.load(Utils.exportResource(context,R.raw.lbpcascade_frontalface));
        svm.load(getSvmFile());
    }

    public CascadeClassifier getCascadeClassifier() {
        return cascadeClassifier;
    }

    public void setCascadeClassifier(CascadeClassifier cascadeClassifier) {
        this.cascadeClassifier = cascadeClassifier;
    }

    public float genderDetection(Mat mat){

        Imgproc.resize(mat, mat, new  Size(64, 64));
        MatOfFloat  matOfFloat=new MatOfFloat();
        MatOfPoint matOfPoint=new MatOfPoint();
        HOGDescriptor hogDescriptor=new HOGDescriptor(new Size(64,64),new Size(16,16),new Size(8,8),new Size(8,8),9);
        hogDescriptor.compute(mat, matOfFloat,new Size(1,1),new Size(0,0),matOfPoint);
        Mat  matd =Mat.zeros(1,matOfFloat.toArray().length,CV_32FC1);
        for (int i = 0; i <matOfFloat.toArray().length ; i++) {
                matd.put(0,i,matOfFloat.toArray()[i]);
          
        }
        return svm.predict(matd);
    }
    public Mat getDetectionMap(int resources){
    Mat  mat=null;
        try {
            mat= Utils.loadResource(context,resources);
        } catch (IOException e) {
            e.printStackTrace();
        }
      return mat;
    }
    public Mat getDetectionMap(Bitmap resources){
        Mat  mat=new Mat();
        Utils.bitmapToMat(resources,mat);

        return mat;
    }
    public float startDetection(int  resources){
        return genderDetection(getDetectionMap(resources));
    }
    public float startDetection(Mat  mat){
        return genderDetection(mat);
    }
    public float startDetection(Bitmap resources){
        return genderDetection(getDetectionMap(resources));
    }
    public String getSvmFile(){
        return  Utils.exportResource(context,R.raw.svmsex);
    }
     public void faceMat(Mat mat){

     }
}
