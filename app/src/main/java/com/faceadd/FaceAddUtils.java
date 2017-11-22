package com.faceadd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;

import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2017/9/11.
 */

public class FaceAddUtils {
    private final   static String key = "aRKh7sDhT536YLHORdXOFFBqJOeF5Gzv";//api_key
    private final static String secret = "zrfIe28avDUP4z36mEy9vg2csDXszxB8";
    private final static int  Count = 6;
    /*byte转bitmap*/
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length > 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length,getBitmap());
        } else {
            return null;
        }
    }
    /*RGB-565 image*/
    private static BitmapFactory.Options getBitmap() {
        BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
        bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
        return bitmapOption;
    }
    public static byte[] bitmapToBytes(Bitmap b) {
        if (b==null){
            return null;
        }
        ByteArrayOutputStream c=new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100,c);
        return c.toByteArray();
    }
    public static boolean isHasFace(Bitmap bitmap, int count){
        if (bitmap==null||count<1){
            return false;
        }
        FaceDetector.Face[] faces=new FaceDetector.Face[count];
        FaceDetector detector=new FaceDetector(bitmap.getWidth(),bitmap.getHeight(),count);
        int faces1 = detector.findFaces(bitmap, faces);
        return faces1>0?true:false;
    }
    public static boolean isHasFace(Bitmap bitmap){
        if (bitmap==null){
            return false;
        }
        FaceDetector.Face[] faces=new FaceDetector.Face[Count];
        FaceDetector detector=new FaceDetector(bitmap.getWidth(),bitmap.getHeight(),Count);
        int faces1 = detector.findFaces(bitmap, faces);
        System.out.println(faces1+"==========");
        return faces1>0?true:false;
    }
    public static boolean isHasFace(byte[] bytes){
        if (bytes==null){
            return false;
        }
        Bitmap bitmap =bytesToBimap(bytes);
        FaceDetector.Face[] faces=new FaceDetector.Face[Count];
        FaceDetector detector=new FaceDetector(bitmap.getWidth(),bitmap.getHeight(),Count);
        int faces1 = detector.findFaces(bitmap, faces);
        return faces1>0?true:false;
    }
    /*is there a human face*/
    public static boolean isHasFace(byte[] bytes,int count){
       Bitmap bitmap =bytesToBimap(bytes);
        if (bitmap==null||count<1){
            return false;
        }
        FaceDetector.Face[] faces=new FaceDetector.Face[count];
        FaceDetector detector=new FaceDetector(bitmap.getWidth(),bitmap.getHeight(),count);
        int faces1 = detector.findFaces(bitmap, faces);
        return faces1>0?true:false;
    }
    /*face Detect counts*/
    public static  int[] faceDetect(byte[] b){
        CommonOperate commonOperate = new CommonOperate(key, secret, false);
        try {
            Response response1 = commonOperate.detectByte(b, 0, "gender,age");
            if (getFaceToken(response1)){
               return faceJsonParse(new String(response1.getContent()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /*net response is normal?*/
    private  static boolean getFaceToken(Response response) throws JSONException {
        if(response.getStatus() != 200){
            return false;
        }
        return true;
    }
    /* parse data*/
    private static int[] faceJsonParse(String value) {
        JSONObject jsonObject = null;
        int[] ages=null;
        try {
            jsonObject = new JSONObject(value);
            JSONArray faces = jsonObject.getJSONArray("faces");
            ages=new int[faces.length()];
            for (int i = 0; i < faces.length(); i++) {
                ages[i]=faces.getJSONObject(i).getJSONObject("attributes").getJSONObject("age").getInt("value");
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return ages;
    }


}
