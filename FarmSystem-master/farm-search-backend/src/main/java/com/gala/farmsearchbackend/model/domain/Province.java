package com.gala.farmsearchbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 省行政区信息表
 * @TableName province
 */
@Data
public class Province implements Serializable {
    /**
     * 省行政区编号
     */
    @TableId(value = "prov_id", type = IdType.AUTO)
    private Integer provId;

    /**
     * 省行政区名称
     */
    private String provName;

    /**
     * 备注
     */
    private String remarks;

    private static final long serialVersionUID = 1L;

    public Integer getProvId() {
        return provId;
    }

    public void setProvId(Integer provId) {
        this.provId = provId;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}