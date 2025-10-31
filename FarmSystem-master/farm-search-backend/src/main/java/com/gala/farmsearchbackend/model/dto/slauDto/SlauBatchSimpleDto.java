package com.gala.farmsearchbackend.model.dto.slauDto;

import lombok.Data;

//屠宰企业产品批号信息dto--用于新增批发商产品批号
@Data
public class SlauBatchSimpleDto {
    private Integer sbId;
    private String batchId;
    private String type;

    // Getter和Setter
    public Integer getSbId() {
        return sbId;
    }

    public void setSbId(Integer sbId) {
        this.sbId = sbId;
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