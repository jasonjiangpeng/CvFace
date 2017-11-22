package com.hjimi.color;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiDeviceAttribute;
import com.hjimi.api.iminect.ImiDeviceState;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiFrameType;
import com.hjimi.api.iminect.ImiNect;
import com.minhaskamal.genderRecognizer.weightedPixel.GenderFaceUtils;

import org.opencv.android.OpenCVLoader;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SurfaceView mColorView;
    private GLPanel mGLPanel;
    private DecodePanel mDecodePanel;
    private Surface mSurface;
    private ImageView mPreview;

    private ImiNect m_ImiNect = null;
    private ImiDevice mDevice;
    private SimpleViewer mViewer;
    private ImiFrameMode mCurrColorMode;
    private ImiDeviceAttribute mDeviceAttribute = null;

    private static final int DEVICE_OPEN_SUCCESS = 0;
    private static final int DEVICE_OPEN_FALIED = 1;
    private static final int DEVICE_DISCONNECT = 2;

    public static final int PREVIEW_BITMAP = 3;
    static {
        if ( OpenCVLoader.initDebug()){
            System.out.println("loadOk");
        }else {
            System.out.println("loadFail");
        }
    }
    private ImiDeviceState deviceState = ImiDeviceState.IMI_DEVICE_STATE_CONNECT;
GenderFaceUtils  genderFaceUtils;
    private Handler MainHandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case DEVICE_OPEN_FALIED:
                case DEVICE_DISCONNECT:
                    showMessageDialog();
                    break;
                case DEVICE_OPEN_SUCCESS:
                    runViewer();
                    break;
                case PREVIEW_BITMAP:
                    Bitmap  bitmaps= (Bitmap) msg.obj;
                    System.out.println(bitmaps.getAllocationByteCount());
                    ByteArrayOutputStream  byteArrayOutputStream =new ByteArrayOutputStream();
                    bitmaps.compress(Bitmap.CompressFormat.JPEG,1,byteArrayOutputStream);
                    if (bitmaps!=null){
                        bitmaps.recycle();
                    }
                    System.out.println(byteArrayOutputStream.toByteArray().length);
                    System.out.println(byteArrayOutputStream.toByteArray().length);
                  /*  Bitmap  bitmap =Bitmap.createScaledBitmap((Bitmap) msg.obj,200,300,true);
                    System.out.println(bitmap.getByteCount());*/
                    genderFaceUtils.faceGender(byteArrayOutputStream.toByteArray());
               //     mPreview.setImageBitmap(bitmap);
                    break;
                case 987:
                    tv.setText((String) msg.obj);
                    break;
            }
        }
    };

    private void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("The device is not connected!!!");
        builder.setPositiveButton("quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    private void runViewer() {
        mViewer = new SimpleViewer(mDevice, ImiFrameType.COLOR, MainHandler);
        switch (mCurrColorMode.getFormat())
        {
            case IMI_PIXEL_FORMAT_IMAGE_H264:
                mDecodePanel.initDecoder(mSurface, mCurrColorMode.getResolutionX(),
                        mCurrColorMode.getResolutionY());
                mViewer.setDecodePanel(mDecodePanel);
                break;
            default:
                mColorView.setVisibility(View.GONE);
                mGLPanel.setVisibility(View.VISIBLE);
                mViewer.setGLPanel(mGLPanel);
                break;
        }

        mViewer.onStart();
    }

    private class MainListener implements ImiDevice.OpenDeviceListener,ImiDevice.DeviceStateListener{

        @Override
        public void onOpenDeviceSuccess() {
            //open device success.
            mDevice = m_ImiNect.Device();
            mDeviceAttribute = mDevice.getAttribute();

            //get current color frame mode.
            mCurrColorMode = mDevice.getCurrentFrameMode(ImiFrameType.COLOR);

            MainHandler.sendEmptyMessage(DEVICE_OPEN_SUCCESS);
        }

        @Override
        public void onOpenDeviceFailed(String errorMsg) {
            //open device falied.
            MainHandler.sendEmptyMessage(DEVICE_OPEN_FALIED);
        }

        @Override
        public void onDeviceStateChanged(String deviceUri, ImiDeviceState state) {
            if (state == ImiDeviceState.IMI_DEVICE_STATE_DISCONNECT) {
                //device disconnect.
                //Toast.makeText(MainActivity.this, deviceUri+" DISCONNECT", Toast.LENGTH_SHORT).show();
                if(mDeviceAttribute != null && mDeviceAttribute.getUri().equals(deviceUri)) {
                    deviceState = ImiDeviceState.IMI_DEVICE_STATE_DISCONNECT;
                    MainHandler.sendEmptyMessage(DEVICE_DISCONNECT);
                }
            }else if(state == ImiDeviceState.IMI_DEVICE_STATE_CONNECT){
                //Toast.makeText(MainActivity.this, deviceUri+" CONNECT", Toast.LENGTH_SHORT).show();
                if(mDeviceAttribute != null && mDeviceAttribute.getUri().equals(deviceUri)) {
                    deviceState = ImiDeviceState.IMI_DEVICE_STATE_CONNECT;
                }
            }
        }
    }

    private class OpenDeviceRunnable implements Runnable{

        @Override
        public void run() {
            //get iminect instance.
            m_ImiNect = ImiNect.create(MainActivity.this);

            MainListener listener = new MainListener();

            m_ImiNect.Device().addDeviceStateListener(listener);

            m_ImiNect.Device().open(listener);
        }
    }
 private TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_view);
        genderFaceUtils=new GenderFaceUtils(this,MainHandler);
        tv= (TextView) findViewById(R.id.tv);
        ((Button)findViewById(R.id.takephoto_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewer != null){
                    mViewer.takePhoto();
                }
            }
        });

        mPreview = (ImageView) findViewById(R.id.preview);

        mColorView = (SurfaceView) findViewById(R.id.color_view);

        mColorView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mSurface = surfaceHolder.getSurface();
                mDecodePanel = new DecodePanel();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                mDecodePanel.stopDecoder();
            }
        });

        mGLPanel = (GLPanel) findViewById(R.id.sv_color_view);

        new Thread(new OpenDeviceRunnable()).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mViewer != null){
            mViewer.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mViewer != null){
            mViewer.onPause();	
        }

        //Suggest : close device as soon as possible. if exit app.

            //destroy viewer.
        if(mViewer != null){
            mViewer.onDestroy();
        }

        m_ImiNect.Device().close();

        m_ImiNect.destroy();

        finish();
		android.os.Process.killProcess(android.os.Process.myPid());

    }
}
