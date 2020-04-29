package com.sir.app.wisdom.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.camera2.CameraDevice;
import android.os.Handler;
import android.os.Message;
import android.util.Size;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.sir.app.wisdom.R;
import com.sir.app.wisdom.common.camera.CameraListener;
import com.sir.app.wisdom.common.camera.CameraManager;
import com.sir.app.wisdom.dialog.ScanResultsDialog;
import com.sir.app.wisdom.model.VehicleModel;
import com.sir.app.wisdom.model.entity.GateBean;
import com.sir.app.wisdom.model.entity.ResponseFaceBean;
import com.sir.app.wisdom.utils.FileUtils;
import com.sir.app.wisdom.utils.ImageUtil;
import com.sir.app.wisdom.view.weight.Circle;
import com.sir.app.wisdom.vm.VehicleViewModel;
import com.sir.library.com.AppLogger;
import com.sir.library.mvvm.AppActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 人脸识别活动
 * Created by zhuyinan on 2020/4/8.
 */
public class FaceRecognitionActivity extends AppActivity<VehicleViewModel> implements CameraListener, DialogInterface.OnDismissListener, View.OnClickListener {

    // 处理的间隔帧
    final int PROCESS_INTERVAL = 50;
    final int REQUEST_CODE_CAMERA = 1001;

    @BindView(R.id.tv_camera_preview)
    TextureView tvCameraPreview;
    @BindView(R.id.circle)
    Circle circle;


    CameraManager cameraManager;

    // 图像帧数据，全局变量避免反复创建，降低gc频率
    private byte[] nv21;
    // 当前获取的帧数
    private int currentIndex = 0;
    // 线程池
    private ExecutorService executorService;
    // 显示的旋转角度
    private int displayOrientation;
    // 是否手动镜像预览
    private boolean isMirrorPreview;
    // 实际打开的cameraId
    private String openedCameraId;
    // 正在进行识别(默认不识别)
    private boolean ongoing = true;
    // 啟動標誌
    boolean flag = false;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                ongoing = false; //开始识别
                flag = true; //已經啟動
                mViewHelper.setTextVal(R.id.tv_scan_content, "開始識別...");
            } else if (msg.what == 1) {
                mViewHelper.setTextVal(R.id.tv_toolbar_title, "人臉識別");
                mViewHelper.setTextVal(R.id.tv_scan_content, "正在識別...");
            }
            return false;
        }
    });
    private ScanResultsDialog resultsDialog;

    @Override
    public int bindLayout() {
        return R.layout.activity_face_recognition;
    }

    @Override
    public void doBusiness() {
        setSwipeBackEnable(true);
        initPermission();
        executorService = Executors.newSingleThreadExecutor();
        resultsDialog = new ScanResultsDialog(getActivity());
        mDialog.setDelay(3000);
        mDialog.setOnDismissListener(this::onDismiss);
        resultsDialog.setOnDismissListener(this::onDismiss);
        circle.start(); //开始动画

        //启动后三秒开始识别
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    /**
     * 初始化权限
     */
    private void initPermission() {
        String[] perms = new String[]{Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            initCamera();
        } else {
            EasyPermissions.requestPermissions(this, "需要照相权限", REQUEST_CODE_CAMERA, perms);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        //提示框消失·继续识别
        ongoing = false;
        circle.start(); //开始动画
        mViewHelper.setVisibility(R.id.tv_scan_content, true);
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        cameraManager = new CameraManager.Builder()
                .cameraListener(this)
                .maxPreviewSize(new Point(1920, 1080))
                .minPreviewSize(new Point(1280, 720))
                .specificCameraId(CameraManager.CAMERA_ID_BACK)// 默认打开的CAMERA
                .context(getApplicationContext())
                .previewOn(tvCameraPreview)
                .previewViewSize(new Point(tvCameraPreview.getWidth(), tvCameraPreview.getHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();

        cameraManager.start();
    }

    @Override
    protected boolean isUseFullScreenMode() {
        return true;
    }

    @OnClick({R.id.ibtn_return, R.id.ibtn_camera_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_return:
                finish();
                break;
            case R.id.ibtn_camera_switch:
                //切换摄像头
                cameraManager.switchCamera();
                break;
        }
    }

    @Override
    public void onCameraOpened(CameraDevice device, String cameraId, Size previewSize, int displayOrientation, boolean isMirror) {
        this.displayOrientation = displayOrientation;
        this.isMirrorPreview = isMirror;
        this.openedCameraId = cameraId;
    }

    @Override
    public void onPreview(byte[] y, byte[] u, byte[] v, Size previewSize, int stride) {
        if (currentIndex++ % PROCESS_INTERVAL == 0 && !ongoing) {
            ongoing = true;
            mHandler.sendEmptyMessage(1);//开始识别
            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    if (nv21 == null) {
                        nv21 = new byte[stride * previewSize.getHeight() * 3 / 2];
                    }

                    // 回传数据是YUV422
                    if (y.length / u.length == 2) {
                        ImageUtil.yuv422ToYuv420sp(y, u, v, nv21, stride, previewSize.getHeight());
                    } else if (y.length / u.length == 4) { // 回传数据是YUV420
                        ImageUtil.yuv420ToYuv420sp(y, u, v, nv21, stride, previewSize.getHeight());
                    }
                    YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, stride, previewSize.getHeight(), null);
                    // ByteArrayOutputStream的close中其实没做任何操作，可不执行
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    // 由于U和V一般都有缺损，因此若使用方式，可能会有个宽度为1像素的绿边
                    yuvImage.compressToJpeg(new Rect(0, 0, previewSize.getWidth(), previewSize.getHeight()), 100, byteArrayOutputStream);
                    byte[] jpgBytes = byteArrayOutputStream.toByteArray();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Matrix matrix = new Matrix();
                    // 预览相对于原数据可能有旋转
                    matrix.postRotate(CameraManager.CAMERA_ID_BACK.equals(openedCameraId) ? displayOrientation : -displayOrientation);
                    // 对于前置数据，镜像处理；若手动设置镜像预览，则镜像处理；若都有，则不需要镜像处理
                    if (CameraManager.CAMERA_ID_FRONT.equals(openedCameraId) ^ isMirrorPreview) {
                        matrix.postScale(-1, 1);
                    }
                    // 原始预览数据生成的bitmap
                    final Bitmap originalBitmap = BitmapFactory.decodeByteArray(jpgBytes, 0, jpgBytes.length, options);
                    // 预览画面相同的bitmap
                    final Bitmap previewBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, false);

                    File file = FileUtils.saveBitmap(getActivity(), previewBitmap);

                    if (file != null) {
                        mViewModel.face(file);
                    }
                }
            });
        }
    }

    @Override
    public void onCameraClosed() {

    }

    @Override
    public void onCameraError(Exception ex) {
        AppLogger.toast(ex.getMessage());
    }

    @Override
    protected void onPause() {
        if (cameraManager != null) {
            cameraManager.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraManager != null) {
            cameraManager.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                //grantResults数组存储的申请的返回结果，
                //PERMISSION_GRANTED 表示申请成功
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCamera();
                } else {
                    //授权失败，简单提示用户
                    AppLogger.toast("授权失败,无法使用照相");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
        if (cameraManager != null) {
            cameraManager.release();
        }
        super.onDestroy();
    }

    @Override
    protected void dataObserver() {
        //人脸识别成果
        mViewModel.subscribe(VehicleModel.EVENT_SUCCESS, ResponseFaceBean.class)
                .observe(this, new Observer<ResponseFaceBean>() {
                    @Override
                    public void onChanged(ResponseFaceBean bean) {
                        if (flag) {
                            ongoing = true; //停止识别
                            circle.pause(); //停止动画
                            mViewHelper.setVisibility(R.id.tv_scan_content, false);
                            resultsDialog.show();
                            resultsDialog.loadData(bean);
                            resultsDialog.setOnClick(FaceRecognitionActivity.this::onClick);

                            mViewHelper.setTextVal(R.id.tv_toolbar_title, "識別成功");
                        }
                    }
                });
        //人脸识别失败
        mViewModel.subscribe(VehicleModel.EVENT_FAILURE, String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (flag) {
                            ongoing = true; //停止识别
                            circle.pause(); //停止动画
                            mDialog.showWarning(s);

                            mViewHelper.setTextVal(R.id.tv_toolbar_title, "識別失敗");
                        }
                    }
                });

        //开闸成果
        mViewModel.subscribe(VehicleModel.EVENT_SUCCESS_GATE, GateBean.class)
                .observe(this, new Observer<GateBean>() {
                    @Override
                    public void onChanged(GateBean bean) {
                        mDialog.showSuccess("开启：" + bean.getName());
                    }
                });
    }

    @Override
    public void onClick(View view) {
        //开闸
        resultsDialog.dismiss();
        switch (view.getId()) {
            case R.id.btn_gate_a:
                mViewModel.openGateA(1);
                break;
            case R.id.btn_gate_b:
                mViewModel.openGateA(2);
                break;
            case R.id.btn_gate_c:
                mViewModel.openGateA(3);
                break;
            case R.id.btn_gate_d:
                mViewModel.openGateA(4);
                break;
        }
    }
}
