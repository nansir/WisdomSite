package com.sir.app.wisdom.common.camera;

import android.hardware.camera2.CameraDevice;
import android.util.Size;

public interface CameraListener {

    /**
     * 当打开时执行
     *
     * @param device             相机实例
     * @param cameraId           相机ID
     * @param displayOrientation 相机预览旋转角度
     * @param isMirror           是否镜像显示
     */
    void onCameraOpened(CameraDevice device, String cameraId, Size previewSize, int displayOrientation, boolean isMirror);

    /**
     * 预览数据回调
     *
     * @param y           预览数据，Y分量
     * @param u           预览数据，U分量
     * @param v           预览数据，V分量
     * @param previewSize 预览尺寸
     * @param stride      步长
     */
    void onPreview(byte[] y, byte[] u, byte[] v, Size previewSize, int stride);

    /**
     * 当相机关闭时执行
     */
    void onCameraClosed();

    /**
     * 当出现异常时执行
     *
     * @param ex 相机相关异常
     */
    void onCameraError(Exception ex);

}
