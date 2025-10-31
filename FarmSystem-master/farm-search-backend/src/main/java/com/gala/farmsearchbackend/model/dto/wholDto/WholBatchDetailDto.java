// WholBatchDetailDto.java
package com.gala.farmsearchbackend.model.dto.wholDto;

import lombok.Data;

/**
 * 批发商产品批号详情DTO
 */
@Data
public class WholBatchDetailDto {
    /**
     * 产品批号
     */
    private String batchId;

    /**
     * 屠宰企业所在省
     */
    private String slauProvince;

    /**
     * 屠宰企业所在市
     */
    private String slauCity;

    /**
     * 屠宰企业名称
     */
    private String slauName;

    /**
     * 屠宰企业产品批号
     */
    private String slauBatchId;

    /**
     * 屠宰企业产品品种
     */
    private String slauProductType;

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

    public String getSlauProvince() {
        return slauProvince;
    }

    public void setSlauProvince(String slauProvince) {
        this.slauProvince = slauProvince;
    }

    public String getSlauCity() {
        return slauCity;
    }

    public void setSlauCity(String slauCity) {
        this.slauCity = slauCity;
    }

    public String getSlauName() {
        return slauName;
    }

    public void setSlauName(String slauName) {
        this.slauName = slauName;
    }

    public String getSlauBatchId() {
        return slauBatchId;
    }

    public void setSlauBatchId(String slauBatchId) {
        this.slauBatchId = slauBatchId;
    }

    public String getSlauProductType() {
        return slauProductType;
    }

    public void setSlauProductType(String slauProductType) {
        this.slauProductType = slauProductType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}