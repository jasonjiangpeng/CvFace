package com.example.administrator.myapplication;

import android.content.Context;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/11/13.
 */

public class OpencvUtils {

    private File mCascadeFile;
    private CascadeClassifier cascadeClassifier;
    private Context context;

    public OpencvUtils(Context context) {
        this.context=context;
        initCascadeClassifier();

    }

    private void initCascadeClassifier(){
        cascadeClassifier=new CascadeClassifier();
        cascadeClassifier.load(getPath());
    }
    public void detectionFace(int photo){
        try {
            long  start= System.currentTimeMillis();
            Mat mat = Utils.loadResource(context, photo);
            MatOfRect matOfRect=new MatOfRect();
           cascadeClassifier.detectMultiScale(mat,matOfRect,1.1,3,0,new Size(10,10),new Size(500,500));
            long  end= System.currentTimeMillis()-start;
            System.out.println("檢測時間"+end);
            System.out.println("檢測人臉數量"+matOfRect.total());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void  writeFile(){
        try {
            InputStream is = context.getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isHasFile(Context context){
        File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
        return mCascadeFile.exists();
    }
    public String getPath(){
        if (isHasFile(context)){
        }else {
            writeFile();
        }
        return mCascadeFile.getAbsolutePath();
    }

}
