package com.sir.app.wisdom.common;

import com.sir.library.com.AppLogger;
import com.sir.library.com.utils.SPUtils;
import com.sir.library.mvvm.base.BaseRepository;
import com.sir.library.retrofit.HttpUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 资料库
 * Created by zhuyinan on 2019/9/6.
 */
public class Repository extends BaseRepository {

    public AppServerApi appServerApi;

    public Repository() {
        this.appServerApi = HttpUtils.getInstance(MyApplication.getContext())
                .addCookie()
                .setAuthToken(SPUtils.getInstance().get(AppKey.AUTH_TOKEN))
                .setToken(SPUtils.getInstance().get(AppKey.TOKEN))
                .setBaseUrl(AppConstant.HTTP)
                .setLoadMemoryCache(false)
                .setLoadDiskCache(true)
                .getRetrofitClient()
                .builder(AppServerApi.class);
    }

    protected RequestBody createBody(String json) {
        AppLogger.d(json);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

    protected RequestBody createBody(File file) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), file);
    }
}
