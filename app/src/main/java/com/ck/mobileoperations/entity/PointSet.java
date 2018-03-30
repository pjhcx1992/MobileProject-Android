package com.ck.mobileoperations.entity;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by chenkai on 2018/1/10.
 */

public class PointSet extends DataSupport {
    private String proname;  //所属项目名
    private String longitude;//经度
    private String latitude;//纬度
    private long cratetime; //创建时间
    private int stautus;    //状态
    private String markname;//名称

    private String projrctId;
    private String orgId;
    private String pointType;
    private String markId;

    public String getMarkId() {
        return markId;
    }

    public void setMarkId(String markId) {
        this.markId = markId;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public String getProjrctId() {
        return projrctId;
    }

    public void setProjrctId(String projrctId) {
        this.projrctId = projrctId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getMarkname() {
        return markname;
    }

    public void setMarkname(String markname) {
        this.markname = markname;
    }

    public int getStautus() {
        return stautus;
    }

    public void setStautus(int stautus) {
        this.stautus = stautus;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
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

    public long getCratetime() {
        return cratetime;
    }

    public void setCratetime(long cratetime) {
        this.cratetime = cratetime;
    }
}
