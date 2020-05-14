package com.sir.app.wisdom.model.entity;

/**
 * 车辆出入信息
 * Created by zhuyinan on 2020/4/20.
 */
public class VehicleRecordsBean {

    private String Record_ID;

    private String CarNo;

    private String Subcontractor;

    private String CaptureTime;

    private String Car_Gate_ID;

    private String BrakeType;

    private String Address;

    private String Headstock;

    private String CarRoof;

    private String RecordDate;

    private String SubcontractorName;

    public String getRecord_ID() {
        return Record_ID == null ? "" : Record_ID;
    }

    public String getCarNo() {
        return CarNo == null ? "" : CarNo;
    }

    public String getSubcontractor() {
        return Subcontractor == null ? "" : Subcontractor;
    }

    public String getCaptureTime() {
        return CaptureTime == null ? "" : CaptureTime;
    }

    public String getCar_Gate_ID() {
        return Car_Gate_ID;
    }

    public String getBrakeType() {
        return BrakeType;
    }

    public String getAddress() {
        return Address == null ? "" : Address;
    }

    public String getHeadstock() {
        return Headstock == null ? "" : Headstock;
    }

    public String getCarRoof() {
        return CarRoof == null ? "" : CarRoof;
    }

    public String getRecordDate() {
        return RecordDate == null ? "" : RecordDate;
    }

    public String getSubcontractorName() {
        return SubcontractorName == null ? "" : SubcontractorName;
    }
}
