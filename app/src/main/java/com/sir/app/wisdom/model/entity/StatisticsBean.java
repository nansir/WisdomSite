package com.sir.app.wisdom.model.entity;

/**
 * Created by zhuyinan on 2020/5/14.
 */
public class StatisticsBean {
    private int count;

    private int Hour;

    private String date;

    public int getCount() {
        return count;
    }

    public int getHour() {
        return Hour;
    }

    public String getDate() {
        return date == null ? "" : date;
    }
}
