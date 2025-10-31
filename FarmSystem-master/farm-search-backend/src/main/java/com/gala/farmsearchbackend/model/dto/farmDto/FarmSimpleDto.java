// FarmBatchSimpleDto.java
package com.gala.farmsearchbackend.model.dto.farmDto;

import lombok.Data;

/**
 * 养殖产品批号简略DTO
 */
@Data
public class FarmSimpleDto {
    /**
     * 产品批号
     */
    private String batchId;

    /**
     * 养殖企业所在省
     */
    private String province;

    /**
     * 养殖企业所在市
     */
    private String city;

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}