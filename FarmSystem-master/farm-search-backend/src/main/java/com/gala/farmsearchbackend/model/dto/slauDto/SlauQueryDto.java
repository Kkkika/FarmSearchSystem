package com.gala.farmsearchbackend.model.dto.slauDto;

import java.util.Objects;

public class SlauQueryDto {
    private Integer slauId;
    private Integer state;

    public Integer getSlauId() {
        return slauId;
    }

    public void setSlauId(Integer slauId) {
        this.slauId = slauId;
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
        SlauQueryDto that = (SlauQueryDto) o;
        return Objects.equals(slauId, that.slauId) && Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slauId, state);
    }
}