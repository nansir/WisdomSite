package com.sir.app.wisdom.common;

import com.sir.library.base.BaseApplication;
import com.sir.library.base.help.DensityHelp;

/**
 * Created by zhuyinan on 2020/4/7.
 */
public class MyApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        DensityHelp.setDpHeight(780f);
        DensityHelp.setDpWidth(360f);
    }
}
