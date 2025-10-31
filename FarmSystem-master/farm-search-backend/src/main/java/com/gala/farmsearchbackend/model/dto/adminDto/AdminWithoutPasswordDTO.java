package com.gala.farmsearchbackend.model.dto.adminDto;

import java.io.Serializable;

/**
 * 不包含密码的管理员信息DTO
 */
public class AdminWithoutPasswordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer adminId;
    private String adminName;
    private String role;
    private String remarks;

    public AdminWithoutPasswordDTO() {
    }

    public AdminWithoutPasswordDTO(Integer adminId, String adminName, String role, String remarks) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.role = role;
        this.remarks = remarks;
    }

    // Getters and Setters
    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
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