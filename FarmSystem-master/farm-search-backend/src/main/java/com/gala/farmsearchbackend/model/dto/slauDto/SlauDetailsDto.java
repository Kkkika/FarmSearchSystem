
package com.gala.farmsearchbackend.model.dto.slauDto;

import lombok.Data;

import java.util.Objects;

/**
 * 屠宰产品批号详情DTO
 */
@Data
public class SlauDetailsDto {
    /**
     * 产品批号
     */
    private String batchId;

    //所属屠宰企业名称
    private String nodeName;

    /**
     * 所属屠宰企业所在省
     */
    private String nodeProvince;

    /**
     * 所属屠宰企业所在市
     */
    private String nodeCity;
    /**
     * 养殖企业产品批号
     */
    private String farmBatchId;

    /**
     * 肉类检验检疫合格证编号
     */
    private String quaId;

    /**
     * 官方检验员名称
     */
    private String testName;

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


    public String getFarmBatchId() {
        return farmBatchId;
    }

    public void setFarmBatchId(String farmBatchId) {
        this.farmBatchId = farmBatchId;
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