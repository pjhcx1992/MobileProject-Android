package com.ck.mobileoperations.busniess.design.model;

import android.util.ArrayMap;

import com.ck.mobileoperations.utils.OrgRequestResultListener;
import com.ck.mobileoperations.utils.RequestResultListener;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenkai on 2018/3/12.
 */

public interface Projtctmodel {
    public void volleyProjectCreate(String name, String states, String aproveResult, String userId, String addTime,String organizationId,
                                    RequestResultListener listener);
    public void volleyAddCable(String name,String stand,String type,String addTime,RequestResultListener listener);

    public void volleyAddMarks(String addType, String name, String stand, String latitude, String longitude
            , String projectId, String projectName, String addTime, String high, String style, String level, String orgId
            , List<Map<String ,String>> conduit, List<Map<String ,String>> cableSegment, RequestResultListener listener);



    //添加标记点
    public void volleyAnnotation(String addType,String name,String stand,String latitude,String longitude
            ,String projectId,String projectName,String addTime, String high, String style, String level,String orgId,RequestResultListener listener);

    //更新标记点信息
    public void volleyAnnotationUpdate(String updateType,String id,String stand,String latitude,String longitude,
                                       String high,String style,String level,
                                       List<Map<String ,String>> addConduit, List<Map<String ,String>> addCableSegment,
                                       List<String> removeConduit, List<String> removeCableSegment
            ,RequestResultListener listener);
    //删除标记点
    public void volleyAnnotationDelete(String id,String type,List<String> conduitlistIds,List<String> cablesegmentlistIds
            ,RequestResultListener listener);

    //更新项目的状态
    public void volleyProjectStatus(String projectId,String states,RequestResultListener listener);






}
