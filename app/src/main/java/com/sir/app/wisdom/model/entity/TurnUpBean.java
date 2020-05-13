package com.sir.app.wisdom.model.entity;

/**
 * 各分包商到场情况
 * Created by zhuyinan on 2020/5/13.
 */
public class TurnUpBean {

    private String SubcontractorName;

    private int Countdali;

    private int Countanquan;

    private int Daodali;

    private int DaoSafe;

    public String getSubcontractorName() {
        return SubcontractorName == null ? "" : SubcontractorName;
    }

    public int getCountdali() {
        return Countdali;
    }

    public int getCountanquan() {
        return Countanquan;
    }

    public int getDaodali() {
        return Daodali;
    }

    public int getDaoSafe() {
        return DaoSafe;
    }
}
