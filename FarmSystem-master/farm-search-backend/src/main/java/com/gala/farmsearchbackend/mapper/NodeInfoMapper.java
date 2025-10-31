package com.gala.farmsearchbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gala.farmsearchbackend.model.domain.NodeInfo;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoTotalByMonthDto;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoTotalByProvDto;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoTotalByTypeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点企业信息Mapper接口
 * @author Gala
 * @description 针对表【node_info(节点企业信息表)】的数据库操作Mapper
 * @createDate 2025-09-20 20:08:44
 */
@Mapper
public interface NodeInfoMapper extends BaseMapper<NodeInfo> {

    /**
     * 根据登录编码和密码获取节点企业信息
     * @param code 登录编码
     * @param password 密码
     * @return 节点企业信息
     */
    NodeInfo getNodeInfoByCodeByPass(@Param("code") String code, @Param("password") String password);

    /**
     * 获取节点企业总数
     * @return 总数
     */
    int getNodeInfoCount();

    /**
     * 按省份统计节点企业数据
     * @return 省份统计列表
     */
    List<NodeInfoTotalByProvDto> listNodeInfoDataByProv();

    /**
     * 按类型统计节点企业数据
     * @return 类型统计列表
     */
    List<NodeInfoTotalByTypeDto> listNodeInfoDataByType();

    /**
     * 按月统计节点企业数据
     * @return 月度统计列表
     */
    List<NodeInfoTotalByMonthDto> listNodeInfoDataByMonth();

    /**
     * 根据节点企业ID查询详情
     * @param nodeId 节点企业ID
     * @return 节点企业详情DTO
     */
    NodeInfoDetailsDto selectNodeInfoDetailById(@Param("nodeId") Integer nodeId);
}