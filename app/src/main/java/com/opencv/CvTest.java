package com.opencv;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.administrator.myapplication.R;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;

/**
 * Created by Administrator on 2017/11/21.
 */

public class CvTest extends Activity {
    static {
        if (OpenCVLoader.initDebug()){
            System.out.println("pass");
        }else {
            System.out.println("fail");
        }
    }
    CvGender cvGender;
    CameraBridgeViewBase cameraBridgeViewBase;
    public int[]  maps={R.drawable.c1,R.drawable.c2,R.drawable.c3,R.drawable.c4};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cvcamera);

    }
    int position=0;
    public void onClickb(View view){
       if (position>=maps.length){
           position=0;
       }
       long  start=System.currentTimeMillis();
        System.out.println( cvGender.startDetection(maps[position]));
        System.out.println( System.currentTimeMillis()-start);
        position++;
    }
}
