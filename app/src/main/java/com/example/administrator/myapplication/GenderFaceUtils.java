package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Bitmap;

import com.minhaskamal.genderRecognizer.weightedPixel.WeightedStandardPixelTrainer;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.IOException;

/**
 * Created by Administrator on 2017/11/13.
 */

public class GenderFaceUtils {
    private WeightedStandardPixelTrainer weightedStandardPixelTrainer;
    private CascadeClassifier cascadeClassifier;
    private Context context;
    public GenderFaceUtils(Context context) {
        this.context=context;
        init();
    }
    public void init(){
        cascadeClassifier=new CascadeClassifier();
        String  na=Utils.assetsFile(context,"haarcascade_frontalface_alt.xml");
        weightedStandardPixelTrainer = new WeightedStandardPixelTrainer();
        String s = Utils.assetsFile(context, "knowledge.log");
        weightedStandardPixelTrainer.load(s);
        cascadeClassifier.load(na);
    }

    public void faceGender(int va){
        HelpsUtils.logSout(String.valueOf(HelpsUtils.sizeValue));
        long  start=System.currentTimeMillis();
        Mat  mat= null;
        try {
            mat = Utils.loadResource(context,va);
            Long  end=System.currentTimeMillis()-start;
            HelpsUtils.logSout(end+"转换时间");
            int  minSize=Math.max(mat.width(),mat.height());
            int  min=minSize/3;
            int  max=minSize/8;
            MatOfRect matOfRect=new MatOfRect();
            cascadeClassifier.detectMultiScale(mat,matOfRect,1.1,3,0,new Size(max,max),new Size(min,min));
            HelpsUtils.logSout(matOfRect.total());
            Long  end3=System.currentTimeMillis()-start;
            HelpsUtils.logSout(end3+"检测时间");
            if (matOfRect.total()>0){
                for (int i = 0; i <matOfRect.total() ; i++) {
                    //int  c=Math.max(b)
                    Rect  re =matOfRect.toArray()[i];
                    Mat  ms=new Mat(mat,re);
                    Integer predict = weightedStandardPixelTrainer.predict(ms);
                    HelpsUtils.logSout(predict);
                }
            }
            Long  end2=System.currentTimeMillis()-start;
            HelpsUtils.logSout(end2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void faceGender(Bitmap os){
        com.hootuu.Ad.HelpsUtils.logSout(String.valueOf(com.hootuu.Ad.HelpsUtils.sizeValue));
        long  start=System.currentTimeMillis();
        Mat mat = new Mat();
        Utils.bitmapToMat(os,mat);
        Long  end=System.currentTimeMillis()-start;
        com.hootuu.Ad.HelpsUtils.logSout(end+"转换时间");
        int  minSize=Math.max(mat.width(),mat.height());
        int  min=minSize/3;
        int  max=minSize/8;
        System.out.println(mat.type()+"type");
        System.out.println(mat.channels()+"channels");
        System.out.println(mat.depth()+"depth");
        MatOfRect matOfRect=new MatOfRect();
        cascadeClassifier.detectMultiScale(mat,matOfRect,1.1,3,0,new Size(max,max),new Size(min,min));
        com.hootuu.Ad.HelpsUtils.logSout(matOfRect.total());
        Long  end3=System.currentTimeMillis()-start;
        com.hootuu.Ad.HelpsUtils.logSout(end3+"检测时间");
        if (matOfRect.total()>0){
            //    com.hootuu.Ad.Utils.sendUnityMessage(value);
            /*    for (int i = 0; i <matOfRect.total() ; i++) {
                    //int  c=Math.max(b)
                    Rect  re =matOfRect.toArray()[i];
                    Mat  ms=new Mat(mat,re);
                    Integer predict = weightedStandardPixelTrainer.predict(ms);
                    HelpsUtils.logSout(predict);
                }*/
        }else {
            return;
        }
        Rect  re =matOfRect.toArray()[0];
        Mat  ms=new Mat(mat,re);
        int  a= weightedStandardPixelTrainer.predict(ms);
        String  sex="";
        if (a==1){
            sex="男";
        }else if (a==0){
            sex="女";
        }else {
            sex="错误";
        }
        Long  end2=System.currentTimeMillis()-start;
        com.hootuu.Ad.HelpsUtils.logSout(end2);
        String   value=matOfRect.total()+"人"+sex+"检测时间"+end2;
        System.out.println(value);
    }
    public int matofRectHold(Mat  ms,MatOfRect matOfRect) {
        if (matOfRect.total() < 1) {
            return -1;
        } else if (matOfRect.total() == 1) {
            Mat msd = new Mat(ms, matOfRect.toArray()[0]);
            return weightedStandardPixelTrainer.predict(ms);
        }

        return 0;

    }

}
