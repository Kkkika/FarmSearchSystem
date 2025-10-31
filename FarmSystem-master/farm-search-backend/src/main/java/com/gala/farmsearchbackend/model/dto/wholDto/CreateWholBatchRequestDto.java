package com.gala.farmsearchbackend.model.dto.wholDto;

import lombok.Data;

@Data
public class CreateWholBatchRequestDto {
    private String batchId;
    private Integer wholId;
    private Integer sbId;
    private String type;
    private String remarks;

    // Getter和Setter
    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getWholId() {
        return wholId;
    }

    public void setWholId(Integer wholId) {
        this.wholId = wholId;
    }

    public Integer getSbId() {
        return sbId;
    }

    public void setSbId(Integer sbId) {
        this.sbId = sbId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}