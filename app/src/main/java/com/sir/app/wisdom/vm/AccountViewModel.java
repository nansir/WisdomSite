package com.sir.app.wisdom.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.common.AppKey;
import com.sir.app.wisdom.contract.AccountContract;
import com.sir.app.wisdom.model.AccountModel;
import com.sir.library.com.utils.SPUtils;
import com.sir.library.mvvm.base.BaseViewModel;

/**
 * Created by zhuyinan on 2020/4/8.
 */
public class AccountViewModel extends BaseViewModel<AccountModel> implements AccountContract {

    public AccountViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void singIn(String account, String password) {
        mRepository.singIn(account, password);
    }

    @Override
    public void singOut() {
        mRepository.singOut();
    }


    /**
     * 记录登录配置
     *
     * @param a
     * @param p
     * @param r
     */
    public void loginConfig(String a, String p, boolean r) {
        SPUtils.getInstance().put(AppKey.ACCOUNT, a);
        SPUtils.getInstance().put(AppKey.REMEMBER, r);
        if (r) {
            SPUtils.getInstance().put(AppKey.PASSWORD, p);
        } else {
            SPUtils.getInstance().remove(AppKey.REMEMBER);
        }
    }
}
