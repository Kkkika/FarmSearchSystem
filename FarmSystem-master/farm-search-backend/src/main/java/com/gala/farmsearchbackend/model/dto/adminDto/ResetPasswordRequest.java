package com.gala.farmsearchbackend.model.dto.adminDto;


import java.io.Serializable;

/**
 * 重置密码请求DTO
 */
public class ResetPasswordRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer adminId;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
}
