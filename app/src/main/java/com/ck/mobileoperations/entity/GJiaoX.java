package com.ck.mobileoperations.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by chenkai on 2018/1/4.
 */

public class GJiaoX extends DataSupport {
    private String gjxname;
    private String fqdjibie;
    private String gjxlb;
    private String longitude;
    private String latitude;
    private String totheproject;
    private String ziyuan;
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

    public String getGjxname() {
        return gjxname;
    }

    public void setGjxname(String gjxname) {
        this.gjxname = gjxname;
    }

    public String getFqdjibie() {
        return fqdjibie;
    }

    public void setFqdjibie(String fqdjibie) {
        this.fqdjibie = fqdjibie;
    }

    public String getGjxlb() {
        return gjxlb;
    }

    public void setGjxlb(String gjxlb) {
        this.gjxlb = gjxlb;
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

    public String getZiyuan() {
        return ziyuan;
    }

    public void setZiyuan(String ziyuan) {
        this.ziyuan = ziyuan;
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
}
