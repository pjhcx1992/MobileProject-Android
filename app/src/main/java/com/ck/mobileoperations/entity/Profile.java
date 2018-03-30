package com.ck.mobileoperations.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by chenkai on 2018/3/14.
 */

public class Profile extends DataSupport {
   private String department;
   private boolean isSecretary;
   private String organizationId;
   private String role;
   private String userId;
   private String profileId;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isSecretary() {
        return isSecretary;
    }

    public void setSecretary(boolean secretary) {
        isSecretary = secretary;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
