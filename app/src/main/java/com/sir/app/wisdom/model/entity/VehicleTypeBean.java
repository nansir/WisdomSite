package com.sir.app.wisdom.model.entity;

/**
 * Created by zhuyinan on 2020/4/19.
 */
public class VehicleTypeBean {

    private int Car_Type_ID;

    private int TerritoryID;

    private String Car_Type_Name;

    private int DeleteMark;

    private String Edit;

    private int Edit_id;

    private String Edit_Time;

    public int getCar_Type_ID() {
        return Car_Type_ID;
    }

    public int getTerritoryID() {
        return TerritoryID;
    }

    public String getCar_Type_Name() {
        return Car_Type_Name == null ? "" : Car_Type_Name;
    }

    public int getDeleteMark() {
        return DeleteMark;
    }

    public String getEdit() {
        return Edit == null ? "" : Edit;
    }

    public int getEdit_id() {
        return Edit_id;
    }

    public String getEdit_Time() {
        return Edit_Time == null ? "" : Edit_Time;
    }
}
