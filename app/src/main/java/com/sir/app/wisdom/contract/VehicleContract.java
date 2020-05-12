package com.sir.app.wisdom.contract;

import androidx.lifecycle.MutableLiveData;

import com.sir.app.wisdom.model.entity.AccessInfoBean;
import com.sir.app.wisdom.model.entity.GateBean;
import com.sir.app.wisdom.model.entity.SubcontractorBean;
import com.sir.app.wisdom.model.entity.VehicleInfoBean;
import com.sir.app.wisdom.model.entity.VehicleTypeBean;

import java.io.File;
import java.util.List;

/**
 * Created by zhuyinan on 2020/4/9.
 */
public interface VehicleContract {

    MutableLiveData<List<SubcontractorBean>> getSubcontractor();

    MutableLiveData<List<VehicleTypeBean>> getVehicleType();

    MutableLiveData<List<AccessInfoBean>> getAccessInfo();

    MutableLiveData<List<GateBean>> getGateInfo();

    void face(File imagePath);

    void subcontractor(String value);

    void vehicleType();

    void vehicleAction(VehicleInfoBean bean);

    void queryGateInfo();

    void openGate(String recordId, int[] staff);

    void getAccessInfo(int number);

    void totalVehicles(int number);

    void vehicleType(int number);

    void statistics(int carType, int dateType, int territoryID);

    void vehicleRecords(int number);
}
