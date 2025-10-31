package com.gala.farmsearchbackend.model.dto.slauDto;

import java.util.Objects;

public class SlauStateDto {
    //id
    private Integer sbId;

    /**
     * 产品批号
     */
    private String batchId;

    /**
     * 产品品种
     */
    private String type;

    //进场批号(上游企业产品批号) - 仅当状态为已确认时显示
    private String fbBatchId;

    //批号时间
    private String batchDate;

    /**
     * 批号状态
     */
    private Integer state;

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

    public String getFbBatchId() {
        return fbBatchId;
    }

    public void setFbBatchId(String fbBatchId) {
        this.fbBatchId = fbBatchId;
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