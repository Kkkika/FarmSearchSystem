
package com.gala.farmsearchbackend.model.dto.wholDto;

import lombok.Data;

import java.util.Objects;

/**
 * 批发商产品批号详情DTO
 */
@Data
public class WholDetailsDto {
    /**
     * 产品批号
     */
    private String batchId;

    //所属批发商名称
    private String nodeName;

    /**
     * 所属批发商所在省
     */
    private String nodeProvince;

    /**
     * 所属批发商所在市
     */
    private String nodeCity;

    /**
     * 屠宰企业产品批号
     */
    private String slauBatchId;

    /**
     * 产品类型
     */
    private String type;

    //批号时间
    private String batchDate;

    //状态
    private String state;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getSlauBatchId() {
        return slauBatchId;
    }

    public void setSlauBatchId(String slauBatchId) {
        this.slauBatchId = slauBatchId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(String batchDate) {
        this.batchDate = batchDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNodeProvince() {
        return nodeProvince;
    }

    public void setNodeProvince(String nodeProvince) {
        this.nodeProvince = nodeProvince;
    }

    public String getNodeCity() {
        return nodeCity;
    }

    public void setNodeCity(String nodeCity) {
        this.nodeCity = nodeCity;
    }

}