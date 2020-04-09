package com.sir.app.wisdom.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    /**
     * 位图到字符串
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        String base64 = "";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //将bitmap转成字节数组流.
        boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        if (compress) {
            base64 = Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP);
        }
        return base64;
    }
}
