package com.gala.farmsearchbackend.model.dto.nodeDto;

import lombok.Data;

import java.util.Objects;

/**
 * 省份统计DTO
 */
@Data
public class NodeInfoTotalByProvDto {
    /**
     * 省份ID
     */
    private Integer provId;

    /**
     * 省份名称
     */
    private String provName;

    /**
     * 总数
     */
    private Integer total;

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
