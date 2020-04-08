package com.sir.app.wisdom.contract;

/**
 * Created by zhuyinan on 2020/4/8.
 */
public interface AccountContract {
    void singIn(String account, String password);

    void singOut();
}
