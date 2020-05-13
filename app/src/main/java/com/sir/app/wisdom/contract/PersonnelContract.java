package com.sir.app.wisdom.contract;

import androidx.lifecycle.MutableLiveData;

import com.sir.app.wisdom.model.entity.PersonnelRecordBean;
import com.sir.app.wisdom.model.entity.TurnUpBean;

import java.util.List;

/**
 * Created by zhuyinan on 2020/4/9.
 */
public interface PersonnelContract {

    void addPersonnel(String code, String nameCN, String nameEn, String photo);

    MutableLiveData<List<PersonnelRecordBean>> getPersonnelRecord();

    MutableLiveData<List<TurnUpBean>> getTurnUp();

    void personnelRecords();

    void getPerson();

}
