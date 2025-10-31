package com.gala.farmsearchbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 节点企业信息表
 * @TableName node_info
 */
@Data
public class NodeInfo implements Serializable {
    /**
     * 节点企业编号
     */
    @TableId(value = "node_id", type = IdType.AUTO)
    private Integer nodeId;

    /**
     * 登录编码
     */
    private String code;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 节点企业名称
     */
    private String name;

    /**
     * 企业类型：1: 养殖企业；2: 屠宰企业；3: 批发商；4: 零售商
     */
    private Integer type;

    /**
     * 所在省编号
     */
    private Integer provId;

    /**
     * 所在市编号
     */
    private Integer cityId;

    /**
     * 节点企业地址
     */
    private String address;

    /**
     * 营业执照代码（所有节点必填）
     */
    private String businessId;

    /**
     * 动物防疫条件合格证编号（养殖企业必填）
     */
    private String epId;

    /**
     * 环境影响评价资质证书编号（屠宰、养殖企业必填）
     */
    private String eiaId;

    /**
     * 食品流通许可证编号（批发商必填）
     */
    private String cirId;

    /**
     * 食品经营许可证编号（批发商、零售商必填）
     */
    private String fbId;

    /**
     * 法定代表人
     */
    private String corporation;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 注册日期
     */
    private LocalDate regDate;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 状态：0-正常，1-禁用
     */
    private Integer status;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deleteTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;

    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getEpId() {
        return epId;
    }

    public void setEpId(String epId) {
        this.epId = epId;
    }

    public String getEiaId() {
        return eiaId;
    }

    public void setEiaId(String eiaId) {
        this.eiaId = eiaId;
    }

    public String getCirId() {
        return cirId;
    }

    public void setCirId(String cirId) {
        this.cirId = cirId;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getCorporation() {
        return corporation;
    }

    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRegDate() {
        if (this.regDate == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.regDate.format(formatter);
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDeleteTime() {
        return formatDateTime(deleteTime);
    }

    public void setDeleteTime(LocalDateTime deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getUpdateTime() {
        return formatDateTime(updateTime);
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}