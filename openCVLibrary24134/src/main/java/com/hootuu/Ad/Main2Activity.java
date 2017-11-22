package com.hootuu.Ad;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import org.json.JSONException;
import org.opencv.android.OpenCVLoader;

public class Main2Activity extends UnityPlayerActivity {
/*    private final   static String key = "aRKh7sDhT536YLHORdXOFFBqJOeF5Gzv";//api_key
    private final static String secret = "zrfIe28avDUP4z36mEy9vg2csDXszxB8";
    private final static int  Count = 6;*/

       static {
    Utils.initNative();
}
    GenderFaceUtils  genderFaceUtils=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          genderFaceUtils =new GenderFaceUtils(this.getApplicationContext());
    }
    public void genderFaceDetection2(byte[] bytes){
        genderFaceUtils.faceGender(bytes);
    }




}
