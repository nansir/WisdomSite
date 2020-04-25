package com.sir.app.wisdom.model.entity;

/**
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
}
