package com.gala.farmsearchbackend.model.dto.retaDto;

import lombok.Data;

@Data
public class CreateRetaBatchRequestDto {
    private String batchId;
    private Integer retaId;
    private Integer wbId;
    private String type;
    private String remarks;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getRetaId() {
        return retaId;
    }

    public void setRetaId(Integer retaId) {
        this.retaId = retaId;
    }

    public Integer getWbId() {
        return wbId;
    }

    public void setWbId(Integer wbId) {
        this.wbId = wbId;
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