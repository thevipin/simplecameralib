package project.everyday.dev.simplecameralibeary.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import project.everyday.dev.simplecameralibeary.R;
import project.everyday.dev.simplecameralibeary.utils.PermissionUtils;
import project.everyday.dev.simplecameralibeary.utils.Utils;

/**
 * Created by vipin on 23-03-2018.
 */

public class CameraView extends FrameLayout {
    private FrameLayout mFrameLayout;
    private TextView mTxtMessage;
    private CameraSurfaceView mCameraSurfaceView;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private Context mContext;
    private Boolean mAudioOptional = false;
    private int mCameraId = 0;
    private boolean mIsVideoIsRecording = false;

    public CameraView(Context context) {
        super(context);
        init();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public boolean isVideoIsRecording() {
        return mIsVideoIsRecording;
    }

    private void init() {
        mContext = getContext();
        mFrameLayout = new FrameLayout(mContext);
        mFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mTxtMessage = new TextView(mContext);
        mTxtMessage.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        mTxtMessage.setText(R.string.unsupported_camera);
        mTxtMessage.setAllCaps(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTxtMessage.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
        }
        mFrameLayout.addView(mTxtMessage);

        this.addView(mFrameLayout);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mIsVideoIsRecording) {
            mIsVideoIsRecording = false;
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mCamera.lock();
            mMediaRecorder = null;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    public void showCameraPerview() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mTxtMessage.setText("Permission Denied");
            return;
        }
        initCameraPreview(mCameraId);
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void prepairVideoRecord(@NonNull final String outputPath) throws Exception {
        String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (mCamera != null) {
            File file = new File(outputPath);
            if (!file.exists())
                throw new FileNotFoundException(outputPath + " is not exist");
            boolean audioAccess = PermissionUtils.hasPermission(mContext, Manifest.permission.RECORD_AUDIO);
            if (ismAudioOptional()) {
                initVideoRecord(outputPath, audioAccess);
            } else {
                if (!audioAccess)
                    throw new Exception("Record Permission Denied");
                initVideoRecord(outputPath, true);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // let the super class handle calculations
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    protected void initCameraPreview(@IntRange(from = 0, to = 6) final int cameraId) {
        if (Utils.checkDeviceHasCameraHardware(mContext)) {
            try {
                mCamera = Utils.getCameraInstance(cameraId);
                mCameraSurfaceView = new CameraSurfaceView(mContext, mCamera);
                //mFrameLayout.removeAllViews();
                mFrameLayout.addView(mCameraSurfaceView);
            } catch (Exception e) {
                mTxtMessage.setText(e.getMessage());
            }

        }
    }


    protected void initVideoRecord(@NonNull String outputPath, boolean isAudioEnabled) {
        mMediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        if (isAudioEnabled) {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }
        mMediaRecorder.setProfile(CamcorderProfile.get(mCameraId, CamcorderProfile.QUALITY_HIGH));
        mMediaRecorder.setOutputFile(outputPath);
    }

    public void startVideoRecord() throws IOException {
        if (mMediaRecorder != null) {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            mIsVideoIsRecording = true;
        }
    }

    public void stopVideoRecording() {
        if (mIsVideoIsRecording) {
            mIsVideoIsRecording = false;
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mCamera.lock();
            mMediaRecorder = null;
        }
    }

    public void setAudioOptional(boolean audioOptional) {
        mAudioOptional = true;
    }

    public FrameLayout getmFrameLayout() {
        return mFrameLayout;
    }

    public TextView getmTxtMessage() {
        return mTxtMessage;
    }

    public CameraSurfaceView getmCameraSurfaceView() {
        return mCameraSurfaceView;
    }

    public Camera getmCamera() {
        return mCamera;
    }

    public MediaRecorder getmMediaRecorder() {
        return mMediaRecorder;
    }

    public Context getmContext() {
        return mContext;
    }

    public Boolean ismAudioOptional() {
        return mAudioOptional;
    }

    public int getCameraId() {
        return mCameraId;
    }

    public void setCameraId(@IntRange(from = 0, to = 6) int mCameraId) {
        this.mCameraId = mCameraId;
    }
}
