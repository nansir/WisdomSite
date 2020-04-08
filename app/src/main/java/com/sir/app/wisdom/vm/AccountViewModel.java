package com.sir.app.wisdom.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.sir.app.wisdom.contract.AccountContract;
import com.sir.app.wisdom.model.AccountModel;
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
}
