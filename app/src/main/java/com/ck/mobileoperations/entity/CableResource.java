package com.ck.mobileoperations.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by chenkai on 2018/2/5.
 */

public class CableResource extends DataSupport {
    private String originname;
    private String endname;
    private String hole;//管孔
    private String childhole;//子孔
    private String cablename;//光缆名

    private String projectId;
    private String orgId;
    private String conduitName;
    private String length;
    private String startPointName;
    private String startPointType;
    private String startLatitude;
    private String startPointId;
    private String startLongitude;
    private String endPointName;
    private String endPointType;
    private String endPointId;
    private String  endLatitude;
    private String endLongitude;
    private String addTime;

    private String cableSegmentId;

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

    public String getCableSegmentId() {
        return cableSegmentId;
    }

    public void setCableSegmentId(String cableSegmentId) {
        this.cableSegmentId = cableSegmentId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getConduitName() {
        return conduitName;
    }

    public void setConduitName(String conduitName) {
        this.conduitName = conduitName;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getStartPointName() {
        return startPointName;
    }

    public void setStartPointName(String startPointName) {
        this.startPointName = startPointName;
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

    public String getEndPointName() {
        return endPointName;
    }

    public void setEndPointName(String endPointName) {
        this.endPointName = endPointName;
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

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getOriginname() {
        return originname;
    }

    public void setOriginname(String originname) {
        this.originname = originname;
    }

    public String getEndname() {
        return endname;
    }

    public void setEndname(String endname) {
        this.endname = endname;
    }

    public String getHole() {
        return hole;
    }

    public void setHole(String hole) {
        this.hole = hole;
    }

    public String getChildhole() {
        return childhole;
    }

    public void setChildhole(String childhole) {
        this.childhole = childhole;
    }

    public String getCablename() {
        return cablename;
    }

    public void setCablename(String cablename) {
        this.cablename = cablename;
    }
}
