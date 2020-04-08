package com.sir.app.wisdom.common;

import com.sir.library.retrofit.response.HttpResponse;

import io.reactivex.Flowable;
import retrofit2.http.POST;

/**
 * Created by zhuyinan on 2020/1/14.
 */
public interface AppServerApi {

    @POST("api/SysLogin")
    Flowable<HttpResponse> singing(String account,String password);
}
