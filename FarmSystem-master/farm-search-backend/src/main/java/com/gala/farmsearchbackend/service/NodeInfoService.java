package com.gala.farmsearchbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gala.farmsearchbackend.model.domain.NodeInfo;
import com.gala.farmsearchbackend.model.dto.nodeDto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 节点企业信息服务接口
 * @author Gala
 * @description 针对表【node_info(节点企业信息表)】的数据库操作Service
 * @createDate 2025-09-20 20:08:44
 */
public interface NodeInfoService extends IService<NodeInfo> {

    /**
     * 根据登录编码和密码获取节点企业信息
     * @param code 登录编码
     * @param password 密码
     * @return 节点企业信息
     */
    NodeInfo getNodeInfoByCodeByPass(String code, String password);

    /**
     * 按月统计节点企业数据
     * @return 月度统计列表
     */
    List<NodeInfoTotalByMonthDto> listNodeInfoDataByMonth();

    /**
     * 分页查询节点企业信息
     * @param requestDto 分页请求参数
     * @return 分页响应结果
     */
    NodeInfoPageResponseDto listNodeInfoPage(NodeInfoPageRequestDto requestDto);

    /**
     * 保存节点企业信息
     * @param nodeInfo 节点企业信息
     * @return 保存结果
     */
    int saveNodeInfo(NodeInfo nodeInfo);

    /**
     * 根据登录编码统计节点企业数量
     * @param code 登录编码
     * @return 数量
     */
    long getNodeInfoCountByCode(String code);

    /**
     * 编辑节点企业信息
     * @param nodeInfo 节点企业信息
     * @return 更新结果
     */
    int editNodeInfo(NodeInfo nodeInfo);

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
     * 更新节点企业密码
     * @param nodeId 节点企业ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 更新结果
     */
    int updatePassword(Integer nodeId, String oldPassword, String newPassword);

    /**
     * 重置节点企业密码
     * @param nodeId 节点企业ID
     * @param newPassword 新密码
     * @return 重置结果
     */
    int resetPassword(Integer nodeId, String newPassword);

    /**
     * 根据节点企业ID查询详情
     * @param nodeId 节点企业ID
     * @return 节点企业详情DTO
     */
    NodeInfoDetailsDto getNodeInfoDetailById(Integer nodeId);

    /**
     * 禁用节点企业（逻辑删除）
     */
    int disableNodeInfo(Integer nodeId);

    /**
     * 批量禁用节点企业（逻辑删除）
     */
    int batchDisableNodeInfo(List<Integer> nodeIds);

    /**
     * 启用节点企业
     */
    int enableNodeInfo(Integer nodeId);

    /**
     * 根据ID永久删除节点企业（物理删除），并级联删除所有关联的下游产品批号
     * @param nodeId 节点企业ID
     * @return 删除结果
     */
    int permanentlyDeleteNodeInfo(Integer nodeId);
}