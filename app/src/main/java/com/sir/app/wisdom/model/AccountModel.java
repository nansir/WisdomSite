package com.sir.app.wisdom.model;

import com.sir.app.wisdom.common.AppKey;
import com.sir.app.wisdom.common.MyApplication;
import com.sir.app.wisdom.common.Repository;
import com.sir.app.wisdom.contract.AccountContract;
import com.sir.app.wisdom.model.entity.LoginBean;
import com.sir.library.com.utils.SPUtils;
import com.sir.library.retrofit.HttpUtils;
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
        postState(ON_LOADING, "登錄..");
        addSubscribe(appServerApi.singing(createBody(String.format(json, account, password)))
                .compose(ComposeTransformer.<LoginBean>Flowable())
                .subscribeWith(new RxSubscriber<LoginBean>() {
                    @Override
                    protected void onSuccess(LoginBean bean) {
                        HttpUtils.getInstance(MyApplication.getContext()).setAuthToken(bean.getAccessToken());
                        HttpUtils.getInstance(MyApplication.getContext()).setToken(bean.getRefreshToken());
                        SPUtils.getInstance().put(AppKey.AUTH_TOKEN, bean.getAccessToken());
                        SPUtils.getInstance().put(AppKey.TOKEN, bean.getRefreshToken());
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
                return "解析失敗";
            case 2:
                return "用戶名或密碼不正確";
            case 3:
                return "賬號鎖定";
            case 4:
                return "登錄權限不足";
            default:
                return "未知錯誤";
        }
    }
}
