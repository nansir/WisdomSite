package com.sir.app.wisdom.model;

import com.sir.app.wisdom.common.Repository;
import com.sir.app.wisdom.contract.AccountContract;
import com.sir.library.retrofit.callback.RxSubscriber;
import com.sir.library.retrofit.exception.ResponseThrowable;
import com.sir.library.retrofit.transformer.ComposeTransformer;

/**
 * Created by zhuyinan on 2020/4/8.
 */
public class AccountModel extends Repository implements AccountContract {

    public static String EVENT_LOGIN = getEventKey();

    @Override
    public void singIn(String account, String password) {
        String json = "{\"type\":\"login\",\"obj\":{\"LoginName\":\"%s\",\"Password\":\"%s\"}}";
        postState(ON_LOADING, "登录..");
        addSubscribe(appServerApi.singing(createBody(String.format(json, account, password)))
                .compose(ComposeTransformer.<String>Flowable())
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void onSuccess(String bean) {
                        postData(EVENT_LOGIN, bean);
                    }

                    @Override
                    protected void onFailure(ResponseThrowable ex) {
                        postState(ON_FAILURE, getLoginMsg(ex.code));
                    }
                }));
    }

    @Override
    public void singOut() {

    }

    public String getLoginMsg(int code) {
        switch (code) {
            case 0:
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
