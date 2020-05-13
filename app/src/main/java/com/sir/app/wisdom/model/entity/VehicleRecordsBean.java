package com.sir.app.wisdom.model.entity;

/**
 * 车辆记录
 * Created by zhuyinan on 2020/5/13.
 */
public class VehicleRecordsBean {
    private int SubcontractorID;

    private String Creator;

    private int Creator_ID;

    private String Creator_Time;

    private String Editor;

    private int Editor_ID;

    private String Editor_Time;

    private int CompanyID;

    private String CompanyName;

    private String SubcontractorName;

    private String Introduce;

    private int IsDesignatedSubcontractor;

    private int IsPrefabrication;

    private int DeleteMark;

    private int Enabled;

    public int getSubcontractorID() {
        return SubcontractorID;
    }

    public String getCreator() {
        return Creator == null ? "" : Creator;
    }

    public int getCreator_ID() {
        return Creator_ID;
    }

    public String getCreator_Time() {
        return Creator_Time == null ? "" : Creator_Time;
    }

    public String getEditor() {
        return Editor == null ? "" : Editor;
    }

    public int getEditor_ID() {
        return Editor_ID;
    }

    public String getEditor_Time() {
        return Editor_Time == null ? "" : Editor_Time;
    }

    public int getCompanyID() {
        return CompanyID;
    }

    public String getCompanyName() {
        return CompanyName == null ? "" : CompanyName;
    }

    public String getSubcontractorName() {
        return SubcontractorName == null ? "" : SubcontractorName;
    }

    public String getIntroduce() {
        return Introduce == null ? "" : Introduce;
    }

    public int getIsDesignatedSubcontractor() {
        return IsDesignatedSubcontractor;
    }

    public int getIsPrefabrication() {
        return IsPrefabrication;
    }

    public int getDeleteMark() {
        return DeleteMark;
    }

    public int getEnabled() {
        return Enabled;
    }
}
