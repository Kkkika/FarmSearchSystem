package com.gala.farmsearchbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gala.farmsearchbackend.annotation.CacheEvict;
import com.gala.farmsearchbackend.annotation.Cacheable;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.exception.GlobalExceptionHandler;
import com.gala.farmsearchbackend.mapper.NodeInfoMapper;
import com.gala.farmsearchbackend.model.domain.*;
import com.gala.farmsearchbackend.model.dto.nodeDto.*;
import com.gala.farmsearchbackend.service.*;
import com.gala.farmsearchbackend.utils.PasswordEncoderUtil;
import com.gala.farmsearchbackend.utils.PasswordPolicyValidator;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 节点企业信息服务实现
 * @author Gala
 * @description 针对表【node_info(节点企业信息表)】的数据库操作Service实现
 * @createDate 2025-09-20 20:08:44
 */
@Service
public class NodeInfoServiceImpl extends ServiceImpl<NodeInfoMapper, NodeInfo>
        implements NodeInfoService {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Resource
    private NodeInfoMapper nodeInfoMapper;

    @Resource
    private PasswordEncoderUtil passwordEncoderUtil;

    @Resource
    private PasswordPolicyValidator passwordPolicyValidator;

    @Resource
    private ProvinceService provinceService;

    @Resource
    private CityService cityService;

    @Resource
    private FarmBatchService farmBatchService;

    @Resource
    @Lazy
    private SlauBatchService slauBatchService;

    @Resource
    private WholBatchService wholBatchService;

    @Resource
    private RetaBatchService retaBatchService;
    @Override
    public NodeInfo getNodeInfoByCodeByPass(String code, String password) {
        LambdaQueryWrapper<NodeInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NodeInfo::getCode, code);
        NodeInfo nodeInfo = getOne(queryWrapper);

        if (nodeInfo == null) {
            return null;
        }

        if (!passwordEncoderUtil.matches(password, nodeInfo.getPassword())) {
            return null;
        }

        return nodeInfo;
    }

    @Override
    @Cacheable(key = "'node:stats:month'", expire = 1800)
    public List<NodeInfoTotalByMonthDto> listNodeInfoDataByMonth() {
        return nodeInfoMapper.listNodeInfoDataByMonth();
    }

    @Override
    @Cacheable(key = "'node:page:' + #requestDto.hashCode()", expire = 300) // 缓存5分钟
    public NodeInfoPageResponseDto listNodeInfoPage(NodeInfoPageRequestDto requestDto) {
        // 创建分页对象
        Page<NodeInfo> page = new Page<>(requestDto.getPageNum(), requestDto.getMaxPageNum());

        // 创建查询条件
        LambdaQueryWrapper<NodeInfo> queryWrapper = new LambdaQueryWrapper<>();

        // 状态筛选条件：如果指定了状态按指定状态查询，否则查询所有状态
        if (requestDto.getStatus() != null) {
            queryWrapper.eq(NodeInfo::getStatus, requestDto.getStatus());
        }
        // 如果status为null，不添加状态条件，查询所有状态

        // 添加其他查询条件
        if (StringUtils.isNotBlank(requestDto.getName())) {
            queryWrapper.like(NodeInfo::getName, requestDto.getName());
        }

        if (requestDto.getType() != null) {
            queryWrapper.eq(NodeInfo::getType, requestDto.getType());
        }

        if (requestDto.getProvId() != null) {
            queryWrapper.eq(NodeInfo::getProvId, requestDto.getProvId());
        }

        if (requestDto.getCityId() != null) {
            queryWrapper.eq(NodeInfo::getCityId, requestDto.getCityId());
        }

        if (StringUtils.isNotBlank(requestDto.getCode())) {
            queryWrapper.like(NodeInfo::getCode, requestDto.getCode());
        }

        // 排序逻辑：根据状态决定排序字段
        if (requestDto.getStatus() != null && requestDto.getStatus() == 1) {
            // 查询禁用状态时，按删除时间降序排列
            queryWrapper.orderByDesc(NodeInfo::getDeleteTime);
        } else {
            // 查询启用状态或所有状态时，按注册日期降序排列
            queryWrapper.orderByDesc(NodeInfo::getRegDate);
        }

        // 执行分页查询
        Page<NodeInfo> resultPage = page(page, queryWrapper);

        // 将 NodeInfo 转换为 NodeInfoSimpleDto
        List<NodeInfoSimpleDto> simpleDtoList = resultPage.getRecords().stream()
                .map(this::convertToSimpleDto)
                .collect(Collectors.toList());

        // 构建响应DTO
        NodeInfoPageResponseDto responseDto = new NodeInfoPageResponseDto();
        responseDto.setTotalRow((int) resultPage.getTotal());
        responseDto.setTotalPageNum((int) resultPage.getPages());
        responseDto.setCurrentPage((int) resultPage.getCurrent());
        responseDto.setPageSize((int) resultPage.getSize());
        responseDto.setData(simpleDtoList);

        return responseDto;
    }

    /**
     * 将 NodeInfo 转换为 NodeInfoSimpleDto
     */
    private NodeInfoSimpleDto convertToSimpleDto(NodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return null;
        }

        NodeInfoSimpleDto simpleDto = new NodeInfoSimpleDto();
        simpleDto.setNodeId(nodeInfo.getNodeId());
        simpleDto.setCode(nodeInfo.getCode());
        simpleDto.setName(nodeInfo.getName());
        simpleDto.setType(convertTypeToString(nodeInfo.getType()));
        simpleDto.setRegDate(nodeInfo.getRegDate());
        simpleDto.setStatus(nodeInfo.getStatus());
        simpleDto.setUpdateTime(nodeInfo.getUpdateTime());
        simpleDto.setDeleteTime(nodeInfo.getDeleteTime());

        // 查询省份和城市名称
        if (nodeInfo.getProvId() != null) {
            Province province = provinceService.getById(nodeInfo.getProvId());
            if (province != null) {
                simpleDto.setProvince(province.getProvName());
            }
        }

        if (nodeInfo.getCityId() != null) {
            City city = cityService.getById(nodeInfo.getCityId());
            if (city != null) {
                simpleDto.setCity(city.getCityName());
            }
        }

        return simpleDto;
    }

    /**
     * 将类型代码转换为类型名称
     */
    private String convertTypeToString(Integer type) {
        if (type == null) {
            return "未知类型";
        }
        switch (type) {
            case 1: return "养殖企业";
            case 2: return "屠宰企业";
            case 3: return "批发商";
            case 4: return "零售商";
            default: return "未知类型";
        }
    }

    @Override
    public int saveNodeInfo(NodeInfo nodeInfo) {
        passwordPolicyValidator.validatePassword(nodeInfo.getPassword());
        String encodedPassword = passwordEncoderUtil.encodePassword(nodeInfo.getPassword());
        nodeInfo.setPassword(encodedPassword);

        return nodeInfoMapper.insert(nodeInfo);
    }

    @Override
    public long getNodeInfoCountByCode(String code) {
        LambdaQueryWrapper<NodeInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NodeInfo::getCode, code);
        return count(queryWrapper);
    }

    @Override
    public int editNodeInfo(NodeInfo nodeInfo) {
        return nodeInfoMapper.updateById(nodeInfo);
    }

    @Override
    @Cacheable(key = "'node:stats:prov'", expire = 1800) // 缓存30分钟
    public List<NodeInfoTotalByProvDto> listNodeInfoDataByProv() {
        return nodeInfoMapper.listNodeInfoDataByProv();
    }

    @Override
    @Cacheable(key = "'node:stats:type'", expire = 1800)
    public List<NodeInfoTotalByTypeDto> listNodeInfoDataByType() {
        return nodeInfoMapper.listNodeInfoDataByType();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePassword(Integer nodeId, String oldPassword, String newPassword) {
        NodeInfo nodeInfo = getById(nodeId);
        if (nodeInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "节点企业不存在");
        }

        if (!passwordEncoderUtil.matches(oldPassword, nodeInfo.getPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "原密码错误");
        }

        passwordPolicyValidator.validatePassword(newPassword);
        passwordPolicyValidator.validateNotSameAsOld(newPassword, oldPassword);

        String encodedNewPassword = passwordEncoderUtil.encodePassword(newPassword);

        LambdaUpdateWrapper<NodeInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(NodeInfo::getNodeId, nodeId)
                .set(NodeInfo::getPassword, encodedNewPassword);

        return nodeInfoMapper.update(null, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resetPassword(Integer nodeId, String newPassword) {
        NodeInfo nodeInfo = getById(nodeId);
        if (nodeInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "节点企业不存在");
        }

        passwordPolicyValidator.validatePassword(newPassword);
        String encodedNewPassword = passwordEncoderUtil.encodePassword(newPassword);

        LambdaUpdateWrapper<NodeInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(NodeInfo::getNodeId, nodeId)
                .set(NodeInfo::getPassword, encodedNewPassword);

        return nodeInfoMapper.update(null, updateWrapper);
    }

    @Override
    public NodeInfoDetailsDto getNodeInfoDetailById(Integer nodeId) {
        if (nodeId == null || nodeId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "节点企业ID不能为空");
        }

        NodeInfoDetailsDto detailDto = nodeInfoMapper.selectNodeInfoDetailById(nodeId);
        if (detailDto == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "节点企业信息不存在");
        }

        return detailDto;
    }

    @Override
    @CacheEvict(key = {
            "'node:stats:*'",                    // 企业统计缓存
            "'node:page:*'",                     // 企业分页缓存
            "'farm:state:*'",                    // 养殖批号状态缓存
            "'slau:state:*'",                    // 屠宰批号状态缓存
            "'whol:state:*'",                    // 批发批号状态缓存
            "'reta:state:*'",                    // 零售批号状态缓存
            "'slau:pending:*'",                  // 屠宰待确认缓存
            "'whol:pending:*'",                  // 批发待确认缓存
            "'reta:pending:*'"                   // 零售待确认缓存
    }, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int disableNodeInfo(Integer nodeId) {
        NodeInfo nodeInfo = getById(nodeId);
        if (nodeInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "节点企业不存在");
        }

        // 1. 禁用节点企业
        LambdaUpdateWrapper<NodeInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(NodeInfo::getNodeId, nodeId)
                .set(NodeInfo::getStatus, 1) // 禁用状态
                .set(NodeInfo::getDeleteTime, LocalDateTime.now());

        int result = nodeInfoMapper.update(null, updateWrapper);

        if (result > 0) {
            // 2. 级联下架所有关联的产品批号
            cascadeDisableProductBatches(nodeInfo);
        }

        return result;
    }

    @Override
    @CacheEvict(key = {
            "'node:stats:*'",
            "'node:page:*'",
            "'farm:state:*'",
            "'slau:state:*'",
            "'whol:state:*'",
            "'reta:state:*'",
            "'slau:pending:*'",
            "'whol:pending:*'",
            "'reta:pending:*'"
    }, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int batchDisableNodeInfo(List<Integer> nodeIds) {
        if (nodeIds == null || nodeIds.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "节点企业ID列表不能为空");
        }

        int totalResult = 0;
        for (Integer nodeId : nodeIds) {
            try {
                int result = disableNodeInfo(nodeId);
                totalResult += result;
            } catch (Exception e) {
                // 记录错误但继续处理其他节点
                log.error("禁用节点企业失败，nodeId: {}", nodeId, e);
            }
        }

        return totalResult;
    }

    @Override
    @CacheEvict(key = {
            "'node:stats:*'",
            "'node:page:*'"
    }, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int enableNodeInfo(Integer nodeId) {
        LambdaUpdateWrapper<NodeInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(NodeInfo::getNodeId, nodeId)
                .set(NodeInfo::getStatus, 0) // 启用状态
                .set(NodeInfo::getDeleteTime, null);

        return nodeInfoMapper.update(null, updateWrapper);
    }

    /**
     * 级联下架所有关联的产品批号
     */
    private void cascadeDisableProductBatches(NodeInfo nodeInfo) {
        Integer nodeId = nodeInfo.getNodeId();
        Integer nodeType = nodeInfo.getType();

        // 1. 先下架本企业的所有产品批号
        switch (nodeType) {
            case 1: // 养殖企业
                disableFarmBatches(nodeId);
                break;
            case 2: // 屠宰企业
                disableSlauBatches(nodeId);
                break;
            case 3: // 批发商
                disableWholBatches(nodeId);
                break;
            case 4: // 零售商
                disableRetaBatches(nodeId);
                break;
        }

        // 2. 级联下架所有下游企业的产品批号
        cascadeDisableDownstreamBatches(nodeId, nodeType);
    }

    /**
     * 下架养殖企业产品批号
     */
    private void disableFarmBatches(Integer farmId) {
        LambdaUpdateWrapper<FarmBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FarmBatch::getFarmId, farmId)
                .set(FarmBatch::getState, 3); // 已下架状态

        farmBatchService.update(updateWrapper);
    }

    /**
     * 下架屠宰企业产品批号
     */
    private void disableSlauBatches(Integer slauId) {
        LambdaUpdateWrapper<SlauBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SlauBatch::getSlauId, slauId)
                .set(SlauBatch::getState, 4); // 已下架状态

        slauBatchService.update(updateWrapper);
    }

    /**
     * 下架批发商产品批号
     */
    private void disableWholBatches(Integer wholId) {
        LambdaUpdateWrapper<WholBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WholBatch::getWholId, wholId)
                .set(WholBatch::getState, 4); // 已下架状态

        wholBatchService.update(updateWrapper);
    }

    /**
     * 下架零售商产品批号
     */
    private void disableRetaBatches(Integer retaId) {
        LambdaUpdateWrapper<RetaBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RetaBatch::getRetaId, retaId)
                .set(RetaBatch::getState, 4); // 已下架状态

        retaBatchService.update(updateWrapper);
    }


    /**
     * 级联下架所有下游企业的产品批号
     */
    private void cascadeDisableDownstreamBatches(Integer nodeId, Integer nodeType) {
        switch (nodeType) {
            case 1: // 养殖企业 -> 级联下架所有屠宰、批发、零售商的产品批号
                cascadeDisableFromFarm(nodeId);
                break;
            case 2: // 屠宰企业 -> 级联下架所有批发、零售商的产品批号
                cascadeDisableFromSlau(nodeId);
                break;
            case 3: // 批发商 -> 级联下架所有零售商的产品批号
                cascadeDisableFromWhol(nodeId);
                break;
            case 4: // 零售商 -> 无下游，不需要级联
                break;
        }
    }

    /**
     * 从养殖企业开始级联下架
     */
    private void cascadeDisableFromFarm(Integer farmId) {
        // 1. 下架所有直接关联的屠宰企业产品批号
        LambdaQueryWrapper<SlauBatch> slauQueryWrapper = new LambdaQueryWrapper<>();
        slauQueryWrapper.eq(SlauBatch::getFbId, farmId);
        List<SlauBatch> directSlauBatches = slauBatchService.list(slauQueryWrapper);

        for (SlauBatch slauBatch : directSlauBatches) {
            // 下架这个屠宰批号
            disableSlauBatchById(slauBatch.getSbId());

            // 继续级联下架这个屠宰批号关联的下游
            cascadeDisableFromSlauBatch(slauBatch.getSbId());
        }
    }

    /**
     * 从屠宰企业开始级联下架
     */
    private void cascadeDisableFromSlau(Integer slauId) {
        // 1. 下架所有直接关联的批发企业产品批号
        LambdaQueryWrapper<WholBatch> wholQueryWrapper = new LambdaQueryWrapper<>();
        wholQueryWrapper.eq(WholBatch::getSbId, slauId);
        List<WholBatch> directWholBatches = wholBatchService.list(wholQueryWrapper);

        for (WholBatch wholBatch : directWholBatches) {
            // 下架这个批发批号
            disableWholBatchById(wholBatch.getWbId());

            // 继续级联下架这个批发批号关联的下游
            cascadeDisableFromWholBatch(wholBatch.getWbId());
        }
    }

    /**
     * 从批发企业开始级联下架
     */
    private void cascadeDisableFromWhol(Integer wholId) {
        // 下架所有直接关联的零售企业产品批号
        LambdaQueryWrapper<RetaBatch> retaQueryWrapper = new LambdaQueryWrapper<>();
        retaQueryWrapper.eq(RetaBatch::getWbId, wholId);
        List<RetaBatch> directRetaBatches = retaBatchService.list(retaQueryWrapper);

        for (RetaBatch retaBatch : directRetaBatches) {
            // 下架这个零售批号
            disableRetaBatchById(retaBatch.getRbId());
        }
    }

    /**
     * 从屠宰批号开始级联下架
     */
    private void cascadeDisableFromSlauBatch(Integer sbId) {
        // 下架所有关联的批发企业产品批号
        LambdaQueryWrapper<WholBatch> wholQueryWrapper = new LambdaQueryWrapper<>();
        wholQueryWrapper.eq(WholBatch::getSbId, sbId);
        List<WholBatch> wholBatches = wholBatchService.list(wholQueryWrapper);

        for (WholBatch wholBatch : wholBatches) {
            disableWholBatchById(wholBatch.getWbId());
            cascadeDisableFromWholBatch(wholBatch.getWbId());
        }
    }

    /**
     * 从批发批号开始级联下架
     */
    private void cascadeDisableFromWholBatch(Integer wbId) {
        // 下架所有关联的零售企业产品批号
        LambdaQueryWrapper<RetaBatch> retaQueryWrapper = new LambdaQueryWrapper<>();
        retaQueryWrapper.eq(RetaBatch::getWbId, wbId);
        List<RetaBatch> retaBatches = retaBatchService.list(retaQueryWrapper);

        for (RetaBatch retaBatch : retaBatches) {
            disableRetaBatchById(retaBatch.getRbId());
        }
    }

    /**
     * 根据批号ID下架单个屠宰批号
     */
    private void disableSlauBatchById(Integer sbId) {
        LambdaUpdateWrapper<SlauBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SlauBatch::getSbId, sbId)
                .set(SlauBatch::getState, 4); // 已下架状态
        slauBatchService.update(updateWrapper);
    }

    /**
     * 根据批号ID下架单个批发批号
     */
    private void disableWholBatchById(Integer wbId) {
        LambdaUpdateWrapper<WholBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WholBatch::getWbId, wbId)
                .set(WholBatch::getState, 4); // 已下架状态
        wholBatchService.update(updateWrapper);
    }

    /**
     * 根据批号ID下架单个零售批号
     */
    private void disableRetaBatchById(Integer rbId) {
        LambdaUpdateWrapper<RetaBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RetaBatch::getRbId, rbId)
                .set(RetaBatch::getState, 4); // 已下架状态
        retaBatchService.update(updateWrapper);
    }

    @Override
    @CacheEvict(key = "'node:disabled:list'", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int permanentlyDeleteNodeInfo(Integer nodeId) {
        if (nodeId == null || nodeId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "节点企业ID不能为空");
        }

        // 1. 先检查节点企业是否存在且处于禁用状态
        NodeInfo nodeInfo = getById(nodeId);
        if (nodeInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "节点企业不存在");
        }

        if (nodeInfo.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只能删除处于禁用状态的节点企业");
        }

        // 2. 级联删除所有关联的下游产品批号
        cascadeDeleteProductBatches(nodeInfo);

        // 3. 执行物理删除
        return nodeInfoMapper.deleteById(nodeId);
    }

    /**
     * 级联删除所有关联的下游产品批号
     */
    private void cascadeDeleteProductBatches(NodeInfo nodeInfo) {
        Integer nodeId = nodeInfo.getNodeId();
        Integer nodeType = nodeInfo.getType();

        // 根据企业类型删除对应的产品批号
        switch (nodeType) {
            case 1: // 养殖企业
                deleteFarmBatchesAndDownstream(nodeId);
                break;
            case 2: // 屠宰企业
                deleteSlauBatchesAndDownstream(nodeId);
                break;
            case 3: // 批发商
                deleteWholBatchesAndDownstream(nodeId);
                break;
            case 4: // 零售商
                deleteRetaBatches(nodeId);
                break;
        }
    }

    /**
     * 删除养殖企业批号及所有下游批号
     */
    private void deleteFarmBatchesAndDownstream(Integer farmId) {
        // 1. 查询该养殖企业的所有批号
        LambdaQueryWrapper<FarmBatch> farmQuery = new LambdaQueryWrapper<>();
        farmQuery.eq(FarmBatch::getFarmId, farmId);
        List<FarmBatch> farmBatches = farmBatchService.list(farmQuery);

        for (FarmBatch farmBatch : farmBatches) {
            // 2. 删除关联的屠宰企业批号及下游
            deleteSlauBatchesByFarmBatch(farmBatch.getFbId());

            // 3. 删除养殖企业批号
            farmBatchService.removeById(farmBatch.getFbId());
        }
    }

    /**
     * 删除屠宰企业批号及所有下游批号
     */
    private void deleteSlauBatchesAndDownstream(Integer slauId) {
        // 1. 查询该屠宰企业的所有批号
        LambdaQueryWrapper<SlauBatch> slauQuery = new LambdaQueryWrapper<>();
        slauQuery.eq(SlauBatch::getSlauId, slauId);
        List<SlauBatch> slauBatches = slauBatchService.list(slauQuery);

        for (SlauBatch slauBatch : slauBatches) {
            // 2. 删除关联的批发商批号及下游
            deleteWholBatchesBySlauBatch(slauBatch.getSbId());

            // 3. 删除屠宰企业批号
            slauBatchService.removeById(slauBatch.getSbId());
        }
    }

    /**
     * 删除批发商批号及所有下游批号
     */
    private void deleteWholBatchesAndDownstream(Integer wholId) {
        // 1. 查询该批发商的所有批号
        LambdaQueryWrapper<WholBatch> wholQuery = new LambdaQueryWrapper<>();
        wholQuery.eq(WholBatch::getWholId, wholId);
        List<WholBatch> wholBatches = wholBatchService.list(wholQuery);

        for (WholBatch wholBatch : wholBatches) {
            // 2. 删除关联的零售商批号
            deleteRetaBatchesByWholBatch(wholBatch.getWbId());

            // 3. 删除批发商批号
            wholBatchService.removeById(wholBatch.getWbId());
        }
    }

    /**
     * 删除零售商批号
     */
    private void deleteRetaBatches(Integer retaId) {
        LambdaQueryWrapper<RetaBatch> retaQuery = new LambdaQueryWrapper<>();
        retaQuery.eq(RetaBatch::getRetaId, retaId);
        retaBatchService.remove(retaQuery);
    }

    /**
     * 根据养殖批号ID删除关联的屠宰批号及下游
     */
    private void deleteSlauBatchesByFarmBatch(Integer fbId) {
        LambdaQueryWrapper<SlauBatch> slauQuery = new LambdaQueryWrapper<>();
        slauQuery.eq(SlauBatch::getFbId, fbId);
        List<SlauBatch> slauBatches = slauBatchService.list(slauQuery);

        for (SlauBatch slauBatch : slauBatches) {
            // 删除关联的批发商批号及下游
            deleteWholBatchesBySlauBatch(slauBatch.getSbId());
            // 删除屠宰批号
            slauBatchService.removeById(slauBatch.getSbId());
        }
    }

    /**
     * 根据屠宰批号ID删除关联的批发批号及下游
     */
    private void deleteWholBatchesBySlauBatch(Integer sbId) {
        LambdaQueryWrapper<WholBatch> wholQuery = new LambdaQueryWrapper<>();
        wholQuery.eq(WholBatch::getSbId, sbId);
        List<WholBatch> wholBatches = wholBatchService.list(wholQuery);

        for (WholBatch wholBatch : wholBatches) {
            // 删除关联的零售商批号
            deleteRetaBatchesByWholBatch(wholBatch.getWbId());
            // 删除批发批号
            wholBatchService.removeById(wholBatch.getWbId());
        }
    }

    /**
     * 根据批发批号ID删除关联的零售批号
     */
    private void deleteRetaBatchesByWholBatch(Integer wbId) {
        LambdaQueryWrapper<RetaBatch> retaQuery = new LambdaQueryWrapper<>();
        retaQuery.eq(RetaBatch::getWbId, wbId);
        retaBatchService.remove(retaQuery);
    }
}