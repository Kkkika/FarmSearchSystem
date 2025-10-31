package com.gala.farmsearchbackend.model.dto.nodeDto;

import lombok.Data;
import java.io.Serializable;


public class NodeLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String password;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}