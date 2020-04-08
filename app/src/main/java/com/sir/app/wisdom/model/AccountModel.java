package com.sir.app.wisdom.model;

import com.sir.app.wisdom.common.Repository;
import com.sir.app.wisdom.contract.AccountContract;

/**
 * Created by zhuyinan on 2020/4/8.
 */
public class AccountModel extends Repository implements AccountContract {

    @Override
    public void singIn(String account, String password) {
        String json = "{\"type\":\"login\",\"obj\":{\"LoginName\":\"app\",\"Password\":\"123456\"}}";


    }

    @Override
    public void singOut() {

    }
}
