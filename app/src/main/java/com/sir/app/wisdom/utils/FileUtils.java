package com.sir.app.wisdom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Base64;

import androidx.core.os.EnvironmentCompat;

import com.sir.app.wisdom.common.AppConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhuyinan on 2020/4/9.
 */
public class FileUtils {

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    public static File createImageFile() {
        File file = new File(AppConstant.ROOT_DIR);
        try {
            //创建文件夹
            if (!file.exists()) {
                //按照指定的路径创建文件夹
                file.mkdirs();
            }

            file = File.createTempFile("NanSir", ".jpg", file);

            //删除文件
            if (file.exists()) {
                file.delete();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 位图到字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        //将bitmap转成字节数组流.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        int options = 90;
        while (bao.toByteArray().length / 1024 > 120) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            try {
                bao.reset(); // 重置bao
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, bao);// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();

                Matrix matrix = new Matrix();
                matrix.setScale(0.5f, 0.5f);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap = Bitmap.createScaledBitmap(bitmap, 480, 800, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            }
        }
        return Base64.encodeToString(bao.toByteArray(), Base64.NO_WRAP);
    }


    /**
     * 保存方法
     */
    public static File saveBitmap(Context context, Bitmap bm) {
        File file = createImageFile(context);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 10, out);
            out.flush();
            out.close();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建保存图片的文件
     */
    public static File createImageFile(Context mContext) {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }
}
