package com.sir.app.wisdom.model.entity;

/**
 * 分包商
 * Created by zhuyinan on 2020/4/17.
 */
public class SubcontractorBean {

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

    public String getCompanyName() {
        return CompanyName == null ? "" : CompanyName;
    }

    public String getSubcontractorName() {
        return SubcontractorName == null ? "" : SubcontractorName;
    }

    public int getSubcontractorID() {
        return SubcontractorID;
    }
}
