package com.example.administrator.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.minhaskamal.genderRecognizer.weightedPixel.GenderDetection;

import org.opencv.android.OpenCVLoader;

/**
 * Created by Administrator on 2017/11/13.
 */

public class OpenCVActvie1 extends Activity {
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }
    static {
        if ( OpenCVLoader.initDebug()){
            System.out.println("loadOk");
        }else {
            System.out.println("loadFail");
        }
    }
  OpencvUtils opencvUtils;
    GenderDetection detector;
    int[]  mapTo={R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4};
    int[]  mapTo2={R.drawable.c1,R.drawable.c2,R.drawable.c3,R.drawable.c4};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        opencvUtils=new OpencvUtils(this.getApplicationContext());
        detector=new GenderDetection(this.getApplicationContext());

        setContentView(R.layout.test3);
    }
    int position=0;
    public void onClickb(View view){
         if (position>=mapTo.length){
             position=0;
         }
          opencvUtils.detectionFace(mapTo[position]);
          position++;
    }
    public void onClickc(View view){
        if (position>=mapTo2.length){
            position=0;
        }
        detector.testGender(mapTo2[position]);
        position++;
    }
}
