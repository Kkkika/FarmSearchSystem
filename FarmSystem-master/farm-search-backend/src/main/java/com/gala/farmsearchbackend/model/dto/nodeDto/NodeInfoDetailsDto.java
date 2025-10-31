package com.gala.farmsearchbackend.model.dto.nodeDto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//节点企业查看详情dto
@Data
public class NodeInfoDetailsDto{
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
    private LocalDateTime regDate;

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
        return formatDate(regDate);
    }

    public void setRegDate(LocalDateTime regDate) {
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

}