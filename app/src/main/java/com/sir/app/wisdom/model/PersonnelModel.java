package com.sir.app.wisdom.model;

import com.sir.app.wisdom.common.AppKey;
import com.sir.app.wisdom.common.Repository;
import com.sir.app.wisdom.contract.PersonnelContract;
import com.sir.library.com.utils.SPUtils;
import com.sir.library.retrofit.callback.RxSubscriber;
import com.sir.library.retrofit.exception.ResponseThrowable;
import com.sir.library.retrofit.transformer.ComposeTransformer;

/**
 * 人员管理模型
 * Created by zhuyinan on 2020/4/9.
 */
public class PersonnelModel extends Repository implements PersonnelContract {

    public static String EVENT_ADD_PERSONNEL = getEventKey();

    @Override
    public void addPersonnel(String code, String name, String photo) {
        String json = "{\"type\":\"add\",\"obj\":{\"Tid\":\"1\",\"StaffCode\":\"%s\",\"CNFullName\":\"%s\",\"Photo\":\"%s\"}}";
        postState(ON_LOADING, "正在提交..");
        addSubscribe(appServerApi.addPersonnel(createBody(String.format(json, code, name, photo)))
                .compose(ComposeTransformer.<String>FlowableMsg())
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void onSuccess(String bean) {
                        postData(EVENT_ADD_PERSONNEL, bean);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, getLoginMsg(ex.code));
                    }
                }));
    }

    public String getLoginMsg(int code) {
        switch (code) {
            case 1:
                return "解析失败";
            case 2:
                return "用户名密码不正确";
            case 3:
                return "账号锁定";
            case 4:
                return "登陆权限不足";
            default:
                return "未知错误";
        }
    }
}
