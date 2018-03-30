package com.ck.mobileoperations.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by chenkai on 2018/3/9.
 */

public interface OrgRequestResultListener {
    public void onSuccess(org.json.JSONObject s);
    public void onError(String s);
}
