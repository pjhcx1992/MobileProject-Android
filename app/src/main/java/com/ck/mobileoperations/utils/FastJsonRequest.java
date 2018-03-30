package com.ck.mobileoperations.utils;

/**
 * Created chen kai on 2018-3-9.
 */


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import java.io.UnsupportedEncodingException;

public class FastJsonRequest extends JsonRequest<com.alibaba.fastjson.JSONObject> {



    public FastJsonRequest(Integer method,String url, String requestBody, Listener<com.alibaba.fastjson.JSONObject> listener,
                             ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }


 /*   super(method, url, requestBody, listener,
    errorListener);*/

    @Override
    protected Response<com.alibaba.fastjson.JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(JSON.parseObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
