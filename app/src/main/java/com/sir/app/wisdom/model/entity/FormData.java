package com.sir.app.wisdom.model.entity;

/**
 * Created by zhuyinan on 2020/4/20.
 */
public class FormData {

    private String type;

    private Object obj;

    public FormData(String type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
