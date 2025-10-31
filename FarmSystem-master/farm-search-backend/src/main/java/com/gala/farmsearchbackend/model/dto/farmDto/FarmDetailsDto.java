
package com.gala.farmsearchbackend.model.dto.farmDto;

import lombok.Data;

import java.util.Objects;

/**
 * 养殖产品批号详情DTO
 */
@Data
public class FarmDetailsDto {
    /**
     * 产品批号
     */
    private String batchId;

    //所属养殖企业名称
    private String nodeName;

    /**
     * 产品品种
     */
    private String type;

    /**
     * 动物检验检疫合格证编号
     */
    private String agcId;

    /**
     * 官方检疫员名称
     */
    private String testName;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAgcId() {
        return agcId;
    }

    public void setAgcId(String agcId) {
        this.agcId = agcId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
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
}