package com.gala.farmsearchbackend.model.dto.adminDto;


import java.io.Serializable;

/**
 * 重置密码响应DTO
 */
public class ResetPasswordResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer adminId;
    private String adminName;
    private String newPassword;

    public ResetPasswordResponse() {
    }

    public ResetPasswordResponse(Integer adminId, String adminName, String newPassword) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.newPassword = newPassword;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
