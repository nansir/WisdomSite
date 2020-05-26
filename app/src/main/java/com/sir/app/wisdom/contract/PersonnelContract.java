package com.sir.app.wisdom.contract;

import androidx.lifecycle.MutableLiveData;

import com.sir.app.wisdom.model.entity.PersonnelRecordBean;
import com.sir.app.wisdom.model.entity.RecordPersonnelBean;
import com.sir.app.wisdom.model.entity.TurnUpBean;

import java.util.List;

/**
 * Created by zhuyinan on 2020/4/9.
 */
public interface PersonnelContract {

    void addPersonnel(String code, String nameCN, String nameEn, String photo);

    void editPersonnel(int id, String code, String nameCN, String nameEn, String photo);

    void searchPersonnel(String staffCode);

    void recordPersonnel(String key);

    MutableLiveData<List<PersonnelRecordBean>> getPersonnelRecord();

    MutableLiveData<List<TurnUpBean>> getTurnUp();

    MutableLiveData<List<RecordPersonnelBean>> getRecordPersonnel();

    void personnelRecords();

    void getPerson();

}
