package com.sir.app.wisdom.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Vibrator;

import androidx.annotation.NonNull;

import com.pgyersdk.update.PgyUpdateManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by zhuyinan on 2019/7/12.
 */
public class AppUtils {

    /**
     * 检查更新
     */
    public static void checkUpdate(@NonNull Activity context, int requestCode) {
        String[] perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,};
        if (EasyPermissions.hasPermissions(context, perms)) {
            /** APP版本检测 **/
            new PgyUpdateManager.Builder()
                    .setForced(false)                //设置是否强制更新
                    .setUserCanRetry(false)         //失败后是否提示重新下载
                    .setDeleteHistroyApk(true)     // 检查更新前是否删除本地历史 Apk
                    .register();
        } else {
            // 申请权限
            EasyPermissions.requestPermissions(context, "检查应用更新需要存储权限", requestCode, perms);
        }
    }


    public static String getJSON(Context context, String fileName) {
        AssetManager am = context.getAssets();
        try {
            // 从assets文件夹里获取fileName数据流解析
            InputStream inputStream = am.open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getLocalInetAddress() {
        try {
            for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements(); ) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                for (Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                     inetAddresses.hasMoreElements(); ) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    //过滤Loopback address, Link-local address
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取MAC地址
     *
     * @return
     */
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                //nif.getInetAddresses();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }


    /**
     * 格式时间
     *
     * @param mss
     * @return
     */
    public static String formatTime(int mss) {
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;

        String m = String.valueOf(minutes);
        String s = String.valueOf(seconds);

        if (m.length() == 1) {
            m = "0" + m;
        }

        if (s.length() == 1) {
            s = "0" + s;
        }

        return m + ":" + s;
    }

    /**
     * 获取WiFi名称
     *
     * @param context
     * @return
     */
    public static String getWifiSSID(Context context) {
        WifiManager manager = ((WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE));
        assert manager != null;
        WifiInfo wifiInfo = manager.getConnectionInfo();
        String SSID = wifiInfo.getSSID();
        int networkId = wifiInfo.getNetworkId();
        List<WifiConfiguration> configuredNetworks = manager.getConfiguredNetworks();
        for (WifiConfiguration wifiConfiguration : configuredNetworks) {
            if (wifiConfiguration.networkId == networkId) {
                SSID = wifiConfiguration.SSID;
                break;
            }
        }
        return SSID.replace("\"", "").replace("\"", "");
    }

    /**
     * 让手机振动milliseconds毫秒
     */
    public static void vibrate(Context context) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        if (vib.hasVibrator()) {  //判断手机硬件是否有振动器
            vib.vibrate(160);
        }
    }
}

