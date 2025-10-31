package com.gala.farmsearchbackend.model.dto.wholDto;

import lombok.Data;

@Data
public class WholesalerDto {
    private Integer nodeId;
    private String name;

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