package com.gala.farmsearchbackend.model.dto.farmDto;

import java.util.Objects;

public class FarmQueryDto {
    private Integer farmId;
    private Integer state;

    public Integer getFarmId() {
        return farmId;
    }

    public void setFarmId(Integer farmId) {
        this.farmId = farmId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FarmQueryDto that = (FarmQueryDto) o;
        return Objects.equals(farmId, that.farmId) && Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(farmId, state);
    }
}