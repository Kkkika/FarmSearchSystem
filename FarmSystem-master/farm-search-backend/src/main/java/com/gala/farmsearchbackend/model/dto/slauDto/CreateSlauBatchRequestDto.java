package com.gala.farmsearchbackend.model.dto.slauDto;

import lombok.Data;

@Data
public class CreateSlauBatchRequestDto {
    private String batchId;
    private Integer slauId;
    private Integer fbId;
    private String type;
    private String quaId;
    private String testName;
    private String remarks;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getSlauId() {
        return slauId;
    }

    public void setSlauId(Integer slauId) {
        this.slauId = slauId;
    }

    public Integer getFbId() {
        return fbId;
    }

    public void setFbId(Integer fbId) {
        this.fbId = fbId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuaId() {
        return quaId;
    }

    public void setQuaId(String quaId) {
        this.quaId = quaId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}