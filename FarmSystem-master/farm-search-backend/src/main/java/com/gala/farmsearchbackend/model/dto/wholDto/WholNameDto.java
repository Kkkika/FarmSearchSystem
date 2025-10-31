package com.gala.farmsearchbackend.model.dto.wholDto;

import java.util.Objects;

public class WholNameDto {
    //id
    private Integer wbId;

    //所属批发商名称
    private String nodeName;

    /**
     * 产品批号
     */
    private String batchId;

    /**
     * 产品品种
     */
    private String type;

    //进场批号(上游企业产品批号)
    private String sbBatchId;

    //批号时间
    private String batchDate;

    /**
     * 批号状态
     */
    private Integer state;

    public Integer getWbId() {
        return wbId;
    }

    public void setWbId(Integer wbId) {
        this.wbId = wbId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
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

    public String getSbBatchId() {
        return sbBatchId;
    }

    public void setSbBatchId(String sbBatchId) {
        this.sbBatchId = sbBatchId;
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
