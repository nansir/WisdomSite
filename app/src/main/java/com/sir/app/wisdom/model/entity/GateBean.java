package com.sir.app.wisdom.model.entity;

/**
 * 闸口信息
 * Created by zhuyinan on 2020/4/21.
 */
public class GateBean {

    private int Car_Gate_ID;

    private int TerritoryID;

    private int Gate_Type;

    private String Name;

    private String Address;

    public String getName() {
        return Name == null ? "" : Name;
    }

    public String getAddress() {
        return Address == null ? "" : Address;
    }

    public int getTerritoryID() {
        return TerritoryID;
    }

    public int getCar_Gate_ID() {
        return Car_Gate_ID;
    }
}
