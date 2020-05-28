package com.sir.app.wisdom.common;

import com.sir.app.wisdom.model.entity.RecordPersonnelBean;
import com.sir.app.wisdom.model.entity.VehicleRecordsBean;
import com.sir.app.wisdom.model.entity.GateBean;
import com.sir.app.wisdom.model.entity.LoginBean;
import com.sir.app.wisdom.model.entity.PersonnelRecordBean;
import com.sir.app.wisdom.model.entity.ResponseFaceBean;
import com.sir.app.wisdom.model.entity.StatisticsBean;
import com.sir.app.wisdom.model.entity.SubcontractorBean;
import com.sir.app.wisdom.model.entity.VehicleTypeBean;
import com.sir.app.wisdom.model.entity.TurnUpBean;
import com.sir.library.retrofit.response.HttpResponse;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
     * 登录(理应还有个获取token接口)
     */
    @POST("api/SysLogin")
    Call<HttpResponse<LoginBean>> token(@Body RequestBody body);


    /**
     * 添加/编辑人员资料
     *
     * @param body
     * @return
     */
    @POST("api/Staff")
    Flowable<HttpResponse> addPersonnel(@Body RequestBody body);


    /**
     * 人员上传记录
     *
     * @param body
     * @return
     */
    @POST("api/Staff")
    Flowable<HttpResponse<List<RecordPersonnelBean>>> recordPersonnel(@Body RequestBody body);

    /**
     * 搜索人员上传记录
     *
     * @param body
     * @return
     */
    @POST("api/Staff")
    Flowable<HttpResponse<RecordPersonnelBean>> searchPersonnel(@Body RequestBody body);



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
    Flowable<HttpResponse<ResponseFaceBean>> face(@Part MultipartBody.Part file);

    /**
     * 控制开闸的列表
     *
     * @return
     */
    @GET("api/Car_Gate/GetEntranceBrake/1")
    Flowable<HttpResponse<List<GateBean>>> gateInfo();

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
     *
     * @param number
     * @return
     */
    @GET("api/Car_Record/Gate/1/{number}")
    Flowable<HttpResponse<List<VehicleRecordsBean>>> getAccessInfo(@Path("number") int number);

    /**
     * 推送过来记录
     *
     * @param number
     * @return
     */
    @GET("api/Car_Record/Record/1/{number}")
    Flowable<HttpResponse<VehicleRecordsBean>> record(@Path("number") String number);


    /**
     * 本月进入车辆总数
     *
     * @return
     */
    @POST("api/GetCarSumByMonth?TerritoryID=1")
    Flowable<HttpResponse<Integer>> totalVehicles();


    /**
     * 统计
     *
     * @param carType
     * @param riqi
     * @param territoryID
     * @return
     */
    @GET("api/GetHHDDMM")
    Flowable<HttpResponse<List<StatisticsBean>>> statistics(@Query("cartype") int carType, @Query("riqi") int riqi, @Query("TerritoryID") int territoryID);


    /**
     * 所有的车辆类别
     *
     * @return
     */
    @GET("api/GetAllCarType?TerritoryID=1")
    Flowable<HttpResponse<List<VehicleTypeBean>>> GetAllCarType();


    /**
     * 车辆进出记录
     *
     * @return
     */
    @GET("api/GetCarJilu?TerritoryID=1")
    Flowable<HttpResponse<List<VehicleRecordsBean>>> vehicleRecords();

    /**
     * 人員统计
     *
     * @return
     */
    @GET("api/GetAppLastT?TerritoryID=1")
    Flowable<HttpResponse<List<PersonnelRecordBean>>> personnelRecords();

    /**
     * 各分包商到场情况
     *
     * @return
     */
    @GET("api/GetAppComPerson?TerritoryID=1")
    Flowable<HttpResponse<List<TurnUpBean>>> getTurnUp();
}
