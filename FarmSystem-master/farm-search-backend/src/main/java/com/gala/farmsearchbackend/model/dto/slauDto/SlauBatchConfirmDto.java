package com.gala.farmsearchbackend.model.dto.slauDto;

import lombok.Data;


public class SlauBatchConfirmDto {
    /**
     * 屠宰企业产品批号ID
     */
    private Integer sbId;

    /**
     * 确认备注
     */
    private String confirmRemarks;

    public Integer getSbId() {
        return sbId;
    }

    public void setSbId(Integer sbId) {
        this.sbId = sbId;
    }

    public String getConfirmRemarks() {
        return confirmRemarks;
    }

    public void setConfirmRemarks(String confirmRemarks) {
        this.confirmRemarks = confirmRemarks;
    }
}
