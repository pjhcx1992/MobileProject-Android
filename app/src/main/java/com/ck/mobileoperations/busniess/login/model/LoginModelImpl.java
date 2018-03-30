package com.ck.mobileoperations.busniess.login.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ck.mobileoperations.entity.User;
import com.ck.mobileoperations.utils.Constant;
import com.ck.mobileoperations.utils.FastJsonRequest;
import com.ck.mobileoperations.utils.RequestResultListener;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by chenkai on 2018/3/9.
 */

public class LoginModelImpl implements LoginModel{
    private static String TAG="LoginModelImpl";
    private RequestQueue mQueue;
    public LoginModelImpl(RequestQueue mQueue){
        this.mQueue=mQueue;
    }

    //注册
    @Override
    public void volleyRegister(String phonenum, String passWord, String username, String organizationName,
                               final RequestResultListener listener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNum",phonenum);
        params.put("passWord",passWord);
        params.put("userName",username);
        params.put("organizationName",organizationName);
        String url="http://"+Constant.SERVER_URL+"/user/register";
        FastJsonRequest jsonRequest=new FastJsonRequest(Request.Method.POST, url, JSON.toJSONString(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        jsonRequest.setTag("/user/register");
        jsonRequest.setShouldCache(false);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        mQueue.add(jsonRequest);

    }

    //登录
    @Override
    public void volleyLogin(final String phonenum, final String passWord, final RequestResultListener listener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNum",phonenum);
        params.put("passWord",passWord);

        String url="http://"+Constant.SERVER_URL+"/user/login";

        FastJsonRequest jsonRequest=new FastJsonRequest(Request.Method.POST, url, JSON.toJSONString(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("post request success");
                        Log.i(TAG,"login_response message"+response.toJSONString());
                        listener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"login_error message"+error.toString());
                        listener.onError(error.toString());
                    }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        jsonRequest.setTag("/user/login");
        jsonRequest.setShouldCache(false);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        mQueue.add(jsonRequest);
    }
    //添加成员
    @Override
    public void addMembers(String phonenum, String username, String organizationId, String department,
                           String role, final RequestResultListener listener) {

       /*List<Map<String, String>> strings=new ArrayList<>();*/

        Map<String, String> params = new HashMap<String, String>();
        params.put("phoneNum",phonenum);
        params.put("userName",username);
        params.put("organizationId",organizationId);//公司organizationId
        params.put("department",department);//部门
        params.put("role",role);//角色



        String url="http://"+Constant.SERVER_URL+"/project/addMembers";
        FastJsonRequest jsonRequest=new FastJsonRequest(Request.Method.POST, url, JSON.toJSONString(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        jsonRequest.setTag("/project/addMembers");
        jsonRequest.setShouldCache(false);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        mQueue.add(jsonRequest);
    }

    @Override
    public void getBaseInfo(String userId, final RequestResultListener listener) {


        Map<String, String> params = new HashMap<String, String>();
        params.put("userId",userId);
        String url="http://"+Constant.SERVER_URL+"/updateData";
        FastJsonRequest request=new FastJsonRequest(Request.Method.POST, url, JSON.toJSONString(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        request.setTag("/updateDatas");
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        mQueue.add(request);

    }

}
