package com.sir.app.wisdom.model.entity;

/**
 * 車輛提交信息
 * Created by zhuyinan on 2020/4/20.
 */
public class VehicleInfoBean {
    private String Territory_Car_ID; //车辆编号：不带这个参数为新增，带这个参数为修改
    private int TerritoryID = 1; //地盘编号固定值1
    private String Car_No;
    private int CardType;
    private int SubcontractorID;
    private String ValidUntil; //有效期至
    private String Deadweight;
    private String NumberNuclearCarriers;
    private String Size;

    public void setTerritory_Car_ID(String territory_Car_ID) {
        Territory_Car_ID = territory_Car_ID;
    }

    public void setTerritoryID(int territoryID) {
        TerritoryID = territoryID;
    }

    public void setCar_No(String car_No) {
        Car_No = car_No;
    }

    public void setCardType(int cardType) {
        CardType = cardType;
    }

    public void setSubcontractorID(int subcontractorID) {
        SubcontractorID = subcontractorID;
    }

    public void setValidUntil(String validUntil) {
        ValidUntil = validUntil;
    }

    public void setDeadWeight(String weight) {
        Deadweight = weight;
    }

    public void setNumberNuclearCarriers(String numberNuclearCarriers) {
        NumberNuclearCarriers = numberNuclearCarriers;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getValidUntil() {
        return ValidUntil == null ? "" : ValidUntil;
    }
}
