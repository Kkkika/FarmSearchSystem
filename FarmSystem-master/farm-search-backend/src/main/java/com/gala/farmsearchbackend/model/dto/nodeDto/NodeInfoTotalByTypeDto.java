package com.gala.farmsearchbackend.model.dto.nodeDto;

import lombok.Data;

import java.util.Objects;

/**
 * 类型统计DTO
 */
@Data
public class NodeInfoTotalByTypeDto {
    /**
     * 企业类型
     */
    private Integer type;

    /**
     * 总数
     */
    private Integer total;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}