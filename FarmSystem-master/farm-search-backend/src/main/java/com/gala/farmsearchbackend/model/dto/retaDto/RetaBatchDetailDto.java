// RetaBatchDetailDto.java
package com.gala.farmsearchbackend.model.dto.retaDto;

import lombok.Data;

/**
 * 零售商产品批号详情DTO
 */
@Data
public class RetaBatchDetailDto {
    /**
     * 产品批号
     */
    private String batchId;

    /**
     * 批发商所在省
     */
    private String wholProvince;

    /**
     * 批发商所在市
     */
    private String wholCity;

    /**
     * 批发商名称
     */
    private String wholName;

    /**
     * 批发商产品批号
     */
    private String wholBatchId;

    /**
     * 批发商产品品种
     */
    private String wholProductType;

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

    public String getWholProvince() {
        return wholProvince;
    }

    public void setWholProvince(String wholProvince) {
        this.wholProvince = wholProvince;
    }

    public String getWholCity() {
        return wholCity;
    }

    public void setWholCity(String wholCity) {
        this.wholCity = wholCity;
    }

    public String getWholName() {
        return wholName;
    }

    public void setWholName(String wholName) {
        this.wholName = wholName;
    }

    public String getWholBatchId() {
        return wholBatchId;
    }

    public void setWholBatchId(String wholBatchId) {
        this.wholBatchId = wholBatchId;
    }

    public String getWholProductType() {
        return wholProductType;
    }

    public void setWholProductType(String wholProductType) {
        this.wholProductType = wholProductType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}