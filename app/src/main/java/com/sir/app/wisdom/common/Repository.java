package com.sir.app.wisdom.common;

import com.sir.library.mvvm.base.BaseRepository;
import com.sir.library.retrofit.HttpUtils;

/**
 * 资料库
 * Created by zhuyinan on 2019/9/6.
 */
public class Repository extends BaseRepository {

    public AppServerApi appServerApi;

    public Repository() {
        this.appServerApi = HttpUtils.getInstance(MyApplication.getContext())
                .addCookie()
                .setBaseUrl(AppConstant.HTTP)
                .setLoadMemoryCache(false)
                .setLoadDiskCache(true)
                .getRetrofitClient()
                .builder(AppServerApi.class);
    }
}
