package com.gala.farmsearchbackend.model.dto.adminDto;

import java.io.Serializable;

public class AdminRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String adminName;
    private String adminPassword;
    private String role;
    private String remarks;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
