package com.ck.mobileoperations.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by chenkai on 2018/1/4.
 */

public class GuaQiang extends DataSupport {
    private String longitude;//经度
    private String latitude;//纬度
    private String  totheproject;//所属工程
    private String gqname;
    private int upNorth;
    private int belowSouth;
    private int leftWest;
    private int rightEast;
    private String addtime;
    private String orgId;
    private String projectId;
    private String annotationId;

    public String getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(String annotationId) {
        this.annotationId = annotationId;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTotheproject() {
        return totheproject;
    }

    public void setTotheproject(String totheproject) {
        this.totheproject = totheproject;
    }

    public String getGqname() {
        return gqname;
    }

    public void setGqname(String gqname) {
        this.gqname = gqname;
    }

    public int getUpNorth() {
        return upNorth;
    }

    public void setUpNorth(int upNorth) {
        this.upNorth = upNorth;
    }

    public int getBelowSouth() {
        return belowSouth;
    }

    public void setBelowSouth(int belowSouth) {
        this.belowSouth = belowSouth;
    }

    public int getLeftWest() {
        return leftWest;
    }

    public void setLeftWest(int leftWest) {
        this.leftWest = leftWest;
    }

    public int getRightEast() {
        return rightEast;
    }

    public void setRightEast(int rightEast) {
        this.rightEast = rightEast;
    }
}
