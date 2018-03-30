package com.ck.mobileoperations.entity;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by chenkai on 2018/1/2.
 */

public class ProName extends DataSupport {
    private String proname;
    private String status;//0 创建中 1待审核 2已驳回 3.已通过
    private String checkstatus;
    private String addtime;
    private String projectId;
    private String orgId;
    private String createMember;//谁创建的

    public String getCreateMember() {
        return createMember;
    }

    public void setCreateMember(String createMember) {
        this.createMember = createMember;
    }

    public String getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(String checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
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
}
