package com.ck.mobileoperations.entity;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by chenkai on 2018/1/15.
 */

public class OpticalCable extends DataSupport {
    private String opticalcablename;//名
    private String coreamount;//芯数
    private String coremodel;//型号
    private String opticalcableId;
    private String addtime;

    public String getOpticalcablename() {
        return opticalcablename;
    }

    public void setOpticalcablename(String opticalcablename) {
        this.opticalcablename = opticalcablename;
    }

    public String getCoreamount() {
        return coreamount;
    }

    public void setCoreamount(String coreamount) {
        this.coreamount = coreamount;
    }

    public String getCoremodel() {
        return coremodel;
    }

    public void setCoremodel(String coremodel) {
        this.coremodel = coremodel;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getOpticalcableId(){
        return opticalcableId;
    }

    public void setOpticalcableId(String opticalcableId) {
        this.opticalcableId = opticalcableId;
    }
}
