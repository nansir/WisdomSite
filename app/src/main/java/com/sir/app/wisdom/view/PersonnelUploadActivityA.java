package com.sir.app.wisdom.view;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import com.bumptech.glide.Glide;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.common.AppKey;
import com.sir.app.wisdom.dialog.PhotoSelectDialog;
import com.sir.app.wisdom.dialog.SubmitResultsDialog;
import com.sir.app.wisdom.model.PersonnelModel;
import com.sir.app.wisdom.model.entity.RecordPersonnelBean;
import com.sir.app.wisdom.utils.BitmapUtil;
import com.sir.app.wisdom.vm.PersonnelViewModel;
import com.sir.library.com.AppLogger;
import com.sir.library.mvvm.AppActivity;
import com.sir.library.retrofit.event.ResState;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 人员信息上传
 * Created by zhuyinan on 2020/4/8.
 */
public class PersonnelUploadActivityA extends AppActivity<PersonnelViewModel> {

    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    final int REQUEST_CODE_ALBUM = 100;//打开相册
    final int CAMERA_REQUEST_CODE = 101;//打开相机
    @BindView(R.id.iv_personnel_photo)
    ImageView ivPhoto;
    SubmitResultsDialog resultsDialog;
    PhotoSelectDialog photoDialog;
    int staffID = 0;

    //用于保存拍照图片的uri
    private Uri mCameraUri;
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;
    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    // 通过uri加载图片
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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

            previewPhotos(bean.getPhoto());
        }
    }

    /**
     * 预览照片
     *
     * @param path
     */
    public void previewPhotos(String path) {
        AppLogger.d("预览照片:" + path);
        Glide.with(this)
                .load(path)
                .asBitmap()
                .centerCrop()
                .dontTransform()
                .placeholder(R.mipmap.ic_placeholder)//占位图片
                .into(ivPhoto);
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
     * 调起相机拍照
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                // 适配android 10
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(this, " com.sir.app.wisdom.fileProvider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }

            mCameraUri = photoUri;

            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
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

        if (TextUtils.isEmpty(code)) {
            mDialog.showError("未填寫員工編號");
            return;
        } else if (TextUtils.isEmpty(nameCN)) {
            mDialog.showError("未填寫中文姓名");
            return;
        } else if (staffID == 0 && mCameraImagePath == null) {
            mDialog.showError("未添加照片");
            return;
        }

        //开启线程压缩图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                String photo = "";

                if (isAndroidQ) {
                    // Android 10 使用图片uri加载
                    mCameraImagePath = getImageContentUri(PersonnelUploadActivityA.this, mCameraUri.getPath()).getPath();
                }

                String filePath = BitmapUtil.compressImage(mCameraImagePath);
                Bitmap mBitmap = BitmapUtil.getSmallBitmap(filePath);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                //将bitmap转成字节数组流.
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                photo = Base64.encodeToString(bao.toByteArray(), Base64.NO_WRAP);

                try {
                    bao.close();
                } catch (IOException e) {
                    e.printStackTrace();
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

    /**
     * 创建保存图片的文件
     */
    private File createImageFile() throws IOException {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    /**
     * 加载图片地址
     *
     * @param context
     * @param path
     * @return
     */
    public static Uri getImageContentUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            // 如果图片不在手机的共享图片数据库，就先把它插入。
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (isAndroidQ) {
                    // Android 10 使用图片uri加载
                    ivPhoto.setImageURI(mCameraUri);
                    mCameraImagePath = mCameraUri.getPath();
                } else {
                    // 使用图片路径加载
                    ivPhoto.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                }
            } else {
                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，有调起相机拍照。
                openCamera();
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
    }
}
