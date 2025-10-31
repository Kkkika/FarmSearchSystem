package com.gala.farmsearchbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 批发商产品批号信息表
 * @TableName whol_batch
 */
@Data
public class WholBatch implements Serializable {
    /**
     * 编号（代替batch_id与whol_id的复合主键）
     */
    @TableId(value = "wb_id", type = IdType.AUTO)
    private Integer wbId;

    /**
     * 所属批发商产品批号
     */
    private String batchId;

    /**
     * 所属批发商编号
     */
    private Integer wholId;

    /**
     * 所属屠宰企业进场编号
     */
    private Integer sbId;

    /**
     * 产品品种
     */
    private String type;

    /**
     * 批号录入日期
     */
    private LocalDateTime batchDate;

    /**
     * 批号状态：1: 新建；2: 待确认；3: 已确认；4: 已下架
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

    public Integer getWbId() {
        return wbId;
    }

    public void setWbId(Integer wbId) {
        this.wbId = wbId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getWholId() {
        return wholId;
    }

    public void setWholId(Integer wholId) {
        this.wholId = wholId;
    }

    public Integer getSbId() {
        return sbId;
    }

    public void setSbId(Integer sbId) {
        this.sbId = sbId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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