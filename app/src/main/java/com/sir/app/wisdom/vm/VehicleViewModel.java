package com.sir.app.wisdom.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.contract.VehicleContract;
import com.sir.app.wisdom.model.VehicleModel;
import com.sir.library.mvvm.base.BaseViewModel;

import java.io.File;

/**
 * Created by zhuyinan on 2020/4/9.
 */
public class VehicleViewModel extends BaseViewModel<VehicleModel> implements VehicleContract {

    public VehicleViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void face(File imagePath) {
        mRepository.face(imagePath);
    }
}
