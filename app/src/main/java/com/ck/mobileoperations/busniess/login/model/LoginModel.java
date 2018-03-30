package com.ck.mobileoperations.busniess.login.model;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.ck.mobileoperations.entity.User;
import com.ck.mobileoperations.utils.RequestResultListener;

import java.util.concurrent.ExecutionException;

/**
 * Created by chenkai on 2018/3/9.
 */

public interface LoginModel {
    public void volleyRegister(String phonenum, String passWord, String username,String organizationName,
                               RequestResultListener listener);
    public void volleyLogin (String passWord, String username, RequestResultListener listener);


    public void addMembers(String phonenum,String username,String organizationId,String department,String role,
                           RequestResultListener listener);

    public void  getBaseInfo(String userId, RequestResultListener listener) ;
}
