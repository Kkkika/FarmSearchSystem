package com.gala.farmsearchbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 零售商产品批号信息表
 * @TableName reta_batch
 */
@Data
public class RetaBatch implements Serializable {
    /**
     * 编号（代替batch_id与reta_id的复合主键）
     */
    @TableId(value = "rb_id", type = IdType.AUTO)
    private Integer rbId;

    /**
     * 所属零售商产品批号
     */
    private String batchId;

    /**
     * 所属零售商编号
     */
    private Integer retaId;

    /**
     * 所属批发商进场编号
     */
    private Integer wbId;

    /**
     * 产品品种
     */
    private String type;

    /**
     * 批号录入日期
     */
    private LocalDateTime batchDate;

    /**
     * 溯源标识码
     */
    private String sourceId;

    /**
     * 溯源二维码
     */
    private String sourceQr;

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

    public Integer getRbId() {
        return rbId;
    }

    public void setRbId(Integer rbId) {
        this.rbId = rbId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getRetaId() {
        return retaId;
    }

    public void setRetaId(Integer retaId) {
        this.retaId = retaId;
    }

    public Integer getWbId() {
        return wbId;
    }

    public void setWbId(Integer wbId) {
        this.wbId = wbId;
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

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceQr() {
        return sourceQr;
    }

    public void setSourceQr(String sourceQr) {
        this.sourceQr = sourceQr;
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