package com.sir.app.wisdom.model;

import com.sir.app.wisdom.common.Repository;
import com.sir.app.wisdom.contract.VehicleContract;
import com.sir.library.retrofit.callback.RxSubscriber;
import com.sir.library.retrofit.exception.ResponseThrowable;
import com.sir.library.retrofit.transformer.ComposeTransformer;

import java.io.File;

import okhttp3.MultipartBody;

/**
 * 车辆管理模型
 * Created by zhuyinan on 2020/4/9.
 */
public class VehicleModel extends Repository implements VehicleContract {

    public static String EVENT_FACE_SUCCESS = getEventKey();
    public static String EVENT_FACE_FAILURE = getEventKey();


    @Override
    public void face(File file) {
        // okhttp请求头中不能含有中文Unexpected char 0x65b0 at 34 in Content-Disposition value: form-data; name="file";
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), createBody(file));
        addSubscribe(appServerApi.face(filePart)
                .compose(ComposeTransformer.<String>FlowableMsg())
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void onSuccess(String bean) {
                        postData(EVENT_FACE_SUCCESS, bean);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postData(EVENT_FACE_FAILURE, ex.message);
                    }

                }));
    }
}
