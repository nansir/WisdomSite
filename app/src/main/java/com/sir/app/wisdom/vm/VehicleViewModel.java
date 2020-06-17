package com.sir.app.wisdom.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.sir.app.wisdom.contract.VehicleContract;
import com.sir.app.wisdom.model.VehicleModel;
import com.sir.app.wisdom.model.entity.GateBean;
import com.sir.app.wisdom.model.entity.StatisticsBean;
import com.sir.app.wisdom.model.entity.SubcontractorBean;
import com.sir.app.wisdom.model.entity.VehicleInfoBean;
import com.sir.app.wisdom.model.entity.VehicleRecordsBean;
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
    public MutableLiveData<List<VehicleRecordsBean>> getAccessInfo() {
        return mRepository.getAccessInfo();
    }

    @Override
    public MutableLiveData<List<GateBean>> getGateInfo() {
        return mRepository.getGateInfo();
    }

    @Override
    public MutableLiveData<List<VehicleRecordsBean>> getVehicleRecords() {
        return mRepository.getVehicleRecords();
    }

    @Override
    public MutableLiveData<List<StatisticsBean>> getStatistics() {
        return mRepository.getStatistics();
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
    public void queryGateInfo() {
        mRepository.queryGateInfo();
    }

    @Override
    public void openGate(String id, int[] staff) {
        mRepository.openGate(id, staff);
    }

    @Override
    public void getAccessInfo(int number) {
        mRepository.getAccessInfo(number);
    }

    @Override
    public void totalVehicles(int type) {
        mRepository.totalVehicles(type);
    }

    @Override
    public void GetAllCarType() {
        mRepository.GetAllCarType();
    }

    @Override
    public void statistics(int carType, int dateType, int territoryID) {
        mRepository.statistics(carType, dateType, territoryID);
    }

    @Override
    public void vehicleRecords(String key) {
        mRepository.vehicleRecords(key);
    }
}
