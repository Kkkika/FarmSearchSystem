package com.gala.farmsearchbackend.model.dto.farmDto;

import java.util.Objects;

public class FarmStateDto {
    //id
    private Integer fbId;

    /**
     * 产品批号
     */
    private String batchId;

    /**
     * 产品品种
     */
    private String type;

    //批号时间
    private String batchDate;

    /**
     * 批号状态
     */
    private Integer state;

    public Integer getFbId() {
        return fbId;
    }

    public void setFbId(Integer fbId) {
        this.fbId = fbId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(String batchDate) {
        this.batchDate = batchDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

}