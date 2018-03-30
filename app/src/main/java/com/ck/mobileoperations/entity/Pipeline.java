package com.ck.mobileoperations.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by chenkai on 2018/2/1.
 */

public class Pipeline extends DataSupport {
    private String startnodename;
    private String startPointType;
    private String startLatitude;
    private String startLongitude;
    private String endnodename;
    private String endPointType;
    private String endLatitude;
    private String endLongitude;
    private String pipelinetype;//类型
    private String pipelinespecifica;//规格
    private String pipelinehole;//孔数
    private String pipelinename;
    private String addtime;
    private String projectname;
    private String projectId;
    private String orgId;
    private String conduitId;
    private String startPointId;
    private String endPointId;
    private String length;

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getStartPointId() {
        return startPointId;
    }

    public void setStartPointId(String startPointId) {
        this.startPointId = startPointId;
    }

    public String getEndPointId() {
        return endPointId;
    }

    public void setEndPointId(String endPointId) {
        this.endPointId = endPointId;
    }

    public String getConduitId() {
        return conduitId;
    }

    public void setConduitId(String conduitId) {
        this.conduitId = conduitId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getStartnodename() {
        return startnodename;
    }

    public void setStartnodename(String startnodename) {
        this.startnodename = startnodename;
    }

    public String getEndnodename() {
        return endnodename;
    }

    public void setEndnodename(String endnodename) {
        this.endnodename = endnodename;
    }

    public String getPipelinetype() {
        return pipelinetype;
    }

    public void setPipelinetype(String pipelinetype) {
        this.pipelinetype = pipelinetype;
    }

    public String getPipelinespecifica() {
        return pipelinespecifica;
    }

    public void setPipelinespecifica(String pipelinespecifica) {
        this.pipelinespecifica = pipelinespecifica;
    }

    public String getPipelinehole() {
        return pipelinehole;
    }

    public void setPipelinehole(String pipelinehole) {
        this.pipelinehole = pipelinehole;
    }

    public String getPipelinename() {
        return pipelinename;
    }

    public void setPipelinename(String pipelinename) {
        this.pipelinename = pipelinename;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getStartPointType() {
        return startPointType;
    }

    public void setStartPointType(String startPointType) {
        this.startPointType = startPointType;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getEndPointType() {
        return endPointType;
    }

    public void setEndPointType(String endPointType) {
        this.endPointType = endPointType;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }
}
