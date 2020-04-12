package com.sir.app.wisdom.model;

import com.sir.app.wisdom.common.Repository;
import com.sir.app.wisdom.contract.VehicleContract;
import com.sir.library.retrofit.callback.RxSubscriber;
import com.sir.library.retrofit.exception.ResponseThrowable;
import com.sir.library.retrofit.transformer.ComposeTransformer;

import java.io.File;

/**
 * 车辆管理模型
 * Created by zhuyinan on 2020/4/9.
 */
public class VehicleModel extends Repository implements VehicleContract {

    @Override
    public void face(File imagePath) {
        addSubscribe(appServerApi.face(createBody(imagePath))
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
}
