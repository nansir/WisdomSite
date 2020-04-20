package com.sir.app.wisdom.model;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.sir.app.wisdom.common.Repository;
import com.sir.app.wisdom.contract.VehicleContract;
import com.sir.app.wisdom.model.entity.AccessInfoBean;
import com.sir.app.wisdom.model.entity.FormData;
import com.sir.app.wisdom.model.entity.SubcontractorBean;
import com.sir.app.wisdom.model.entity.VehicleInfoBean;
import com.sir.app.wisdom.model.entity.VehicleTypeBean;
import com.sir.library.com.AppLogger;
import com.sir.library.retrofit.callback.RxSubscriber;
import com.sir.library.retrofit.exception.ResponseThrowable;
import com.sir.library.retrofit.transformer.ComposeTransformer;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;

/**
 * 车辆管理模型
 * Created by zhuyinan on 2020/4/9.
 */
public class VehicleModel extends Repository implements VehicleContract {

    public static String EVENT_SUCCESS = getEventKey();
    public static String EVENT_FAILURE = getEventKey();

    private MutableLiveData<List<SubcontractorBean>> subcontractor;
    private MutableLiveData<List<VehicleTypeBean>> vehicleType;
    private MutableLiveData<AccessInfoBean> accessInfo;

    private String getSubconMsg(int code) {
        return "";
    }

    @Override
    public void vehicleAction(VehicleInfoBean bean) {
        FormData data = new FormData("AddOrEdit", bean);
        postState(ON_LOADING, "正在添加..");
        AppLogger.d(new Gson().toJson(data));
        addSubscribe(appServerApi.carAction(createBody(new Gson().toJson(data)))
                .compose(ComposeTransformer.<String>FlowableMsg())
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void onSuccess(String bean) {
                        postState(ON_SUCCESS, bean);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, ex.message);
                    }
                }));
    }

    @Override
    public void openGateB(String number, int[] staff) {

    }

    @Override
    public void getAccessInfo(String number) {
        addSubscribe(appServerApi.getAccessInfo(number)
                .compose(ComposeTransformer.<AccessInfoBean>Flowable())
                .subscribeWith(new RxSubscriber<AccessInfoBean>() {
                    @Override
                    protected void onSuccess(AccessInfoBean bean) {

                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, ex.message);
                    }
                }));
    }

    @Override
    public MutableLiveData<List<SubcontractorBean>> getSubcontractor() {
        if (subcontractor == null) {
            subcontractor = new MutableLiveData<>();
        }
        return subcontractor;
    }

    @Override
    public MutableLiveData<List<VehicleTypeBean>> getVehicleType() {
        if (vehicleType == null) {
            vehicleType = new MutableLiveData<>();
        }
        return vehicleType;
    }

    @Override
    public MutableLiveData<AccessInfoBean> getAccessInfo() {
        if (accessInfo == null) {
            accessInfo = new MutableLiveData<>();
        }
        return accessInfo;
    }

    @Override
    public void face(File file) {
        // okhttp请求头中不能含有中文Unexpected char 0x65b0 at 34 in Content-Disposition value: form-data; name="file";
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), createBody(file));
        addSubscribe(appServerApi.face(filePart)
                .compose(ComposeTransformer.<String>FlowableMsg())
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void onSuccess(String bean) {
                        postData(EVENT_SUCCESS, bean);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postData(EVENT_FAILURE, ex.message);
                    }
                }));
    }

    @Override
    public void subcontractor(String value) {
        String json = "{\"type\":\"GetList\",\"obj\":{\"TerritoryID\":\"1\",\"Key\":\"%s\"}}";
        addSubscribe(appServerApi.subcontractor(createBody(String.format(json, value)))
                .compose(ComposeTransformer.<List<SubcontractorBean>>Flowable())
                .subscribeWith(new RxSubscriber<List<SubcontractorBean>>() {
                    @Override
                    protected void onSuccess(List<SubcontractorBean> list) {
                        getSubcontractor().postValue(list);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, ex.message);
                    }
                }));

    }

    @Override
    public void vehicleType() {
        addSubscribe(appServerApi.carType()
                .compose(ComposeTransformer.<List<VehicleTypeBean>>Flowable())
                .subscribeWith(new RxSubscriber<List<VehicleTypeBean>>() {
                    @Override
                    protected void onSuccess(List<VehicleTypeBean> list) {
                        getVehicleType().postValue(list);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, ex.message);
                    }
                }));
    }
}
