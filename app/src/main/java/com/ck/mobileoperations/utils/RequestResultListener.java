package com.ck.mobileoperations.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by chenkai on 2018/3/9.
 */

public interface RequestResultListener {
    public void onSuccess(JSONObject s);
    public void onError(String s);
}
