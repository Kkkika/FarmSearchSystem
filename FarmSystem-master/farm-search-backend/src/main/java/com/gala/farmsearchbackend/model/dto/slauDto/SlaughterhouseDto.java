package com.gala.farmsearchbackend.model.dto.slauDto;

import lombok.Data;

//屠宰企业信息dto--用于新增批发商产品批号
@Data
public class SlaughterhouseDto {
    private Integer nodeId;
    private String name;

    // Getter和Setter
    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}