package com.minhaskamal.genderRecognizer.weightedPixel;

import android.content.Context;

import com.example.administrator.myapplication.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.IOException;

/**
 * Created by Administrator on 2017/11/13.
 */

public class GenderDetection {
  private   WeightedStandardPixelTrainer weightedStandardPixelTrainer;
private Context context;
    public GenderDetection(Context context) {
        this.context = context;
        initWeighted();
    }

    public void initWeighted(){
        weightedStandardPixelTrainer = new WeightedStandardPixelTrainer();
        String s = Utils.exportResource(context, R.raw.knowledge);
        weightedStandardPixelTrainer.load(s);
    }
    public void testGender(int id){
        try {
            long  start=System.currentTimeMillis();
            Mat mat = Utils.loadResource(context, id);
          //  Imgproc.resize(mat, mat, new Size(90,90));
            int prediction = weightedStandardPixelTrainer.predict(mat);
            long  end=System.currentTimeMillis()-start;
            System.out.println("檢測時間"+end);
            System.out.println(prediction);
            if(prediction==-1){
                System.out.println("I think " +"is not a face.");
                //	Highgui.imwrite("src/res/sample/" + faceNo + "_noface.jpg", face);
            }else if(prediction==0){
                System.out.println("I think " +  " is a female.");
                //Highgui.imwrite("src/res/sample/" + faceNo + "_female.jpg", face);
            }else{
                System.out.println("I think " +  " is a male.");
                //	Highgui.imwrite("src/res/sample/" + faceNo + "_male.jpg", face);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
