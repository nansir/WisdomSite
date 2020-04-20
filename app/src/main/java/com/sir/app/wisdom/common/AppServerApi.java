package com.sir.app.wisdom.common;

import com.sir.app.wisdom.model.entity.AccessInfoBean;
import com.sir.app.wisdom.model.entity.LoginBean;
import com.sir.app.wisdom.model.entity.SubcontractorBean;
import com.sir.app.wisdom.model.entity.VehicleTypeBean;
import com.sir.library.retrofit.response.HttpResponse;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by zhuyinan on 2020/1/14.
 */
public interface AppServerApi {

    /**
     * 登录
     *
     * @param body
     * @return
     */
    @POST("api/SysLogin")
    Flowable<HttpResponse<LoginBean>> singing(@Body RequestBody body);

    /**
     * 添加人员资料
     *
     * @param body
     * @return
     */
    @POST("api/Staff")
    Flowable<HttpResponse> addPersonnel(@Body RequestBody body);

    /**
     * 分判商列表
     *
     * @param body
     * @return
     */
    @POST("api/Subcontractor")
    Flowable<HttpResponse<List<SubcontractorBean>>> subcontractor(@Body RequestBody body);

    /**
     * 车辆种类
     *
     * @return
     */
    @GET("api/CarAction/CarTypeList/1")
    Flowable<HttpResponse<List<VehicleTypeBean>>> carType();

    /**
     * 新增车辆/修改车辆信息
     *
     * @param body
     * @return
     */
    @POST("api/CarAction/")
    Flowable<HttpResponse> carAction(@Body RequestBody body);

    /**
     * 人脸识别
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("api/Search?TerritoryID=1")
    Flowable<HttpResponse> face(@Part MultipartBody.Part file);

    /**
     * 控制开闸的列表
     *
     * @param body
     * @return
     */
    @GET("api/Car_Gate/GetEntranceBrake/1")
    Flowable<HttpResponse> openGateA(@Body RequestBody body);

    /**
     * 人脸识别成功后，点击开闸
     *
     * @param body
     * @return
     */
    @POST("api/Car_Record")
    Flowable<HttpResponse> openGateB(@Body RequestBody body);

    /**
     * 闸口获取车辆信息
     * @param number
     * @return
     */
    @GET("api/Car_Record/Gate/1/{number}")
    Flowable<HttpResponse<AccessInfoBean>> getAccessInfo(@Path("number") String number);

}
