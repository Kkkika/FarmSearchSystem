package com.gala.farmsearchbackend.model.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 屠宰企业产品批号信息表
 * @TableName slau_batch
 */
@Data
public class SlauBatch implements Serializable {
    /**
     * 编号（代替batch_id与slau_id的复合主键）
     */
    @TableId(value = "sb_id", type = IdType.AUTO)
    private Integer sbId;

    /**
     * 所属屠宰企业产品批号
     */
    private String batchId;

    /**
     * 所属屠宰企业编号
     */
    private Integer slauId;

    /**
     * 所属养殖企业进场编号
     */
    private Integer fbId;

    /**
     * 产品品种
     */
    private String type;

    /**
     * 肉类检验检疫合格证编号
     */
    private String quaId;

    /**
     * 官方检验员名称
     */
    private String testName;

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

    public Integer getSbId() {
        return sbId;
    }

    public void setSbId(Integer sbId) {
        this.sbId = sbId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getSlauId() {
        return slauId;
    }

    public void setSlauId(Integer slauId) {
        this.slauId = slauId;
    }

    public Integer getFbId() {
        return fbId;
    }

    public void setFbId(Integer fbId) {
        this.fbId = fbId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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