package com.ck.mobileoperations.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by chenkai on 2018/2/11.
 */

public class OpticalFiberJumper extends DataSupport{
    private String opticalBoxName;
    private String flangeName;
    private String location;
    private String rfId;

    public String getOpticalBoxName() {
        return opticalBoxName;
    }

    public void setOpticalBoxName(String opticalBoxName) {
        this.opticalBoxName = opticalBoxName;
    }

    public String getFlangeName() {
        return flangeName;
    }

    public void setFlangeName(String flangeName) {
        this.flangeName = flangeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRfId() {
        return rfId;
    }

    public void setRfId(String rfId) {
        this.rfId = rfId;
    }
}
