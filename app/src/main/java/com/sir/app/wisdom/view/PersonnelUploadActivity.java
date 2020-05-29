package com.sir.app.wisdom.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.common.AppKey;
import com.sir.app.wisdom.dialog.PhotoSelectDialog;
import com.sir.app.wisdom.dialog.SubmitResultsDialog;
import com.sir.app.wisdom.model.PersonnelModel;
import com.sir.app.wisdom.model.entity.RecordPersonnelBean;
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
    int staffID = 0;
    private Uri mImageUri;
    private Bitmap bitmap;
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;
    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    @Override
    public int bindLayout() {
        return R.layout.activity_personnel_upload;
    }

    @Override
    public void doBusiness() {
        setToolbarTitle(getTitle());
        setSwipeBackEnable(true);

        RecordPersonnelBean bean = (RecordPersonnelBean) getIntent().getSerializableExtra(AppKey.ValA);

        //编辑模式
        if (bean != null) {
            staffID = bean.getStaffID();
            setToolbarTitle("编辑人員信息");
            mViewHelper.setEditVal(R.id.et_personnel_name_cn, bean.getCN_FullName());
            mViewHelper.setEditVal(R.id.et_personnel_name_en, bean.getEN_FullName());
            mViewHelper.setEditVal(R.id.et_personnel_number, bean.getStaffCode());

            Glide.with(this)
                    .load(bean.getPhoto())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_placeholder)//占位图片
                    .into(ivInfoPhoto);

        }
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
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            if (isAndroidQ) {
                // 适配android 10
                mImageUri = createImageUri();
            } else {
                photoFile = FileUtils.createImageFile(this);//创建用来保存照片的文件
//                if (photoFile != null) {
//                    mCameraImagePath = photoFile.getAbsolutePath();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
//                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
//                    } else {
//                        photoUri = Uri.fromFile(photoFile);
//                    }
//                }
                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        /*7.0 以上要通过FileProvider将File转化为Uri*/
                        mImageUri = FileProvider.getUriForFile(this, "com.sir.app.wisdom.fileProvider", photoFile);
                    } else {
                        /*7.0 以下则直接使用Uri的fromFile方法将File转化为Uri*/
                        mImageUri = Uri.fromFile(photoFile);
                    }
                }
            }
            if (mImageUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, REQUEST_CODE_CAMERA);
            }
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

        mDialog.showLoading("正在提交..");

        String nameCN = mViewHelper.getEditVal(R.id.et_personnel_name_cn);
        String nameEN = mViewHelper.getEditVal(R.id.et_personnel_name_en);
        String code = mViewHelper.getEditVal(R.id.et_personnel_number);

        if (TextUtils.isEmpty(nameCN)) {
            mDialog.showError("未填寫員工編號");
            return;
        } else if (TextUtils.isEmpty(nameCN)) {
            mDialog.showError("未填寫中文姓名");
            return;
        } else if (staffID == 0 && bitmap == null) {
            mDialog.showError("未添加照片");
            return;
        }

        //开启线程压缩图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                String photo = "";
                if (bitmap != null) {
                    photo = FileUtils.bitmapToString(bitmap);
                }
                if (staffID == 0) {
                    mViewModel.addPersonnel(code, nameCN, nameEN, photo);
                } else {
                    mViewModel.editPersonnel(staffID, code, nameCN, nameEN, photo);
                }
            }
        }).start();
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ALBUM && resultCode == RESULT_OK) { //相册返回
            if (data != null) {
                // 照片的原始资源地址
                Uri uri = data.getData();
                //String path = uri.getPath();
                ContentResolver cr = getContentResolver();
                try {
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    /* 将Bitmap设定到ImageView */
                    ivInfoPhoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }
            }
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {//打开相机返回
            try {
                // 是否是Android 10以上手机
                if (isAndroidQ) {
                    ContentResolver cr = getContentResolver();
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(mImageUri));
                } else {
                    bitmap = BitmapFactory.decodeFile(mCameraImagePath);
                }
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
            /* 将Bitmap设定到ImageView */
            ivInfoPhoto.setImageBitmap(bitmap);
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
