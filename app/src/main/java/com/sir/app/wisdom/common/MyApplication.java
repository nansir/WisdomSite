package com.sir.app.wisdom.common;

import com.pgyersdk.crash.PgyCrashManager;
import com.sir.app.wisdom.BuildConfig;
import com.sir.library.base.BaseApplication;
import com.sir.library.base.help.DensityHelp;
import com.sir.library.com.AppLogger;
import com.sir.library.com.utils.SPUtils;


/**
 * Created by zhuyinan on 2020/4/7.
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        DensityHelp.setDpHeight(780f);
        DensityHelp.setDpWidth(360f);
        SPUtils.init(this);
        AppLogger.setShow(BuildConfig.DEBUG);

        //注册Crash接口,上报 Crash 异常
        PgyCrashManager.register();
    }
}
