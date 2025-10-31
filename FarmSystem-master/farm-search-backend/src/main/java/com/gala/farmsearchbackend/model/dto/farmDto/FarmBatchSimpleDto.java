package com.gala.farmsearchbackend.model.dto.farmDto;

import lombok.Data;

@Data
public class FarmBatchSimpleDto {
    private Integer fbId;
    private String batchId;
    private String type;

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
}