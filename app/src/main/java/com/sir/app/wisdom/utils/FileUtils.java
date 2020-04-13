package com.sir.app.wisdom.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import com.sir.app.wisdom.common.AppConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
        String base64 = "";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //将bitmap转成字节数组流.
        boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        if (compress) {
            base64 = Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP);
        }
        return base64;
    }


    /**
     * 保存方法
     */
    public static File saveBitmap(Bitmap bm) {
        File file = new File(AppConstant.ROOT_DIR, "NanSir.jpg");
        try {

            //创建文件夹
            if (!file.exists()) {
                //按照指定的路径创建文件夹
                file.mkdirs();
            }

            //删除文件
            if (file.exists()) {
                file.delete();
            }

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
}
