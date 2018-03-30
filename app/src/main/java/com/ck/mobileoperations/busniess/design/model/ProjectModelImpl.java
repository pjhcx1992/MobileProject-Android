package com.ck.mobileoperations.busniess.design.model;

import android.util.ArrayMap;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.ck.mobileoperations.utils.Constant;
import com.ck.mobileoperations.utils.FastJsonRequest;
import com.ck.mobileoperations.utils.OrgRequestResultListener;
import com.ck.mobileoperations.utils.RequestResultListener;
import com.ck.mobileoperations.utils.TestJsonRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenkai on 2018/3/12.
 */

public class ProjectModelImpl implements Projtctmodel {
    private static String TAG="ProjectModelImpl";
    private RequestQueue mQueue;
    public ProjectModelImpl(RequestQueue mQueue){
        this.mQueue=mQueue;
    }
    @Override
    public void volleyProjectCreate(String name, String states, String aproveResult, String userId, String addTime,
                                    String organizationId, final RequestResultListener listener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("name",name);//项目名
        params.put("states",states);//状态
        params.put("aproveResult",aproveResult);
        params.put("userId",userId);
        params.put("addTime",addTime);
        params.put("organizationId",organizationId);
        String url="http://"+ Constant.SERVER_URL+"/project/create";
        FastJsonRequest jsonRequest=new FastJsonRequest(Method.POST, url, JSON.toJSONString(params),
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
        jsonRequest.setTag("/project/create");
        jsonRequest.setShouldCache(false);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        mQueue.add(jsonRequest);
    }

    @Override
    public void volleyAddCable(String name, String stand, String type, String addTime, final RequestResultListener listener) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name",name);//项目名
        params.put("stand",stand);//状态
        params.put("type",type);
        params.put("addTime",addTime);
        String url="http://"+ Constant.SERVER_URL+"/addCable";
        FastJsonRequest jsonRequest=new FastJsonRequest(Method.POST, url, JSON.toJSONString(params),
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
        jsonRequest.setTag("/addCable");
        jsonRequest.setShouldCache(false);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        mQueue.add(jsonRequest);
    }

    @Override
    public void volleyAddMarks(String addType, String name, String stand, String latitude, String longitude
            , String projectId, String projectName, String addTime, String high, String style, String level, String orgId,
                               List<Map<String ,String>> conduit, List<Map<String ,String>> cableSegment, final RequestResultListener listener){


        Map<String,Object> map=new HashMap<>();
        if(addType=="WELL"){
            map.put("stand",stand.toString());
        }else if(addType=="POLE"){
            map.put("high",high.toString());
            map.put("style",style);
        }else if(addType=="LIGHTBOX"){
            map.put("stand",stand.toString());
            map.put("level",level.toString());
        }else if(addType=="JOINTBOX"){
            map.put("stand",stand.toString());
        }
        map.put("addType",addType.toString());
        map.put("name",name.toString());
        map.put("latitude",latitude.toString());
        map.put("longitude",longitude.toString());
        map.put("projectId",projectId.toString());
        map.put("projectName",projectName.toString());
        map.put("addTime",addTime.toString());
        map.put("orgId",orgId.toString());
        map.put("conduit",conduit);
        map.put("cableSegment",cableSegment);

        String url="http://"+ Constant.SERVER_URL+"/annotation/add";
        System.out.println("project request json"+JSON.toJSONString(map));



        /*org.json.JSONObject jsonObject= new org.json.JSONObject(map);
        JsonRequest<org.json.JSONObject> jsonRequest=new TestJsonRequest(Method.POST, url, jsonObject,
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(org.json.JSONObject response) {
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
        };*/


        JsonRequest jsonRequest=new FastJsonRequest(Method.POST, url,JSON.toJSONString(map),
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
        jsonRequest.setTag("/annotation/add");
        jsonRequest.setShouldCache(false);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        mQueue.add(jsonRequest);

    }

    @Override
    public void volleyAnnotation(String addType, String name, String stand, String latitude, String longitude, String projectId,
                                 String projectName, String addTime, String high, String style,
                                 String level, String orgId, final RequestResultListener listener) {

        Map<String,String> map=new HashMap<>();
        if(addType=="WELL"){
            map.put("stand",stand);
        }else if(addType=="POLE"){
            map.put("high",high);
            map.put("style",style);
        }else if(addType=="LIGHTBOX"){
            map.put("stand",stand);
            map.put("level",level);
        }else if(addType=="JOINTBOX"){
            map.put("stand",stand);
        }
        map.put("addType",addType);
        map.put("name",name);
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("projectId",projectId);
        map.put("projectName",projectName);
        map.put("addTime",addTime);
        map.put("orgId",orgId);

        String url="http://"+ Constant.SERVER_URL+"/annotation/add";
        JsonRequest jsonRequest=new FastJsonRequest(Method.POST, url,JSON.toJSONString(map),
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
        jsonRequest.setTag("/annotation/add");
        jsonRequest.setShouldCache(false);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        mQueue.add(jsonRequest);
    }

    @Override
    public void volleyAnnotationUpdate(String updateType, String id, String stand,
                                       String latitude, String longitude, String high, String style, String level,
                                       List<Map<String ,String>> addConduit, List<Map<String ,String>> addCableSegment,
                                       List<String> removeConduit, List<String> removeCableSegment,
                                        final RequestResultListener listener) {
        Map<String,Object> map=new HashMap<>();
        if(updateType=="WELL"){
            map.put("stand",stand.toString());
        }else if(updateType=="POLE"){
            map.put("high",high.toString());
            map.put("style",style.toString());
        }else if(updateType=="LIGHTBOX"){
            map.put("stand",stand.toString());
            map.put("level",level.toString());
        }else if(updateType=="JOINTBOX"){
            map.put("stand",stand.toString());
        }
        map.put("_id",id.toString());
        map.put("updateType",updateType.toString());
        map.put("latitude",latitude.toString());
        map.put("longitude",longitude.toString());
        map.put("removeConduit",removeConduit);
        map.put("removeCableSegment",removeCableSegment);
        map.put("addConduit",addConduit);
        map.put("addCableSegment",addCableSegment);
        String url="http://"+ Constant.SERVER_URL+"/annotation/update";
        FastJsonRequest jsonRequest=new FastJsonRequest(Request.Method.POST, url, JSON.toJSONString(map),
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
        jsonRequest.setTag("/annotation/update");
        jsonRequest.setShouldCache(false);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        mQueue.add(jsonRequest);
    }

    @Override
    public void volleyAnnotationDelete(String id, String type, List<String> conduitlistIds,List<String> cablesegmentlistIds,final RequestResultListener listener) {
        Map<String,Object> map=new HashMap<>();
        map.put("_id",id.toString());
        map.put("type",type.toString());
        map.put("conduitlistIds",conduitlistIds);
        map.put("cablesegmentlistIds",cablesegmentlistIds);

        String url="http://"+ Constant.SERVER_URL+"/annotation/delete";
        FastJsonRequest jsonRequest=new FastJsonRequest(Request.Method.POST, url, JSON.toJSONString(map),
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
        jsonRequest.setTag("/annotation/delete");
        jsonRequest.setShouldCache(false);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        mQueue.add(jsonRequest);
    }

    @Override
    public void volleyProjectStatus(String projectId, String states, final RequestResultListener listener) {
        Map<String,String> map=new HashMap<>();
        map.put("projectId",projectId);
        map.put("states",states);
        String url="http://"+ Constant.SERVER_URL+"/project/status";
      FastJsonRequest jsonRequest=new FastJsonRequest(Request.Method.POST, url, JSON.toJSONString(map),
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

        jsonRequest.setTag("/project/status");
        jsonRequest.setShouldCache(false);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        mQueue.add(jsonRequest);

    }

}
