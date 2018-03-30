package com.ck.mobileoperations.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by chenkai on 2018/2/11.
 */

public class JumpOperation extends DataSupport {
    private String originName,endName;
    private String originFlange,endFlange;
    private String originRfID,endRfID;
    private String originLocation,endLocation;
    private String operationName;//操作人

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public String getOriginFlange() {
        return originFlange;
    }

    public void setOriginFlange(String originFlange) {
        this.originFlange = originFlange;
    }

    public String getEndFlange() {
        return endFlange;
    }

    public void setEndFlange(String endFlange) {
        this.endFlange = endFlange;
    }

    public String getOriginRfID() {
        return originRfID;
    }

    public void setOriginRfID(String originRfID) {
        this.originRfID = originRfID;
    }

    public String getEndRfID() {
        return endRfID;
    }

    public void setEndRfID(String endRfID) {
        this.endRfID = endRfID;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
