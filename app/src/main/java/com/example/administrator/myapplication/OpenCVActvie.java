package com.example.administrator.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.View;

import com.minhaskamal.genderRecognizer.weightedPixel.GenderDetection;

import org.opencv.android.OpenCVLoader;

/**
 * Created by Administrator on 2017/11/13.
 */

public class OpenCVActvie extends Activity {
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

    int[]  mapTo={R.drawable.d1,R.drawable.d2,R.drawable.d3,R.drawable.d4,R.drawable.d5,R.drawable.d6};
  //  int[]  mapTo2={R.drawable.c1,R.drawable.c2,R.drawable.c3,R.drawable.c4};
    GenderFaceUtils genderFaceUtils;
    private Bitmap bitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genderFaceUtils=new GenderFaceUtils(this.getApplicationContext());
              setContentView(R.layout.test3);
        bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.timg);

    }
    int position=0;
    public void onClickb(View view){
        Bitmap  bitmap2=bitmap.copy(Bitmap.Config.ARGB_8888,true);
        genderFaceUtils.faceGender(bitmap2);
   /*      if (position>=mapTo.length){
             position=0;
         }
        genderFaceUtils.faceGender(mapTo[position]);
          position++;*/
    }
    public void onClickc(View view){
        System.out.println("CCC");
     /*   HelpsUtils.sizeValue+=10;
        if (HelpsUtils.sizeValue>100){
            HelpsUtils.sizeValue=10;
        }*/
    }
}
