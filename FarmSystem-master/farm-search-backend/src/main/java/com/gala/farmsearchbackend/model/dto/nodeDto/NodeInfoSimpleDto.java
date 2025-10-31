package com.gala.farmsearchbackend.model.dto.nodeDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//分页查询节点企业dto

public class NodeInfoSimpleDto implements Serializable{
    /**
     * 节点企业编号
     */
    private Integer nodeId;

    /**
     * 登录编码
     */
    private String code;

    /**
     * 节点企业名称
     */
    private String name;

    /**
     * 企业类型
     */
    private String type;

    /**
     * 所在省
     */
    private String province;

    /**
     * 所在市
     */
    private String city;

    /**
     * 注册日期
     */
    private String regDate;

    /**
     * 删除日期
     */
    private String deleteTime;

    /**
     * 更新日期
     */
    private String updateTime;

    /**
     * 企业状态
     */
    private Integer status;

    private static final long serialVersionUID = 1L;

    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getStatus() {return status;}

    public void setStatus(Integer status) {this.status = status;}

    public String getUpdateTime() {return updateTime;}

    public void setUpdateTime(String updateTime) {this.updateTime = updateTime;}

    public String getDeleteTime() {return deleteTime;}

    public void setDeleteTime(String deleteTime) {this.deleteTime = deleteTime;}
}