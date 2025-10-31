// TraceInfoDto.java
package com.gala.farmsearchbackend.model.dto.traceDto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
public class TraceInfoDto {
    // 养殖企业信息
    private String farmName;
    private String farmProvince;
    private String farmCity;
    private String farmBatchId;
    private String farmType;
    private String farmAgcId;
    private String farmTestName;
    private String farmBatchDate; // 养殖批号时间（年月日）

    // 屠宰企业信息
    private String slauName;
    private String slauProvince;
    private String slauCity;
    private String slauBatchId;
    private String slauType;
    private String slauQuaId;
    private String slauTestName;
    private String slauBatchDate; // 屠宰批号时间（年月日）

    // 批发商信息
    private String wholName;
    private String wholProvince;
    private String wholCity;
    private String wholBatchId;
    private String wholType;
    private String wholBatchDate; // 批发批号时间（年月日）

    // 零售商信息
    private String retaName;
    private String retaProvince;
    private String retaCity;
    private String retaBatchId;
    private String retaType;
    private String retaBatchDate; // 零售批号时间（年月日）

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
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

    public String getFarmBatchId() {
        return farmBatchId;
    }

    public void setFarmBatchId(String farmBatchId) {
        this.farmBatchId = farmBatchId;
    }

    public String getFarmType() {
        return farmType;
    }

    public void setFarmType(String farmType) {
        this.farmType = farmType;
    }

    public String getFarmAgcId() {
        return farmAgcId;
    }

    public void setFarmAgcId(String farmAgcId) {
        this.farmAgcId = farmAgcId;
    }

    public String getFarmTestName() {
        return farmTestName;
    }

    public void setFarmTestName(String farmTestName) {
        this.farmTestName = farmTestName;
    }

    public String getFarmBatchDate() {
        return farmBatchDate;
    }

    public void setFarmBatchDate(String farmBatchDate) {
        this.farmBatchDate = farmBatchDate;
    }

    public String getSlauName() {
        return slauName;
    }

    public void setSlauName(String slauName) {
        this.slauName = slauName;
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

    public String getSlauBatchId() {
        return slauBatchId;
    }

    public void setSlauBatchId(String slauBatchId) {
        this.slauBatchId = slauBatchId;
    }

    public String getSlauType() {
        return slauType;
    }

    public void setSlauType(String slauType) {
        this.slauType = slauType;
    }

    public String getSlauQuaId() {
        return slauQuaId;
    }

    public void setSlauQuaId(String slauQuaId) {
        this.slauQuaId = slauQuaId;
    }

    public String getSlauTestName() {
        return slauTestName;
    }

    public void setSlauTestName(String slauTestName) {
        this.slauTestName = slauTestName;
    }

    public String getSlauBatchDate() {
        return slauBatchDate;
    }

    public void setSlauBatchDate(String slauBatchDate) {
        this.slauBatchDate = slauBatchDate;
    }

    public String getWholName() {
        return wholName;
    }

    public void setWholName(String wholName) {
        this.wholName = wholName;
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

    public String getWholBatchId() {
        return wholBatchId;
    }

    public void setWholBatchId(String wholBatchId) {
        this.wholBatchId = wholBatchId;
    }

    public String getWholType() {
        return wholType;
    }

    public void setWholType(String wholType) {
        this.wholType = wholType;
    }

    public String getWholBatchDate() {
        return wholBatchDate;
    }

    public void setWholBatchDate(String wholBatchDate) {
        this.wholBatchDate = wholBatchDate;
    }

    public String getRetaName() {
        return retaName;
    }

    public void setRetaName(String retaName) {
        this.retaName = retaName;
    }

    public String getRetaProvince() {
        return retaProvince;
    }

    public void setRetaProvince(String retaProvince) {
        this.retaProvince = retaProvince;
    }

    public String getRetaCity() {
        return retaCity;
    }

    public void setRetaCity(String retaCity) {
        this.retaCity = retaCity;
    }

    public String getRetaBatchId() {
        return retaBatchId;
    }

    public void setRetaBatchId(String retaBatchId) {
        this.retaBatchId = retaBatchId;
    }

    public String getRetaType() {
        return retaType;
    }

    public void setRetaType(String retaType) {
        this.retaType = retaType;
    }

    public String getRetaBatchDate() {
        return retaBatchDate;
    }

    public void setRetaBatchDate(String retaBatchDate) {
        this.retaBatchDate = retaBatchDate;
    }

}