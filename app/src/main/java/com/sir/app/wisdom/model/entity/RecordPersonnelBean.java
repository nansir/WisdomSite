package com.sir.app.wisdom.model.entity;

import java.io.Serializable;

/**
 * 移动端人员上传记录
 * Created by zhuyinan on 2020/5/25.
 */
public class RecordPersonnelBean implements Serializable {

    private int StaffID;

    private String StaffCode;

    private String Photo;

    private String CN_FullName;

    private String EN_FullName;

    private String SubcontractorName;

    private String Time;

    public int getStaffID() {
        return StaffID;
    }

    public String getStaffCode() {
        return StaffCode == null ? "" : StaffCode;
    }

    public String getPhoto() {
        return Photo == null ? "" : Photo;
    }

    public String getCN_FullName() {
        return CN_FullName == null ? "" : CN_FullName;
    }

    public String getEN_FullName() {
        return EN_FullName == null ? "" : EN_FullName;
    }

    public String getSubcontractorName() {
        return SubcontractorName == null ? "" : SubcontractorName;
    }

    public String getTime() {
        return Time == null ? "" : Time;
    }
}
