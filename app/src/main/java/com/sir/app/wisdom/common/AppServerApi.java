package com.sir.app.wisdom.common;

import com.sir.library.retrofit.response.HttpResponse;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by zhuyinan on 2020/1/14.
 */
public interface AppServerApi {

    @POST("api/SysLogin")
    Flowable<HttpResponse<String>> singing(@Body RequestBody body);

    @POST("api/Staff")
    Flowable<HttpResponse> addPersonnel(@Body RequestBody body);

    @Multipart
    @POST("api/Search?TerritoryID=1")
    Flowable<HttpResponse> face(@Part MultipartBody.Part file);
}
