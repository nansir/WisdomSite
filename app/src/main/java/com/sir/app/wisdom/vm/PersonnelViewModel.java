package com.sir.app.wisdom.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.sir.app.wisdom.contract.PersonnelContract;
import com.sir.app.wisdom.model.PersonnelModel;
import com.sir.app.wisdom.model.entity.PersonnelRecordBean;
import com.sir.app.wisdom.model.entity.RecordPersonnelBean;
import com.sir.app.wisdom.model.entity.TurnUpBean;
import com.sir.library.mvvm.base.BaseViewModel;

import java.util.List;

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
    public void editPersonnel(int id, String code, String nameCN, String nameEn, String photo) {
        mRepository.editPersonnel(id, code, nameCN, nameEn, photo);
    }

    @Override
    public void searchPersonnel(String staffCode) {
        mRepository.searchPersonnel(staffCode);
    }

    @Override
    public void recordPersonnel(String key) {
        mRepository.recordPersonnel(key);
    }

    @Override
    public MutableLiveData<List<PersonnelRecordBean>> getPersonnelRecord() {
        return mRepository.getPersonnelRecord();
    }

    @Override
    public MutableLiveData<List<TurnUpBean>> getTurnUp() {
        return mRepository.getTurnUp();
    }

    @Override
    public MutableLiveData<List<RecordPersonnelBean>> getRecordPersonnel() {
        return mRepository.getRecordPersonnel();
    }

    @Override
    public void personnelRecords() {
        mRepository.personnelRecords();
    }

    @Override
    public void getPerson() {
        mRepository.getPerson();
    }
}
