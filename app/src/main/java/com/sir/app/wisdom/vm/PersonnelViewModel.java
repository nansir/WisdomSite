package com.sir.app.wisdom.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.contract.PersonnelContract;
import com.sir.app.wisdom.model.PersonnelModel;
import com.sir.library.mvvm.base.BaseViewModel;

/**
 * Created by zhuyinan on 2020/4/9.
 */
public class PersonnelViewModel extends BaseViewModel<PersonnelModel> implements PersonnelContract {

    public PersonnelViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void addPersonnel(String code, String nameCN, String nameEn, String photo) {
        mRepository.addPersonnel(code, nameCN, nameEn, photo);
    }

    @Override
    public void personnelRecords() {
        mRepository.personnelRecords();
    }
}
