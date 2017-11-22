package com.hootuu.Ad;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

import org.opencv.android.OpenCVLoader;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2017/10/19.
 */
public class Utils {

    public static void initNative(){
        OpenCVLoader.initDebug();
     /*   if (!OpenCVLoader.initDebug()){

        }else {

        }*/
    }
    public static void sendUnityMessage(String data){
        //  UnityPlayer.UnitySendMessage("Main Camera","message","未检测到人脸");
        UnityPlayer.UnitySendMessage("FaceDetect","message",data);
    }
    private static int[] convertByteToColor(byte[] data){
        int size = data.length;
        if (size == 0){
            return null;
        }
        // 理论上data的长度应该是3的倍数，这里做个兼容
        int arg = 0;
        if (size % 3 != 0){
            arg = 1;
        }
        int []color = new int[size / 3 + arg];
        if (arg == 0){                                  //  正好是3的倍数
            for(int i = 0; i < color.length; ++i){
                color[i] = (data[i * 3] << 16 & 0x00FF0000) |
                        (data[i * 3 + 1] << 8 & 0x0000FF00 ) |
                        (data[i * 3 + 2] & 0x000000FF ) |
                        0xFF000000;
            }
        }else{                                      // 不是3的倍数
            for(int i = 0; i < color.length - 1; ++i){
                color[i] = (data[i * 3] << 16 & 0x00FF0000) |
                        (data[i * 3 + 1] << 8 & 0x0000FF00 ) |
                        (data[i * 3 + 2] & 0x000000FF ) |
                        0xFF000000;
            }
            color[color.length - 1] = 0xFF000000;                   // 最后一个像素用黑色填充
        }
        return color;
    }
    public static Bitmap rgbToBitmap(byte[] bytes, int w, int h){
        int[]  data=convertByteToColor(bytes);
        Bitmap src= Bitmap.createBitmap(data,w,h, Bitmap.Config.ARGB_8888);
        Bitmap dst=src.copy(Bitmap.Config.RGB_565,true);
        Bitmap dst2=rotateBitmap(dst,180);
        return dst2;
    }
    public static byte[] bitmapToBytes(Bitmap b)
    {
        if (b == null) {
            return null;
        }
        ByteArrayOutputStream c = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, c);
        return c.toByteArray();
    }
    /*图片旋转*/
    private static Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        return newBM;
    }
    public static byte[] rotateBitmap(byte[] bytes){
        BitmapFactory.Options  options =new BitmapFactory.Options();
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length, options);
        Bitmap dst=rotateBitmap(bitmap,180);
        return bitmapToBytes(dst);
    }
}
