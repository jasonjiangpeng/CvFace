package com.hjimi.color;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Handler;

import com.hjimi.api.iminect.ImiPixelFormat;
import com.hjimi.cvtImage.ImageCvt;
import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiFrameType;
import com.hjimi.api.iminect.ImiImageFrame;
import com.hjimi.api.iminect.Utils;
import com.minhaskamal.genderRecognizer.weightedPixel.GenderFaceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


public class SimpleViewer extends Thread {

    private boolean mShouldRun = false;
    private boolean mTakePhoto = false;

    private ImiFrameType mFrameType;
    private GLPanel mGLPanel;
    private DecodePanel mDecodePanel;
    private ImiDevice mDevice;
    private ImiFrameMode mCurrentMode;

    private Handler mOutHandler;
 private GenderFaceUtils  genderFaceUtils;
    public SimpleViewer(ImiDevice device, ImiFrameType frameType, Handler handler) {
        mDevice = device;
        mFrameType = frameType;
        mOutHandler = handler;

    }
    public SimpleViewer(Context context,ImiDevice device, ImiFrameType frameType, Handler handler) {
        mDevice = device;
        mFrameType = frameType;
        mOutHandler = handler;
        genderFaceUtils=new GenderFaceUtils(context,handler);
    }

    public void setGLPanel(GLPanel GLPanel) {
        this.mGLPanel = GLPanel;
    }

    public void setDecodePanel(DecodePanel decodePanel) {
        this.mDecodePanel = decodePanel;
    }

    @Override
    public void run() {
        super.run();

        //open stream.
        mDevice.openStream(mFrameType);

        mDevice.setFrameMode(ImiFrameType.COLOR,new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_IMAGE_RGB24, 1280, 720));

        //get current framemode.
        mCurrentMode = mDevice.getCurrentFrameMode(mFrameType);

        //start read frame.
        while (mShouldRun) {
            ImiImageFrame nextFrame = mDevice.readNextFrame(mFrameType, 25);

            //frame maybe null, if null, continue.
            if(nextFrame == null){
                continue;
            }

            if(mTakePhoto){
                Bitmap cropBitmap = Bitmap.createBitmap(nextFrame.getWidth(), nextFrame.getHeight(), Bitmap.Config.ARGB_8888);
                ImageCvt.RGB2Bitmap(nextFrame.getData(), nextFrame.getWidth(), nextFrame.getHeight(), cropBitmap);
                mOutHandler.obtainMessage(MainActivity.PREVIEW_BITMAP, cropBitmap).sendToTarget();
             /*   ByteArrayOutputStream  byteArrayOutputStream =new ByteArrayOutputStream();
                cropBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);*/

                mTakePhoto = false;
            }

            //draw color.
            drawColor(nextFrame);
        }
    }

    /**
     *
     *
     *  File f;
     int c = 0;c++;
     if(c==300 && f == null){
     f = new File("/sdcard/test.jpg");
     try {
     f.createNewFile();
     } catch (IOException e) {
     e.printStackTrace();
     }
     byte[] data = new byte[460800];
     frameData.get(data);
     YuvImage yuv = new YuvImage(data, ImageFormat.NV21, 640, 480, null);
     FileOutputStream out = null;
     try {
     out = new FileOutputStream(f);
     } catch (FileNotFoundException e) {
     e.printStackTrace();
     }
     yuv.compressToJpeg(new Rect(0, 0, 640, 480), 100, out);//相片质量
     }

     */

    private void drawColor(ImiImageFrame nextFrame) {
        ByteBuffer frameData = nextFrame.getData();
        int width = nextFrame.getWidth();
        int height = nextFrame.getHeight();

        //draw color image.
        switch (mCurrentMode.getFormat())
        {
            case IMI_PIXEL_FORMAT_IMAGE_H264:
                if(mDecodePanel != null){
                    mDecodePanel.paint(frameData, nextFrame.getTimeStamp());
                }
                break;
            case IMI_PIXEL_FORMAT_IMAGE_YUV420SP:
                frameData = Utils.yuv420sp2RGB(nextFrame);
                if(mGLPanel != null){
                    mGLPanel.paint(null, frameData, width, height);
                }
                break;
            case IMI_PIXEL_FORMAT_IMAGE_RGB24:
                if(mGLPanel != null){
                    mGLPanel.paint(null, frameData, width, height);
                }
                break;
            default:
                break;
        }
    }

    public void onPause(){
        if(mGLPanel != null){
            mGLPanel.onPause();
        }
    }

    public void onResume(){
        if(mGLPanel != null){
            mGLPanel.onResume();
        }
    }

    public void onStart(){
        if(!mShouldRun){
            mShouldRun = true;

            //start read thread
            this.start();
        }
    }

    public void onDestroy(){
        mShouldRun = false;

        mDevice.closeStream(mFrameType);
    }

    public void takePhoto() {
        mTakePhoto = true;
    }
}
