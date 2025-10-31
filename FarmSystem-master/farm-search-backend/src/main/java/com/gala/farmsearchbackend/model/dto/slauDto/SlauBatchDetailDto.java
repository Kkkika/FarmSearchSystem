// SlauBatchDetailDto.java
package com.gala.farmsearchbackend.model.dto.slauDto;

import lombok.Data;

/**
 * 屠宰产品批号详情DTO
 */
@Data
public class SlauBatchDetailDto {
    /**
     * 产品批号
     */
    private String batchId;

    /**
     * 养殖企业所在省
     */
    private String farmProvince;

    /**
     * 养殖企业所在市
     */
    private String farmCity;

    /**
     * 养殖企业名称
     */
    private String farmName;

    /**
     * 养殖企业产品批号
     */
    private String farmBatchId;

    /**
     * 养殖企业产品品种
     */
    private String farmProductType;

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

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getFarmProvince() {
        return farmProvince;
    }

    public void setFarmProvince(String farmProvince) {
        this.farmProvince = farmProvince;
    }

    public String getFarmCity() {
        return farmCity;
    }

    public void setFarmCity(String farmCity) {
        this.farmCity = farmCity;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getFarmBatchId() {
        return farmBatchId;
    }

    public void setFarmBatchId(String farmBatchId) {
        this.farmBatchId = farmBatchId;
    }

    public String getFarmProductType() {
        return farmProductType;
    }

    public void setFarmProductType(String farmProductType) {
        this.farmProductType = farmProductType;
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
}