package com.gala.farmsearchbackend.model.dto.wholDto;

import lombok.Data;

@Data
public class WholBatchSimpleDto {
    private Integer wbId;
    private String batchId;
    private String type;

    public Integer getWbId() {
        return wbId;
    }

    public void setWbId(Integer wbId) {
        this.wbId = wbId;
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