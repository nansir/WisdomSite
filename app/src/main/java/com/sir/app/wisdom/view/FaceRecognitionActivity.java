package com.sir.app.wisdom.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.sir.app.wisdom.R;
import com.sir.app.wisdom.utils.FileUtils;
import com.sir.app.wisdom.vm.VehicleViewModel;
import com.sir.library.com.AppBaseActivity;
import com.sir.library.com.AppLogger;
import com.sir.library.mvvm.AppActivity;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 人脸识别活动
 * Created by zhuyinan on 2020/4/8.
 */
public class FaceRecognitionActivity extends AppActivity<VehicleViewModel> {

    final int REQUEST_CODE_CAMERA = 101;//打开相机

    private Uri mImageUri;

    @Override
    public int bindLayout() {
        return R.layout.activity_face_recognition;
    }

    @Override
    public void doBusiness() {
        setToolbarTitle(getTitle());
        setSwipeBackEnable(true);
    }

    @Override
    protected void dataObserver() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_camera) {
            AppLogger.toast("切換");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 判断权限并打开摄像机
     */
    private void openCameraPermission() {
        String[] perms = new String[]{Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            // 申请权限
            EasyPermissions.requestPermissions(this, "需要存储照相权限", REQUEST_CODE_CAMERA, perms);
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        File imageFile = FileUtils.createImageFile();//创建用来保存照片的文件
        if (imageFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                /*7.0 以上要通过FileProvider将File转化为Uri*/
                mImageUri = FileProvider.getUriForFile(this, "com.sir.app.wisdom.fileProvider", imageFile);
            } else {
                /*7.0 以下则直接使用Uri的fromFile方法将File转化为Uri*/
                mImageUri = Uri.fromFile(imageFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
            startActivityForResult(intent, REQUEST_CODE_CAMERA);//打开相机
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    openCameraPermission();
                } else {
                    //授权失败，简单提示用户
                    AppLogger.toast("授权失败,无法使用照相");
                }
                break;
            default:
                break;
        }
    }


}
