package com.sir.app.wisdom.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.sir.app.wisdom.R;
import com.sir.app.wisdom.dialog.PhotoSelectDialog;
import com.sir.app.wisdom.dialog.SubmitResultsDialog;
import com.sir.app.wisdom.model.PersonnelModel;
import com.sir.app.wisdom.utils.FileUtils;
import com.sir.app.wisdom.vm.PersonnelViewModel;
import com.sir.library.com.AppLogger;
import com.sir.library.mvvm.AppActivity;
import com.sir.library.retrofit.event.ResState;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 人员信息上传
 * Created by zhuyinan on 2020/4/8.
 */
public class PersonnelUploadActivity extends AppActivity<PersonnelViewModel> {

    final int REQUEST_CODE_ALBUM = 100;//打开相册
    final int REQUEST_CODE_CAMERA = 101;//打开相机

    @BindView(R.id.iv_personnel_photo)
    ImageView ivInfoPhoto;
    SubmitResultsDialog resultsDialog;
    PhotoSelectDialog photoDialog;
    private Uri mImageUri;
    private Bitmap bitmap;

    @Override
    public int bindLayout() {
        return R.layout.activity_personnel_upload;
    }

    @Override
    public void doBusiness() {
        setToolbarTitle(getTitle());
        setSwipeBackEnable(true);
    }

    @Override
    protected void dataObserver() {
        resultsDialog = new SubmitResultsDialog(getActivity());
        photoDialog = new PhotoSelectDialog(getActivity());
    }

    @Override
    protected void notification(ResState state) {
        mDialog.dismiss();
        if (state.getCode() == PersonnelModel.ON_SUCCESS) {
            resultsDialog.show();
            resultsDialog.setSuccess();
            resultsDialog.setBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            //三秒后自动关闭
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);
        } else if (state.getCode() == PersonnelModel.ON_FAILURE) {
            resultsDialog.show();
            resultsDialog.setFailure(state.getMsg());
            resultsDialog.setBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resultsDialog.dismiss();
                }
            });
        } else {
            super.notification(state);
        }
    }

    @OnClick({R.id.iv_personnel_photo, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_personnel_photo:
                photoDialog.show();
                photoDialog.set(v -> {
                    photoDialog.dismiss();
                    if (v.getId() == R.id.tv_photo_camera) {
                        openCamera();
                    } else if (v.getId() == R.id.tv_photo_select) {
                        openAlbum();
                    }
                });
                break;
            case R.id.btn_submit:
                submitData();
                break;
        }
    }

    /**
     * 判断权限并打开摄像机
     */
    private void openCamera() {
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

    /**
     * 调用相册
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    /**
     * 提交數據
     */
    private void submitData() {
        String nameCN = mViewHelper.getEditVal(R.id.et_personnel_name_cn);
        String nameEN = mViewHelper.getEditVal(R.id.et_personnel_name_en);
        String code = mViewHelper.getEditVal(R.id.et_personnel_number);
        if (TextUtils.isEmpty(nameCN)) {
            mDialog.showError("未填寫中文姓名");
            return;
        } else if (TextUtils.isEmpty(nameEN)) {
            mDialog.showError("未填寫英文姓名");
            return;
        } else if (TextUtils.isEmpty(nameCN)) {
            mDialog.showError("未填寫員工編號");
            return;
        } else if (bitmap == null) {
            mDialog.showError("未添加照片");
            return;
        }
        String photo = FileUtils.bitmapToString(bitmap);
        mViewModel.addPersonnel(code, nameCN, photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                /* 将Bitmap设定到ImageView */
                ivInfoPhoto.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_ALBUM && resultCode == RESULT_OK) {
            if (data != null) {
                // 照片的原始资源地址
                Uri uri = data.getData();
                String path = uri.getPath();
                ContentResolver cr = getContentResolver();
                try {
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    /* 将Bitmap设定到ImageView */
                    ivInfoPhoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }
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
                    openCamera();
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
