package com.sir.app.wisdom.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhuyinan on 2020/1/14.
 */
public class AppConstant {
    public static final String HTTP = "http://120.78.211.126:81";


    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "wisdom";
}
