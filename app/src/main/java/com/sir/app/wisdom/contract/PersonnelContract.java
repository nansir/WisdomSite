package com.sir.app.wisdom.contract;

/**
 * Created by zhuyinan on 2020/4/9.
 */
public interface PersonnelContract {
    void addPersonnel(String code, String nameCN, String nameEn, String photo);

    void personnelRecords();
}
