package com.opencv;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.myapplication.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

public class Tutorial1Activityold extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public Tutorial1Activityold() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.tutorial1_surface_view);
        if (mIsJavaCamera)
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        else
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    @Override
    public void onResume()
    {
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
        super.onResume();

        cvGender=new CvGender(this);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemSwitchCamera = menu.add("Toggle Native/Java camera");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String toastMesage = new String();
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == mItemSwitchCamera) {
            mOpenCvCameraView.setVisibility(SurfaceView.GONE);
            mIsJavaCamera = !mIsJavaCamera;
            if (mIsJavaCamera) {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
                toastMesage = "Java Camera";
            } else {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);
                toastMesage = "Native Camera";
            }

            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);
            mOpenCvCameraView.enableView();
            Toast toast = Toast.makeText(this, toastMesage, Toast.LENGTH_LONG);
            toast.show();
        }

        return true;
    }
    public void onClickb(View view){
        mAbsoluteFaceSize=0;
        if (mRelativeFaceSize>0.9){
            mRelativeFaceSize=0.1f;
        }
        mRelativeFaceSize+=0.1f;
        log(mRelativeFaceSize+"");

    }
    public void onCameraViewStarted(int width, int height) {
        cascadeClassifier=cvGender.getCascadeClassifier();
    }

    public void onCameraViewStopped() {
    }

    private float                  mRelativeFaceSize   = 0.3f;
    private int                    mAbsoluteFaceSize   = 0;
    private CascadeClassifier cascadeClassifier;
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        long  start=System.currentTimeMillis();
            Mat  mRgba=inputFrame.rgba();
        Mat  mGray=inputFrame.gray();
        MatOfRect  matOfRect =new MatOfRect();
          int  height=mGray.height();
        if (mAbsoluteFaceSize==0){
            mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
        }
        cascadeClassifier.detectMultiScale(mGray,matOfRect,1.1,2,2,new Size(mAbsoluteFaceSize,mAbsoluteFaceSize),new Size());
        Rect[] facesArray = matOfRect.toArray();
        for (int i = 0; i < facesArray.length; i++){
            Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
            Mat mat=new Mat(mGray,facesArray[i]);
            float v = cvGender.genderDetection(mat);
            log(v);
            if (v==1){
                Core.putText(mRgba,"male",facesArray[i].br(),Core.FONT_HERSHEY_COMPLEX,1,new Scalar(0,255,0));
            }else if (v==2){
                Core.putText(mRgba,"female",facesArray[i].br(),Core.FONT_HERSHEY_COMPLEX,1,new Scalar(0,255,0));
            }

        }
        long end=System.currentTimeMillis()-start;
     log(""+end);


        return mRgba;
    }
    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
    CvGender cvGender;
    public void log(Object o){
        System.out.println("输出结果:"+o.toString());
    }
}
