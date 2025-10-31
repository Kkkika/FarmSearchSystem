package com.gala.farmsearchbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 养殖企业产品批号信息表
 * @TableName farm_batch
 */
@Data
public class FarmBatch implements Serializable {
    /**
     * 编号（代替batch_id与farm_id的复合主键）
     */
    @TableId(value = "fb_id", type = IdType.AUTO)
    private Integer fbId;

    /**
     * 所属养殖企业产品批号
     */
    private String batchId;

    /**
     * 所属养殖企业编号
     */
    private Integer farmId;

    /**
     * 产品品种
     */
    private String type;

    /**
     * 动物检疫合格证编号
     */
    private String agcId;

    /**
     * 官方检疫员名称
     */
    private String testName;

    /**
     * 批号录入日期
     */
    private LocalDateTime batchDate;

    /**
     * 批号状态：1: 待发布；2: 已发布；3: 已下架
     */
    private Integer state;

    /**
     * 备注
     */
    private String remarks;

    private static final long serialVersionUID = 1L;

    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }

    public Integer getFbId() {
        return fbId;
    }

    public void setFbId(Integer fbId) {
        this.fbId = fbId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getFarmId() {
        return farmId;
    }

    public void setFarmId(Integer farmId) {
        this.farmId = farmId;
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

    public String getBatchDate() {
        return formatDate(batchDate);
    }

    public void setBatchDate(LocalDateTime batchDate) {
        this.batchDate = batchDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}