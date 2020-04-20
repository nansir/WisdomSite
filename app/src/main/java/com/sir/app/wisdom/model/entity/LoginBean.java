package com.sir.app.wisdom.model.entity;

/**
 * 登录信息
 * Created by zhuyinan on 2020/4/8.
 */
public class LoginBean {

    private String AccessToken;

    private String RefreshTokenExpired;

    private String RefreshToken;

    public String getAccessToken() {
        return AccessToken == null ? "" : AccessToken;
    }

    public String getRefreshTokenExpired() {
        return RefreshTokenExpired == null ? "" : RefreshTokenExpired;
    }

    public String getRefreshToken() {
        return RefreshToken == null ? "" : RefreshToken;
    }
}
