package com.ck.mobileoperations.utils.cable;

/**
 * Created by chenkai on 2018/2/4.
 */

public class CableCache {
    private String cableorigin; //起点
    private String hole;  //管孔
    private String childhole; //子孔
    private String cablename;

    public String getCableorigin() {
        return cableorigin;
    }

    public void setCableorigin(String cableorigin) {
        this.cableorigin = cableorigin;
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
