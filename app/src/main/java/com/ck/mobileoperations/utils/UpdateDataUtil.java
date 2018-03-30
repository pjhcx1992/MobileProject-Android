package com.ck.mobileoperations.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ck.mobileoperations.entity.CableResource;
import com.ck.mobileoperations.entity.GJiaoX;
import com.ck.mobileoperations.entity.GuaQiang;
import com.ck.mobileoperations.entity.JieTouHe;
import com.ck.mobileoperations.entity.Organization;
import com.ck.mobileoperations.entity.PeopleWell;
import com.ck.mobileoperations.entity.Pipeline;
import com.ck.mobileoperations.entity.PointSet;
import com.ck.mobileoperations.entity.Pole;
import com.ck.mobileoperations.entity.ProName;
import com.ck.mobileoperations.entity.Profile;
import com.ck.mobileoperations.entity.User;
import com.ck.mobileoperations.entity.YinShangDian;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by chenkai on 2018/3/14.
 * 更新数据库
 */

public class UpdateDataUtil {

    public static void deletePointSet(){
        List<PointSet> list=DataSupport.findAll(PointSet.class);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                PointSet pointSet=list.get(i);
                if(pointSet.isSaved()){
                    pointSet.delete();
                }
            }
        }

        List<Pipeline> pipelineList=DataSupport.findAll(Pipeline.class);
        if(pipelineList.size()>0){
            for(int i=0;i<pipelineList.size();i++){
                Pipeline pipeline=pipelineList.get(i);
                if(pipeline.isSaved()){
                    pipeline.delete();
                }
            }
        }

        List<CableResource> cableResourceList=DataSupport.findAll(CableResource.class);
        if(cableResourceList.size()>0){
            for(int i=0;i<cableResourceList.size();i++){
                CableResource cableResource=cableResourceList.get(i);
                if(cableResource.isSaved()){
                    cableResource.delete();
                }
            }
        }

    }

    public static void updateUser(JSONObject jsonObject){
        List<User> userList=DataSupport.findAll(User.class);
        if(userList.size()>0){
            for(int a=0;a<userList.size();a++){
                User user=userList.get(a);
                if(user.isSaved()){
                    user.delete();
                }
            }
        }

        User user=new User();
        user.setPhoneNum(jsonObject.getString("phoneNum"));
        user.setPassword(jsonObject.getString("passWord"));
        user.setUserName(jsonObject.getString("userName"));
        user.setUserId(jsonObject.getString("_id"));
        user.save();
        Constant.user=user;
    }
    public static void updateAllOrganization(JSONArray allOrganization){

        List<Organization> organizationList=DataSupport.findAll(Organization.class);
        for(int j=0;j<organizationList.size();j++){
            Organization organization=organizationList.get(j);
            if(organization.isSaved()){
                organization.delete();
            }
        }
        for(int i=0;i<allOrganization.size();i++){
            JSONObject jsonObject= (JSONObject) allOrganization.get(i);
            Organization organization=new Organization();
            organization.setName(jsonObject.getString("organizationName"));
            organization.setOrgId(jsonObject.getString("_id"));
            organization.save();

        }

    }

    public static void updateAllProfile(JSONArray allProfile){
        List<Profile> profileList=DataSupport.findAll(Profile.class);
        if(profileList.size()>0){
            for(int a=0;a<profileList.size();a++){
                Profile profile=profileList.get(a);
                if(profile.isSaved()){
                    profile.delete();
                }
            }
        }

        for(int i=0;i<allProfile.size();i++){
            JSONObject jsonObject=(JSONObject) allProfile.get(i);
            Profile profile=new Profile();
            profile.setDepartment(jsonObject.getString("department"));
            profile.setOrganizationId(jsonObject.getString("organizationId"));
            profile.setProfileId(jsonObject.getString("_id"));
            profile.setRole(jsonObject.getString("role"));
            profile.setSecretary(jsonObject.getBoolean("isSecretary"));
            profile.setUserId(jsonObject.getString("userId"));
            profile.save();
        }



    }
    public static  void updateAllProject(JSONArray allprojrct){
        List<ProName>proNameList= DataSupport.findAll(ProName.class);
        if(proNameList.size()>0){
            for(int b=0;b<proNameList.size();b++){
                ProName proName=proNameList.get(b);
                if(proName.isSaved()){
                    proName.delete();
                }
            }
        }
        for(int i=0;i<allprojrct.size();i++){
            JSONArray jsonArray=(JSONArray) allprojrct.get(i);
            for(int a=0;a<jsonArray.size();a++){
                JSONObject jsonObject=(JSONObject) jsonArray.get(a);
                ProName proName =new ProName();
                proName.setOrgId(jsonObject.getString("organizationId"));
                proName.setProjectId(jsonObject.getString("_id"));
                proName.setProname(jsonObject.getString("name"));
                proName.setCreateMember(jsonObject.getString("createMember"));
                proName.setStatus(jsonObject.getString("states"));
                proName.setAddtime(jsonObject.getString("addTime"));
                proName.setCheckstatus(jsonObject.getString("aproveResult"));
                proName.save();
            }
        }
    }
    public static  void updateAllWell(JSONArray allWell){

        List<PeopleWell> peopleWellList=DataSupport.findAll(PeopleWell.class);
        if(peopleWellList.size()>0){
            for(int a=0;a<peopleWellList.size();a++){
                PeopleWell peopleWell=peopleWellList.get(a);
                if(peopleWell.isSaved()){
                    peopleWell.delete();
                }
            }
        }

        for(int i=0;i<allWell.size();i++){
            JSONObject jsonArray=(JSONObject) allWell.get(i);
            PeopleWell peopleWell=new PeopleWell();
            peopleWell.setAddtime(jsonArray.getString("addTime"));
            peopleWell.setLatitude(jsonArray.getString("latitude"));
            peopleWell.setLongitude(jsonArray.getString("longitude"));
            peopleWell.setWellname(jsonArray.getString("name"));
            peopleWell.setOrgId(jsonArray.getString("orgId"));
            peopleWell.setProjectId(jsonArray.getString("projectId"));
            peopleWell.setSpecifications(jsonArray.getString("stand"));
            peopleWell.setTotheproject(jsonArray.getString("projectName"));
            peopleWell.setAnnotationId(jsonArray.getString("_id"));
            peopleWell.save();

            PointSet pointSet=new PointSet();
            pointSet.setMarkname(jsonArray.getString("name"));
            pointSet.setLatitude(jsonArray.getString("latitude"));
            pointSet.setLongitude(jsonArray.getString("longitude"));
            pointSet.setProjrctId(jsonArray.getString("projectId"));
            pointSet.setOrgId(jsonArray.getString("orgId"));
            pointSet.setPointType("WELL");
            pointSet.setMarkId(jsonArray.getString("_id"));
            pointSet.save();


        }

    }
    public static  void updateAllPole(JSONArray allPole){
        List<Pole> poleList=DataSupport.findAll(Pole.class);
        if(poleList.size()>0){
            for(int a=0;a<poleList.size();a++){
                Pole pole=poleList.get(a);
                if(pole.isSaved()){
                    pole.delete();
                }
            }
        }

        for(int i=0;i<allPole.size();i++){
            JSONObject jsonArray=(JSONObject) allPole.get(i);
            Pole poleadd=new Pole();
            poleadd.setAnnotationId(jsonArray.getString("_id"));
            poleadd.setLatitude(jsonArray.getString("latitude"));
            poleadd.setLongitude(jsonArray.getString("longitude"));
            poleadd.setPolename(jsonArray.getString("name"));
            poleadd.setFencha(jsonArray.getString("style"));
            poleadd.setPoleheight(jsonArray.getString("high"));
            poleadd.setTotheproject(jsonArray.getString("projectName"));
            poleadd.setAddtime(jsonArray.getString("addTime"));
            poleadd.setOrgId(jsonArray.getString("orgId"));
            poleadd.setProjectId(jsonArray.getString("projectId"));
            poleadd.save();


            PointSet pointSet=new PointSet();
            pointSet.setMarkname(jsonArray.getString("name"));
            pointSet.setLatitude(jsonArray.getString("latitude"));
            pointSet.setLongitude(jsonArray.getString("longitude"));
            pointSet.setProjrctId(jsonArray.getString("projectId"));
            pointSet.setOrgId(jsonArray.getString("orgId"));
            pointSet.setPointType("POLE");
            pointSet.setMarkId(jsonArray.getString("_id"));
            pointSet.save();

        }

    }
    public static  void updateAllLightBox(JSONArray allLightBox){
        List<GJiaoX> gJiaoXList=DataSupport.findAll(GJiaoX.class);
        if(gJiaoXList.size()>0){
            for(int a=0;a<gJiaoXList.size();a++){
                GJiaoX gJiaoX=gJiaoXList.get(a);
                if(gJiaoX.isSaved()){
                    gJiaoX.delete();
                }
            }
        }
        for(int i=0;i<allLightBox.size();i++){
            JSONObject jsonArray=(JSONObject) allLightBox.get(i);

            GJiaoX gJiaoX=new GJiaoX();
            gJiaoX.setAnnotationId(jsonArray.getString("_id"));
            gJiaoX.setTotheproject(jsonArray.getString("projectName"));
            gJiaoX.setLongitude(jsonArray.getString("longitude"));
            gJiaoX.setLatitude(jsonArray.getString("latitude"));
            gJiaoX.setGjxlb(jsonArray.getString("level"));
            gJiaoX.setFqdjibie(jsonArray.getString("stand"));
            gJiaoX.setGjxname(jsonArray.getString("name"));
            gJiaoX.setAddtime(jsonArray.getString("addTime"));
            gJiaoX.setOrgId(jsonArray.getString("orgId"));
            gJiaoX.setProjectId(jsonArray.getString("projectId"));
            gJiaoX.save();

            PointSet pointSet=new PointSet();
            pointSet.setMarkname(jsonArray.getString("name"));
            pointSet.setLatitude(jsonArray.getString("latitude"));
            pointSet.setLongitude(jsonArray.getString("longitude"));
            pointSet.setProjrctId(jsonArray.getString("projectId"));
            pointSet.setOrgId(jsonArray.getString("orgId"));
            pointSet.setPointType("LIGHTBOX");
            pointSet.setMarkId(jsonArray.getString("_id"));
            pointSet.save();

        }
    }
    public static  void updateAllUpPoint(JSONArray allUpPoint){
        List<YinShangDian> yinShangDianList=DataSupport.findAll(YinShangDian.class);
        if(yinShangDianList.size()>0){
            for(int a=0;a<yinShangDianList.size();a++){
                YinShangDian yinShangDian=yinShangDianList.get(a);
                if(yinShangDian.isSaved()){
                    yinShangDian.delete();
                }
            }
        }

        for(int i=0;i<allUpPoint.size();i++){
            JSONObject jsonArray=(JSONObject) allUpPoint.get(i);

            YinShangDian yinShangDian=new YinShangDian();
            yinShangDian.setAnnotationId(jsonArray.getString("_id"));
            yinShangDian.setLatitude(jsonArray.getString("latitude"));
            yinShangDian.setLongitude(jsonArray.getString("longitude"));
            yinShangDian.setTotheproject(jsonArray.getString("projectName"));
            yinShangDian.setYsdname(jsonArray.getString("name"));
            yinShangDian.setAddtime(jsonArray.getString("addTime"));
            yinShangDian.setOrgId(jsonArray.getString("orgId"));
            yinShangDian.setProjectId(jsonArray.getString("projectId"));
            yinShangDian.save();

            PointSet pointSet=new PointSet();
            pointSet.setMarkname(jsonArray.getString("name"));
            pointSet.setLatitude(jsonArray.getString("latitude"));
            pointSet.setLongitude(jsonArray.getString("longitude"));
            pointSet.setProjrctId(jsonArray.getString("projectId"));
            pointSet.setOrgId(jsonArray.getString("orgId"));
            pointSet.setPointType("UPPOINT");
            pointSet.setMarkId(jsonArray.getString("_id"));
            pointSet.save();

        }
    }

    public static  void updateAllWallHang(JSONArray allWallHang){
        List<GuaQiang> guaQiangList=DataSupport.findAll(GuaQiang.class);
        if(guaQiangList.size()>0){
            for(int a=0;a<guaQiangList.size();a++){
                GuaQiang guaQiang=guaQiangList.get(a);
                if(guaQiang.isSaved()){
                    guaQiang.delete();
                }
            }
        }
        for(int i=0;i<allWallHang.size();i++){
            JSONObject jsonArray=(JSONObject) allWallHang.get(i);
            JSONObject wallHang=jsonArray.getJSONObject("wallHang");
            GuaQiang guaQiang=new GuaQiang();
            guaQiang.setAnnotationId(jsonArray.getString("_id"));
            guaQiang.setLatitude(wallHang.getString("latitude"));
            guaQiang.setLongitude(wallHang.getString("longitude"));
            guaQiang.setGqname(wallHang.getString("name"));
            guaQiang.setAddtime(wallHang.getString("addTime"));
            guaQiang.setOrgId(wallHang.getString("orgId"));
            guaQiang.setProjectId(wallHang.getString("projectId"));
            guaQiang.setTotheproject(wallHang.getString("projectName"));
            guaQiang.save();

            PointSet pointSet=new PointSet();
            pointSet.setMarkname(wallHang.getString("name"));
            pointSet.setLatitude(wallHang.getString("latitude"));
            pointSet.setLongitude(wallHang.getString("longitude"));
            pointSet.setProjrctId(wallHang.getString("projectId"));
            pointSet.setOrgId(wallHang.getString("orgId"));
            pointSet.setPointType("WALLHANG");
            pointSet.setMarkId(jsonArray.getString("_id"));
            pointSet.save();

        }
    }
    public static  void updateAllJointBox(JSONArray allJointBox){

        List<JieTouHe> jieTouHeList=DataSupport.findAll(JieTouHe.class);
        if(jieTouHeList.size()>0){
            for(int a=0;a<jieTouHeList.size();a++){
                JieTouHe jieTouHe=jieTouHeList.get(a);
                if(jieTouHe.isSaved()){
                    jieTouHe.delete();
                }
            }
        }
        for(int i=0;i<allJointBox.size();i++){
            JSONObject jsonArray=(JSONObject) allJointBox.get(i);

            JieTouHe jieTouHe=new JieTouHe();
            jieTouHe.setAnnotationId(jsonArray.getString("_id"));
            jieTouHe.setLatitude(jsonArray.getString("latitude"));
            jieTouHe.setLongitude(jsonArray.getString("longitude"));
            jieTouHe.setJthname(jsonArray.getString("name"));
            jieTouHe.setOrgId(jsonArray.getString("orgId"));
            jieTouHe.setProjectId(jsonArray.getString("projectId"));
            jieTouHe.setAddtime(jsonArray.getString("addTime"));
            jieTouHe.setTotheproject(jsonArray.getString("projectName"));
            jieTouHe.save();

            PointSet pointSet=new PointSet();
            pointSet.setMarkname(jsonArray.getString("name"));
            pointSet.setLatitude(jsonArray.getString("latitude"));
            pointSet.setLongitude(jsonArray.getString("longitude"));
            pointSet.setProjrctId(jsonArray.getString("projectId"));
            pointSet.setOrgId(jsonArray.getString("orgId"));
            pointSet.setPointType("JOINTBOX");
            pointSet.setMarkId(jsonArray.getString("_id"));
            pointSet.save();
        }
    }

    public static  void updateConduitAndCableSegment(JSONObject s){
        JSONArray conduitArray=s.getJSONArray("conduitArray");
        if(conduitArray.size()>0){
            for(int i=0;i<conduitArray.size();i++){
                JSONObject jsonObject=(JSONObject) conduitArray.get(i);
                Pipeline pipeline= DataSupport.where("startnodename=? and endnodename=? and projectId=?",
                        jsonObject.getString("startPointName"),jsonObject.getString("endPointName"),jsonObject.getString("projectId"))
                        .find(Pipeline.class).get(0);
                pipeline.setConduitId(jsonObject.getString("_id"));
                pipeline.setStartPointId(jsonObject.getString("startPointId"));
                pipeline.setEndPointId(jsonObject.getString("endPointId"));
                pipeline.save();

            }
        }
        JSONArray cableSegmentArray=s.getJSONArray("cableSegmentArray");
        if(cableSegmentArray.size()>0){
            for(int i=0;i<cableSegmentArray.size();i++){
                JSONObject jsonObject=(JSONObject)cableSegmentArray.get(i);
                CableResource cableResource=DataSupport.where("hole=? and childhole=? and startPointName=? and endPointName=? and projectId=?",
                        jsonObject.getString("useHole"), jsonObject.getString("useSubHole"),
                        jsonObject.getString("startPointName"), jsonObject.getString("endPointName"),
                        jsonObject.getString("projectId"))
                        .find(CableResource.class).get(0);
                cableResource.setCableSegmentId(jsonObject.getString("_id"));
                cableResource.setStartPointId(jsonObject.getString("startPointId"));
                cableResource.setEndPointId(jsonObject.getString("endPointId"));
                cableResource.save();
            }
        }
    }

    public static void saveConduitAndCableSegment(JSONObject s){
        JSONArray conduitArray=s.getJSONArray("conduitArray");
        if(conduitArray.size()>0){
            for(int i=0;i<conduitArray.size();i++){
                JSONObject jsonObject=(JSONObject) conduitArray.get(i);
                Pipeline pipeline= new Pipeline();
                pipeline.setConduitId(jsonObject.getString("_id"));
                pipeline.setOrgId(jsonObject.getString("orgId"));
                pipeline.setProjectId(jsonObject.getString("projectId"));
                pipeline.setStartLongitude(jsonObject.getString("startLongitude"));
                pipeline.setStartLatitude(jsonObject.getString("startLatitude"));
                pipeline.setStartPointType(jsonObject.getString("startPointType"));
                pipeline.setStartnodename(jsonObject.getString("startPointName"));
                pipeline.setStartPointId(jsonObject.getString("startPointId"));
                pipeline.setEndPointId(jsonObject.getString("endPointId"));
                pipeline.setEndLongitude(jsonObject.getString("endLongitude"));
                pipeline.setEndLatitude(jsonObject.getString("endLatitude"));
                pipeline.setEndPointType(jsonObject.getString("endPointType"));
                pipeline.setEndnodename(jsonObject.getString("endPointName"));
                pipeline.setPipelinename(jsonObject.getString("name"));
                pipeline.setPipelinehole(jsonObject.getString("holeNumber"));
                pipeline.setAddtime(jsonObject.getString("addTime"));
                pipeline.setPipelinetype(jsonObject.getString("type"));
                pipeline.setPipelinespecifica(jsonObject.getString("stand"));
                pipeline.setLength(jsonObject.getString("length"));
                pipeline.save();

            }
        }
        JSONArray cableSegmentArray=s.getJSONArray("cableSegmentArray");
        if(cableSegmentArray.size()>0){
            for(int i=0;i<cableSegmentArray.size();i++){
                JSONObject jsonObject=(JSONObject)cableSegmentArray.get(i);
                CableResource cableResource=new CableResource();
                cableResource.setCablename(jsonObject.getString("cableName"));
                cableResource.setConduitName(jsonObject.getString("conduitName"));
                cableResource.setHole(jsonObject.getString("useHole"));
                cableResource.setChildhole(jsonObject.getString("useSubHole"));
                cableResource.setLength(jsonObject.getString("length"));
                cableResource.setStartPointName(jsonObject.getString("startPointName"));
                cableResource.setStartPointType(jsonObject.getString("startPointType"));
                cableResource.setStartLongitude(jsonObject.getString("startLongitude"));
                cableResource.setStartLatitude(jsonObject.getString("startLatitude"));
                cableResource.setStartPointId(jsonObject.getString("startPointId"));
                cableResource.setCableSegmentId(jsonObject.getString("_id"));
                cableResource.setEndPointId(jsonObject.getString("endPointId"));
                cableResource.setEndLatitude(jsonObject.getString("endLatitude"));
                cableResource.setEndLongitude(jsonObject.getString("endLongitude"));
                cableResource.setEndPointName(jsonObject.getString("endPointName"));
                cableResource.setEndPointType(jsonObject.getString("endPointType"));
                cableResource.setAddTime(jsonObject.getString("addTime"));
                cableResource.setProjectId(jsonObject.getString("projectId"));
                cableResource.setOrgId(jsonObject.getString("orgId"));
                cableResource.save();
            }
        }
    }

    public static void updateAllConduit(JSONArray allConduit){
        for(int i=0;i<allConduit.size();i++){
            JSONObject jsonObject=(JSONObject) allConduit.get(i);
            Pipeline pipeline= new Pipeline();
            pipeline.setConduitId(jsonObject.getString("_id"));
            pipeline.setOrgId(jsonObject.getString("orgId"));
            pipeline.setProjectId(jsonObject.getString("projectId"));
            pipeline.setStartLongitude(jsonObject.getString("startLongitude"));
            pipeline.setStartLatitude(jsonObject.getString("startLatitude"));
            pipeline.setStartPointType(jsonObject.getString("startPointType"));
            pipeline.setStartnodename(jsonObject.getString("startPointName"));
            pipeline.setStartPointId(jsonObject.getString("startPointId"));
            pipeline.setEndPointId(jsonObject.getString("endPointId"));
            pipeline.setEndLongitude(jsonObject.getString("endLongitude"));
            pipeline.setEndLatitude(jsonObject.getString("endLatitude"));
            pipeline.setEndPointType(jsonObject.getString("endPointType"));
            pipeline.setEndnodename(jsonObject.getString("endPointName"));
            pipeline.setPipelinename(jsonObject.getString("name"));
            pipeline.setPipelinehole(jsonObject.getString("holeNumber"));
            pipeline.setAddtime(jsonObject.getString("addTime"));
            pipeline.setPipelinetype(jsonObject.getString("type"));
            pipeline.setPipelinespecifica(jsonObject.getString("stand"));
            pipeline.setLength(jsonObject.getString("length"));
            pipeline.save();

        }

    }

    public static void updateAllCableSegment(JSONArray allCableSegment){
        for(int i=0;i<allCableSegment.size();i++){
            JSONObject jsonObject=(JSONObject)allCableSegment.get(i);
            CableResource cableResource=new CableResource();
            cableResource.setCablename(jsonObject.getString("cableName"));
            cableResource.setConduitName(jsonObject.getString("conduitName"));
            cableResource.setHole(jsonObject.getString("useHole"));
            cableResource.setChildhole(jsonObject.getString("useSubHole"));
            cableResource.setLength(jsonObject.getString("length"));
            cableResource.setStartPointName(jsonObject.getString("startPointName"));
            cableResource.setStartPointType(jsonObject.getString("startPointType"));
            cableResource.setStartLongitude(jsonObject.getString("startLongitude"));
            cableResource.setStartLatitude(jsonObject.getString("startLatitude"));
            cableResource.setStartPointId(jsonObject.getString("startPointId"));
            cableResource.setCableSegmentId(jsonObject.getString("_id"));
            cableResource.setEndPointId(jsonObject.getString("endPointId"));
            cableResource.setEndLatitude(jsonObject.getString("endLatitude"));
            cableResource.setEndLongitude(jsonObject.getString("endLongitude"));
            cableResource.setEndPointName(jsonObject.getString("endPointName"));
            cableResource.setEndPointType(jsonObject.getString("endPointType"));
            cableResource.setAddTime(jsonObject.getString("addTime"));
            cableResource.setProjectId(jsonObject.getString("projectId"));
            cableResource.setOrgId(jsonObject.getString("orgId"));
            cableResource.save();
        }

    }



}
