package com.gala.farmsearchbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gala.farmsearchbackend.annotation.CacheEvict;
import com.gala.farmsearchbackend.annotation.Cacheable;
import com.gala.farmsearchbackend.constants.BatchStateConstants;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.mapper.FarmBatchMapper;
import com.gala.farmsearchbackend.mapper.NodeInfoMapper;
import com.gala.farmsearchbackend.model.domain.FarmBatch;
import com.gala.farmsearchbackend.model.domain.NodeInfo;
import com.gala.farmsearchbackend.model.domain.SlauBatch;
import com.gala.farmsearchbackend.model.dto.farmDto.*;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.slauDto.SlauNameDto;
import com.gala.farmsearchbackend.service.FarmBatchService;

import com.gala.farmsearchbackend.service.SlauBatchService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author Gala
* @description 针对表【farm_batch(养殖企业产品批号信息表)】的数据库操作Service实现
* @createDate 2025-09-20 20:08:35
*/
@Service
public class FarmBatchServiceImpl extends ServiceImpl<FarmBatchMapper, FarmBatch>
    implements FarmBatchService {
    @Resource
    private FarmBatchMapper farmBatchMapper;

    @Resource
    private NodeInfoMapper nodeInfoMapper;

    @Resource
    private SlauBatchServiceImpl slauBatchService;

    @Override
    public FarmBatch getFarmBatchByFarmIdByBatchId(Integer farmId, String batchId) {
        LambdaQueryWrapper<FarmBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FarmBatch::getFarmId, farmId)
                .eq(FarmBatch::getBatchId, batchId);
        return getOne(queryWrapper);
    }

    @Override
    @CacheEvict(key = "'farm:state:*'", allEntries = true)
    public int saveFarmBatch(FarmBatch farmBatch) {
        return farmBatchMapper.insert(farmBatch);
    }

    @Override
    public FarmBatch getFarmBatchById(Integer fbId) {
        return farmBatchMapper.selectById(fbId);
    }

    @Override
    @CacheEvict(key = {"'farm:state:*'", "'farm:detail:' + #farmBatch.fbId"}, allEntries = true)
    public int editFarmBatch(FarmBatch farmBatch) {
        if (farmBatch.getFbId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "批次ID不能为空");
        }

        // 使用 LambdaUpdateWrapper 只更新非空字段
        LambdaUpdateWrapper<FarmBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FarmBatch::getFbId, farmBatch.getFbId());

        updateWrapper
                .set(farmBatch.getBatchId() != null, FarmBatch::getBatchId, farmBatch.getBatchId())
                .set(farmBatch.getType() != null, FarmBatch::getType, farmBatch.getType())
                .set(farmBatch.getAgcId() != null, FarmBatch::getAgcId, farmBatch.getAgcId())
                .set(farmBatch.getTestName() != null, FarmBatch::getTestName, farmBatch.getTestName())
                .set(farmBatch.getRemarks() != null, FarmBatch::getRemarks, farmBatch.getRemarks())
                .set(farmBatch.getState() != null, FarmBatch::getState, farmBatch.getState());
        // 不设置 batch_date，保持原值

        return farmBatchMapper.update(null, updateWrapper);
    }

    @Override
    @CacheEvict(key = {
            "'farm:state:*'",
            "'farm:detail:' + #fbId",
    }, allEntries = true)
    public int removeFarmBatchById(Integer fbId) {
        return farmBatchMapper.deleteById(fbId);
    }

    @Override
    public List<FarmDto> getFarmsByArea(Integer provId, Integer cityId) {
        LambdaQueryWrapper<NodeInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NodeInfo::getType, 1) // 养殖企业类型
                .eq(NodeInfo::getProvId, provId)
                .eq(NodeInfo::getCityId, cityId)
                .orderByAsc(NodeInfo::getName);

        List<NodeInfo> nodeInfos = nodeInfoMapper.selectList(queryWrapper);

        return nodeInfos.stream().map(nodeInfo -> {
            FarmDto dto = new FarmDto();
            dto.setNodeId(nodeInfo.getNodeId());
            dto.setName(nodeInfo.getName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<FarmBatchSimpleDto> getFarmBatchesByFarmId(Integer farmId) {
        LambdaQueryWrapper<FarmBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FarmBatch::getFarmId, farmId)
                .eq(FarmBatch::getState, 2) // 已发布状态的产品批号
                .orderByDesc(FarmBatch::getBatchDate);

        List<FarmBatch> farmBatches = farmBatchMapper.selectList(queryWrapper);

        return farmBatches.stream().map(farmBatch -> {
            FarmBatchSimpleDto dto = new FarmBatchSimpleDto();
            dto.setFbId(farmBatch.getFbId());
            dto.setBatchId(farmBatch.getBatchId());
            dto.setType(farmBatch.getType());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 根据养殖产品批号ID查询简略信息
     */
    @Override
    public FarmSimpleDto getFarmBatchSimpleById(Integer fbId) {
        if (fbId == null || fbId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "养殖产品批号ID不能为空");
        }

        FarmSimpleDto simpleDto = farmBatchMapper.selectFarmBatchSimpleById(fbId);
        if (simpleDto == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "养殖产品批号信息不存在");
        }

        return simpleDto;
    }

    @Override
    @Cacheable(key = "'farm:detail:' + #fbId", expire = 600)
    public FarmDetailsDto getFarmDetailById(Integer fbId) {
        if (fbId == null || fbId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "养殖产品批号ID不能为空");
        }

        FarmDetailsDto detailDto = farmBatchMapper.selectFarmDetailById(fbId);
        if (detailDto == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "养殖产品批号信息不存在");
        }

        return detailDto;
    }

    @Override
    @Cacheable(key = "'farm:state:' + #queryDto.hashCode()", expire = 300)
    public List<FarmStateDto> getFarmBatchByState(FarmQueryDto queryDto) {
        // 设置默认排除下架状态
        if (queryDto.getState() != null && queryDto.getState() == BatchStateConstants.Farm.OFFLINE) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能查询已下架的产品批号");
        }
        return farmBatchMapper.selectFarmBatchByState(queryDto);
    }

    @Override
    public List<SlauNameDto> getPendingSlauBatchBySlauName(String slauName) {
        return slauBatchService.getPendingSlauBatchBySlauNameWithNode(slauName);
    }

    @Override
    @CacheEvict(key = {"'farm:state:*'", "'farm:detail:' + #fbId", "'slau:state:*'", "'whol:state:*'", "'reta:state:*'"}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int offlineFarmBatch(Integer fbId) {
        FarmBatch farmBatch = getById(fbId);
        if (farmBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");
        }

        // 更新养殖批号状态为下架（3）
        LambdaUpdateWrapper<FarmBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FarmBatch::getFbId, fbId)
                .set(FarmBatch::getState, 3); // 下架状态

        int result = baseMapper.update(null, updateWrapper);

        if (result > 0) {
            // 级联下架所有关联的屠宰批号
            cascadeOfflineSlauBatches(fbId);
        }

        return result;
    }

    /**
     * 级联下架关联的屠宰批号
     */
    private void cascadeOfflineSlauBatches(Integer fbId) {
        // 查询所有关联的屠宰批号
        LambdaQueryWrapper<SlauBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SlauBatch::getFbId, fbId)
                .ne(SlauBatch::getState, 4); // 排除已经是下架状态的

        List<SlauBatch> slauBatches = slauBatchService.list(queryWrapper);

        for (SlauBatch slauBatch : slauBatches) {
            // 调用屠宰批号的下架方法，会继续级联下架下游
            slauBatchService.offlineSlauBatch(slauBatch.getSbId());
        }
    }

    @Override
    public NodeInfoDetailsDto getFarmInfoById(Integer farmId) {
        if (farmId == null || farmId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "养殖企业ID不能为空");
        }

        NodeInfoDetailsDto farmInfo = nodeInfoMapper.selectNodeInfoDetailById(farmId);
        if (farmInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "养殖企业信息不存在");
        }

        return farmInfo;
    }

    @Override
    public List<String> getDownstreamSlaughterhouseNames(Integer farmId) {
        return farmBatchMapper.selectDownstreamSlaughterhouseNames(farmId);
    }
}




