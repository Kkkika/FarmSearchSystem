package com.gala.farmsearchbackend.model.dto.retaDto;

import java.util.Objects;

public class RetaQueryDto {
    private Integer retaId;
    private Integer state;

    public Integer getRetaId() {
        return retaId;
    }

    public void setRetaId(Integer retaId) {
        this.retaId = retaId;
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
        RetaQueryDto that = (RetaQueryDto) o;
        return Objects.equals(retaId, that.retaId) && Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(retaId, state);
    }
}