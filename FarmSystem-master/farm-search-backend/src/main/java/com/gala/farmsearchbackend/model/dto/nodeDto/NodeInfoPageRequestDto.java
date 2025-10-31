package com.gala.farmsearchbackend.model.dto.nodeDto;

import lombok.Data;

import java.util.Objects;

/**
 * 节点企业分页请求DTO
 */
@Data
public class NodeInfoPageRequestDto {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页最大记录数
     */
    private Integer maxPageNum;

    /**
     * 企业名称（模糊查询）
     */
    private String name;

    /**
     * 企业类型（1:养殖企业；2:屠宰企业；3:批发商；4:零售商）
     */
    private Integer type;

    /**
     * 所在省份ID
     */
    private Integer provId;

    /**
     * 所在城市ID
     */
    private Integer cityId;

    /**
     * 登录编码
     */
    private String code;

    /**
     * 企业状态（0:正常启用；1:禁用；null:查询所有状态）
     */
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getMaxPageNum() {
        return maxPageNum;
    }

    public void setMaxPageNum(Integer maxPageNum) {
        this.maxPageNum = maxPageNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getProvId() {
        return provId;
    }

    public void setProvId(Integer provId) {
        this.provId = provId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeInfoPageRequestDto that = (NodeInfoPageRequestDto) o;
        return Objects.equals(pageNum, that.pageNum) &&
                Objects.equals(maxPageNum, that.maxPageNum) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(provId, that.provId) &&
                Objects.equals(cityId, that.cityId) &&
                Objects.equals(code, that.code) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNum, maxPageNum, name, type, provId, cityId, code, status);

    }
}