package com.sir.app.wisdom.common;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.SplashActivity;
import com.sir.library.com.AppLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * 全部捕获异常之后重启程序
 * Created by zhuyinan on 2020/5/22.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler instance;
    //用来存储设备信息和异常信息
    private Context mContext;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                AppLogger.toast("很抱歉,程序出现异常,3秒后将重启.");
                //收集设备信息和错误日志上传到服务器
                reportError(deviceInfo(), e.getMessage());
                Looper.loop();
            }
        }.start();
        try {
            Thread.sleep(3000);
            restartApp();
        } catch (InterruptedException ex) {
            AppLogger.d(ex);
        }
    }

    /**
     * 日志上传到服务器
     *
     * @param device
     * @param content
     */
    public void reportError(String device, String content) {
        Map<String, String> map = new HashMap<>();
        map.put("device", device);
        map.put("content", content);
        map.put("rank", "3");
//        HttpUtils.getInstance(MyApplication.getContext())
//                .setBaseUrl(Constant.HTTP)
//                .getRetrofitClient()
//                .builder(ServerApi.class)
//                .reportLog(map)
//                .compose(ComposeTransformer.<String>FlowableMsg())
//                .subscribe();
    }

    /**
     * 收集设备参数信息
     */
    public String deviceInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("厂商", android.os.Build.BRAND);
        info.put("型号", android.os.Build.MODEL);
        info.put("系统版", android.os.Build.VERSION.RELEASE);
        return info.toString();
    }

    /**
     * 重启APP
     */
    private void restartApp() {
        Intent intent = new Intent(mContext, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
