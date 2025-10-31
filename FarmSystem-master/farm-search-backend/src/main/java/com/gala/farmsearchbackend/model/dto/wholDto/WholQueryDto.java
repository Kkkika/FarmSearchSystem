package com.gala.farmsearchbackend.model.dto.wholDto;

import java.util.Objects;

public class WholQueryDto {
    private Integer wholId;
    private Integer state;

    public Integer getWholId() {
        return wholId;
    }

    public void setWholId(Integer wholId) {
        this.wholId = wholId;
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
        WholQueryDto that = (WholQueryDto) o;
        return Objects.equals(wholId, that.wholId) && Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wholId, state);
    }
}