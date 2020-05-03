package com.sir.app.wisdom.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.sir.app.wisdom.contract.VehicleContract;
import com.sir.app.wisdom.model.VehicleModel;
import com.sir.app.wisdom.model.entity.AccessInfoBean;
import com.sir.app.wisdom.model.entity.GateBean;
import com.sir.app.wisdom.model.entity.SubcontractorBean;
import com.sir.app.wisdom.model.entity.VehicleInfoBean;
import com.sir.app.wisdom.model.entity.VehicleTypeBean;
import com.sir.library.mvvm.base.BaseViewModel;

import java.io.File;
import java.util.List;

/**
 * Created by zhuyinan on 2020/4/9.
 */
public class VehicleViewModel extends BaseViewModel<VehicleModel> implements VehicleContract {

    public VehicleViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public MutableLiveData<List<SubcontractorBean>> getSubcontractor() {
        return mRepository.getSubcontractor();
    }

    @Override
    public MutableLiveData<List<VehicleTypeBean>> getVehicleType() {
        return mRepository.getVehicleType();
    }

    @Override
    public MutableLiveData<List<AccessInfoBean>> getAccessInfo() {
        return mRepository.getAccessInfo();
    }

    @Override
    public MutableLiveData<List<GateBean>> getGateInfo() {
        return mRepository.getGateInfo();
    }

    @Override
    public void face(File imagePath) {
        mRepository.face(imagePath);
    }

    @Override
    public void subcontractor(String value) {
        mRepository.subcontractor(value);
    }

    @Override
    public void vehicleType() {
        mRepository.vehicleType();
    }

    @Override
    public void vehicleAction(VehicleInfoBean bean) {
        mRepository.vehicleAction(bean);
    }

    @Override
    public void gateInfo() {
        mRepository.gateInfo();
    }

    @Override
    public void openGateB(String recordId, int[] staff) {
        mRepository.openGateB(recordId, staff);
    }

    @Override
    public void getAccessInfo(int number) {
        mRepository.getAccessInfo(number);
    }
}
