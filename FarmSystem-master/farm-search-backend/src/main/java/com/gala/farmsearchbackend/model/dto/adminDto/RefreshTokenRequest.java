package com.gala.farmsearchbackend.model.dto.adminDto;

import lombok.Data;
import java.io.Serializable;


public class RefreshTokenRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}