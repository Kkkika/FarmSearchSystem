package com.gala.farmsearchbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gala.farmsearchbackend.annotation.CacheEvict;
import com.gala.farmsearchbackend.annotation.Cacheable;
import com.gala.farmsearchbackend.annotation.RateLimit;
import com.gala.farmsearchbackend.constants.BatchStateConstants;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.mapper.NodeInfoMapper;
import com.gala.farmsearchbackend.mapper.WholBatchMapper;
import com.gala.farmsearchbackend.model.domain.NodeInfo;
import com.gala.farmsearchbackend.model.domain.RetaBatch;
import com.gala.farmsearchbackend.model.domain.SlauBatch;
import com.gala.farmsearchbackend.model.domain.WholBatch;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.retaDto.RetaNameDto;
import com.gala.farmsearchbackend.model.dto.wholDto.*;
import com.gala.farmsearchbackend.service.ConfirmRequestService;
import com.gala.farmsearchbackend.service.SlauBatchService;
import com.gala.farmsearchbackend.service.WholBatchService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 批发商产品批号信息服务实现
 * @author Gala
 * @description 针对表【whol_batch(批发商产品批号信息表)】的数据库操作Service实现
 * @createDate 2025-09-20 20:09:26
 */
@Service
public class WholBatchServiceImpl extends ServiceImpl<WholBatchMapper, WholBatch>
        implements WholBatchService {

    @Resource
    private WholBatchMapper wholBatchMapper;
    @Resource
    private NodeInfoMapper nodeInfoMapper;

    @Resource
    private ConfirmRequestService confirmRequestService;
    @Resource
    @Lazy
    private SlauBatchService slauBatchService;

    @Resource
    private RetaBatchServiceImpl retaBatchService;
    @Override
    public WholBatch getWholBatchByWholIdByBatchId(Integer wholId, String batchId) {
        LambdaQueryWrapper<WholBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WholBatch::getWholId, wholId)
                .eq(WholBatch::getBatchId, batchId);
        return getOne(queryWrapper);
    }

    @Override
    @CacheEvict(key = "'whol:state:*'", allEntries = true)
    public int saveWholBatch(WholBatch wholBatch) {
        return wholBatchMapper.insert(wholBatch);
    }

    @Override
    public WholBatch getWholBatchById(Integer wbId) {
        return wholBatchMapper.selectById(wbId);
    }

    @Override
    @CacheEvict(key = "'whol:detail:' + #wholBatch.wbId", allEntries = true)
    public int editWholBatch(WholBatch wholBatch) {
        if (wholBatch.getWbId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "产品批号ID不能为空");
        }

        // 使用 LambdaUpdateWrapper 只更新非空字段
        LambdaUpdateWrapper<WholBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WholBatch::getWbId, wholBatch.getWbId());

        updateWrapper
                .set(wholBatch.getBatchId() != null, WholBatch::getBatchId, wholBatch.getBatchId())
                .set(wholBatch.getWholId() != null, WholBatch::getWholId, wholBatch.getWholId())
                .set(wholBatch.getSbId() != null, WholBatch::getSbId, wholBatch.getSbId())
                .set(wholBatch.getType() != null, WholBatch::getType, wholBatch.getType())
                .set(wholBatch.getRemarks() != null, WholBatch::getRemarks, wholBatch.getRemarks())
                .set(wholBatch.getState() != null, WholBatch::getState, wholBatch.getState());
        // 不设置 batchDate，保持原值

        return wholBatchMapper.update(null, updateWrapper);
    }

    @Override
    @CacheEvict(key = {
            "'whol:state:*'",
            "'whol:detail:' + #wbId",
    }, allEntries = true)
    public int removeWholBatchById(Integer wbId) {
        return wholBatchMapper.deleteById(wbId);
    }

    @Override
    @CacheEvict(key = {"'whol:state:*'", "'whol:detail:' + #wbId", "'whol:pending:*'"}, allEntries = true)
    public int confirmWholBatch(Integer wbId, String confirm) {
        // 状态3: 已确认
        return wholBatchMapper.updateWholBatchState(wbId, 3, confirm);
    }

    @Override
    public List<WholesalerDto> getWholesalersByArea(Integer provId, Integer cityId) {
        LambdaQueryWrapper<NodeInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NodeInfo::getType, 3) // 批发商企业类型
                .eq(NodeInfo::getProvId, provId)
                .eq(NodeInfo::getCityId, cityId)
                .orderByAsc(NodeInfo::getName);

        List<NodeInfo> nodeInfos = nodeInfoMapper.selectList(queryWrapper);

        return nodeInfos.stream().map(nodeInfo -> {
            WholesalerDto dto = new WholesalerDto();
            dto.setNodeId(nodeInfo.getNodeId());
            dto.setName(nodeInfo.getName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<WholBatchSimpleDto> getWholBatchesByWholId(Integer wholId) {
        LambdaQueryWrapper<WholBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WholBatch::getWholId, wholId)
                .eq(WholBatch::getState, 3) // 已确认状态的产品批号
                .orderByDesc(WholBatch::getBatchDate);

        List<WholBatch> wholBatches = wholBatchMapper.selectList(queryWrapper);

        return wholBatches.stream().map(wholBatch -> {
            WholBatchSimpleDto dto = new WholBatchSimpleDto();
            dto.setWbId(wholBatch.getWbId());
            dto.setBatchId(wholBatch.getBatchId());
            dto.setType(wholBatch.getType());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 根据批发商产品批号ID查询详情
     */
    @Override
    public WholBatchDetailDto getWholBatchDetailById(Integer wbId) {
        if (wbId == null || wbId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "批发商产品批号ID不能为空");
        }

        WholBatchDetailDto detailDto = wholBatchMapper.selectWholBatchDetailById(wbId);
        if (detailDto == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "批发商产品批号信息不存在");
        }

        return detailDto;
    }

    /**
     * 批发商发送确认请求给屠宰企业
     */
    @Override
    @RateLimit(key = "confirm_request:", time = 60, count = 10, limitType = RateLimit.LimitType.USER)
    @CacheEvict(key = {"'whol:state:*'", "'whol:pending:*'"}, allEntries = true) // 清理批发批号状态
    @Transactional
    public void sendConfirmToSlau(Integer wbId) {
        WholBatch wholBatch = getById(wbId);
        if (wholBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "批发商批号信息不存在");
        }

        SlauBatch slauBatch = slauBatchService.getById(wholBatch.getSbId());
        if (slauBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "屠宰批号信息不存在");
        }

        // 发送确认请求
        confirmRequestService.sendConfirmRequest(
                wholBatch.getWholId(),      // 批发商ID
                slauBatch.getSlauId(),      // 屠宰企业ID
                wbId,                       // 批发批号记录ID
                "WHOL"                      // 记录类型
        );
    }

    @Override
    @Cacheable(key = "'whol:detail:' + #wbId", expire = 600)
    public WholDetailsDto getWholDetailById(Integer wbId) {
        if (wbId == null || wbId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "批发商产品批号ID不能为空");
        }

        WholDetailsDto detailDto = wholBatchMapper.selectWholDetailById(wbId);
        if (detailDto == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "批发商产品批号信息不存在");
        }

        return detailDto;
    }

    @Override
    @Cacheable(key = "'whol:state:' + #queryDto.hashCode()", expire = 300)
    public List<WholStateDto> getWholBatchByState(WholQueryDto queryDto) {
        // 设置默认排除下架状态
        if (queryDto.getState() != null && queryDto.getState() == BatchStateConstants.Other.OFFLINE) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能查询已下架的产品批号");
        }
        return wholBatchMapper.selectWholBatchByState(queryDto);
    }

    @Override
    public List<RetaNameDto> getPendingRetaBatchByRetaName(String retaName) {
        return retaBatchService.getPendingRetaBatchByRetaNameWithNode(retaName);
    }

    /**
     * 查询批发商的待确认状态产品批号列表（包含节点名称）
     */
    @Cacheable(key = "'whol:pending:' + #wholName", expire = 300)
    public List<WholNameDto> getPendingWholBatchByWholNameWithNode(String wholName) {
        return wholBatchMapper.selectPendingWholBatchByWholNameWithNode(wholName);
    }

    @Override
    @CacheEvict(key = {"'whol:state:*'", "'whol:detail:' + #wbId", "'reta:state:*'"}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int offlineWholBatch(Integer wbId) {
        WholBatch wholBatch = getById(wbId);
        if (wholBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");
        }

        // 更新批发商批号状态为下架（4）
        LambdaUpdateWrapper<WholBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WholBatch::getWbId, wbId)
                .set(WholBatch::getState, 4); // 下架状态

        int result = baseMapper.update(null, updateWrapper);

        if (result > 0) {
            // 级联下架所有关联的零售商批号
            cascadeOfflineRetaBatches(wbId);
        }

        return result;
    }

    /**
     * 级联下架关联的零售商批号
     */
    private void cascadeOfflineRetaBatches(Integer wbId) {
        // 查询所有关联的零售商批号
        LambdaQueryWrapper<RetaBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RetaBatch::getWbId, wbId)
                .ne(RetaBatch::getState, 4); // 排除已经是下架状态的

        List<RetaBatch> retaBatches = retaBatchService.list(queryWrapper);

        for (RetaBatch retaBatch : retaBatches) {
            // 调用零售商批号的下架方法
            retaBatchService.offlineRetaBatch(retaBatch.getRbId());
        }
    }

    @Override
    public NodeInfoDetailsDto getWholInfoById(Integer wholId) {
        if (wholId == null || wholId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "批发商ID不能为空");
        }

        NodeInfoDetailsDto wholInfo = nodeInfoMapper.selectNodeInfoDetailById(wholId);
        if (wholInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "批发商信息不存在");
        }

        return wholInfo;
    }

    @Override
    public List<String> getDownstreamRetailerNames(Integer wholId) {
        return wholBatchMapper.selectDownstreamRetailerNames(wholId);
    }
}