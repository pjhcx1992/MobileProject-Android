package com.ck.mobileoperations.busniess.login.vo;

/**
 * Created by CK on 2017/12/14.
 * phoneNun,password,job,name
 * 注册发送到服务器的字段
 */

public class UserSend {
    private String phoneNun;
    private String password;

    private String name;

    public String getPhoneNun() {
        return phoneNun;
    }

    public void setPhoneNun(String phoneNun) {
        this.phoneNun = phoneNun;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
