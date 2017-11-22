package com.hjimi.cvtImage;

import android.graphics.Bitmap;
import android.util.Log;

import java.nio.ByteBuffer;


public class ImageCvt {

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("ImiImageCvt");
    }
    public static native int RGB2Bitmap(ByteBuffer srcBuffer, int width, int height, Bitmap outBitmap);
}
