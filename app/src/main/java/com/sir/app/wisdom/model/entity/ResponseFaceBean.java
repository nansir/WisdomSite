package com.sir.app.wisdom.model.entity;

/**
 * 人脸识别响应
 * Created by zhuyinan on 2020/4/25.
 */
public class ResponseFaceBean {

    private int StaffID;

    private String CN_FullName;

    private String Subcontractor;

    private String Team;

    private String Photo;

    public int getStaffID() {
        return StaffID;
    }

    public String getCN_FullName() {
        return CN_FullName == null ? "" : CN_FullName;
    }

    public String getSubcontractor() {
        return Subcontractor == null ? "" : Subcontractor;
    }

    public String getTeam() {
        return Team == null ? "" : Team;
    }

    public String getPhoto() {
        return Photo == null ? "" : Photo;
    }
}
