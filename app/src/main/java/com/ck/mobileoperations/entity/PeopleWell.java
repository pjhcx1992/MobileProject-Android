package com.ck.mobileoperations.entity;

import com.amap.api.maps.model.LatLng;

import org.litepal.crud.DataSupport;

/**
 * Created by chenkai on 2017/12/27.
 */

public class PeopleWell extends DataSupport {
    private String longitude;//经度
    private String latitude;//纬度
    private String wellname;
    private String specifications;//  人井 单页井
    private String oldandnew;
    private int category;
    private String  totheproject;//所属工程
    private LatLng latLng;
    private int upNorth;
    private int belowSouth;
    private int leftWest;
    private int rightEast;

    private String addtime;
    private String orgId;//公司的ID
    private String projectId;//所属的project的Id
    private String annotationId;//当前井的ID

    public String getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(String annotationId) {
        this.annotationId = annotationId;
    }

    public String getOldandnew() {
        return oldandnew;
    }

    public void setOldandnew(String oldandnew) {
        this.oldandnew = oldandnew;
    }

    public String getTotheproject() {
        return totheproject;
    }

    public void setTotheproject(String totheproject) {
        this.totheproject = totheproject;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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

    public String getWellname() {
        return wellname;
    }

    public void setWellname(String wellname) {
        this.wellname = wellname;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
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
