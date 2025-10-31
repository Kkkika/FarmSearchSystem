package com.gala.farmsearchbackend.model.dto.nodeDto;

import lombok.Data;

import java.util.Objects;

/**
 * 月度统计DTO
 */
@Data
public class NodeInfoTotalByMonthDto {
    /**
     * 月份
     */
    private String month;

    /**
     * 总数
     */
    private Integer total;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
