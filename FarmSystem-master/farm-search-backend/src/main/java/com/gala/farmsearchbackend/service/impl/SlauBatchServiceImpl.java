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
import com.gala.farmsearchbackend.mapper.SlauBatchMapper;
import com.gala.farmsearchbackend.model.domain.FarmBatch;
import com.gala.farmsearchbackend.model.domain.NodeInfo;
import com.gala.farmsearchbackend.model.domain.SlauBatch;
import com.gala.farmsearchbackend.model.domain.WholBatch;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.slauDto.*;
import com.gala.farmsearchbackend.model.dto.wholDto.WholNameDto;
import com.gala.farmsearchbackend.service.ConfirmRequestService;
import com.gala.farmsearchbackend.service.FarmBatchService;
import com.gala.farmsearchbackend.service.SlauBatchService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 屠宰企业产品批号信息服务实现
 * @author Gala
 * @description 针对表【slau_batch(屠宰企业产品批号信息表)】的数据库操作Service实现
 * @createDate 2025-09-20 20:09:16
 */
@Service
public class SlauBatchServiceImpl extends ServiceImpl<SlauBatchMapper, SlauBatch>
        implements SlauBatchService {

    @Resource
    private SlauBatchMapper slauBatchMapper;

    @Resource
    private NodeInfoMapper nodeInfoMapper;

    @Resource
    private ConfirmRequestService confirmRequestService;
    @Resource
    @Lazy
    private FarmBatchService farmBatchService;

    @Resource
    private WholBatchServiceImpl wholBatchService;
    @Override
    public SlauBatch getSlauBatchBySlauIdByBatchId(Integer slauId, String batchId) {
        LambdaQueryWrapper<SlauBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SlauBatch::getSlauId, slauId)
                .eq(SlauBatch::getBatchId, batchId);
        return getOne(queryWrapper);
    }

    @Override
    @CacheEvict(key = "'slau:state:*'", allEntries = true)
    public int saveSlauBatch(SlauBatch slauBatch) {
        return slauBatchMapper.insert(slauBatch);
    }

    @Override
    public SlauBatch getSlauBatchById(Integer sbId) {
        return slauBatchMapper.selectById(sbId);
    }

    @Override
    @CacheEvict(key = "'slau:detail:' + #slauBatch.sbId", allEntries = true)
    public int editSlauBatch(SlauBatch slauBatch) {
        if (slauBatch.getSbId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "批次ID不能为空");
        }

        // 使用 LambdaUpdateWrapper 只更新非空字段
        LambdaUpdateWrapper<SlauBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SlauBatch::getSbId, slauBatch.getSbId());

        updateWrapper
                .set(slauBatch.getBatchId() != null, SlauBatch::getBatchId, slauBatch.getBatchId())
                .set(slauBatch.getFbId() != null, SlauBatch::getFbId, slauBatch.getFbId())
                .set(slauBatch.getType() != null, SlauBatch::getType, slauBatch.getType())
                .set(slauBatch.getQuaId() != null, SlauBatch::getQuaId, slauBatch.getQuaId())
                .set(slauBatch.getTestName() != null, SlauBatch::getTestName, slauBatch.getTestName())
                .set(slauBatch.getRemarks() != null, SlauBatch::getRemarks, slauBatch.getRemarks())
                .set(slauBatch.getState() != null, SlauBatch::getState, slauBatch.getState());
        // 不设置 batchDate，保持原值

        return slauBatchMapper.update(null, updateWrapper);
    }
    @Override
    @CacheEvict(key = {
            "'slau:state:*'",
            "'slau:detail:' + #sbId",
    }, allEntries = true)
    public int removeSlauBatchById(Integer sbId) {
        return slauBatchMapper.deleteById(sbId);
    }

    @Override
    @CacheEvict(key = {"'slau:state:*'", "'slau:detail:' + #sbId", "'slau:pending:*'"}, allEntries = true)
    public int confirmSlauBatch(Integer sbId, String confirm) {
        // 状态3: 已确认
        return slauBatchMapper.updateSlauBatchState(sbId, 3, confirm);
    }

    @Override
    public List<SlaughterhouseDto> getSlaughterhousesByArea(Integer provId, Integer cityId) {
        LambdaQueryWrapper<NodeInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NodeInfo::getType, 2) // 屠宰企业类型
                .eq(NodeInfo::getProvId, provId)
                .eq(NodeInfo::getCityId, cityId)
                .orderByAsc(NodeInfo::getName);

        List<NodeInfo> nodeInfos = nodeInfoMapper.selectList(queryWrapper);

        return nodeInfos.stream().map(nodeInfo -> {
            SlaughterhouseDto dto = new SlaughterhouseDto();
            dto.setNodeId(nodeInfo.getNodeId());
            dto.setName(nodeInfo.getName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SlauBatchSimpleDto> getSlauBatchesBySlauId(Integer slauId) {
        LambdaQueryWrapper<SlauBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SlauBatch::getSlauId, slauId)
                .eq(SlauBatch::getState, 3) // 已确认状态的产品批号
                .orderByDesc(SlauBatch::getBatchDate);

        List<SlauBatch> slauBatches = slauBatchMapper.selectList(queryWrapper);

        return slauBatches.stream().map(slauBatch -> {
            SlauBatchSimpleDto dto = new SlauBatchSimpleDto();
            dto.setSbId(slauBatch.getSbId());
            dto.setBatchId(slauBatch.getBatchId());
            dto.setType(slauBatch.getType());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 根据屠宰产品批号ID查询详情
     */
    @Override
    public SlauBatchDetailDto getSlauBatchDetailById(Integer sbId) {
        if (sbId == null || sbId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "屠宰产品批号ID不能为空");
        }

        SlauBatchDetailDto detailDto = slauBatchMapper.selectSlauBatchDetailById(sbId);
        if (detailDto == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "屠宰产品批号信息不存在");
        }

        return detailDto;
    }

    /**
     * 屠宰企业发送确认请求给养殖企业
     */
    @Override
    @RateLimit(key = "confirm_request:", time = 60, count = 10, limitType = RateLimit.LimitType.USER)
    @CacheEvict(key = {"'slau:state:*'", "'slau:pending:*'"}, allEntries = true) // 清理屠宰批号状态缓存
    @Transactional
    public void sendConfirmToFarm(Integer sbId) {
        SlauBatch slauBatch = getById(sbId);
        if (slauBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "屠宰批号信息不存在");
        }

        FarmBatch farmBatch = farmBatchService.getById(slauBatch.getFbId());
        if (farmBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "养殖批号信息不存在");
        }

        // 发送确认请求
        confirmRequestService.sendConfirmRequest(
                slauBatch.getSlauId(),      // 屠宰企业ID
                farmBatch.getFarmId(),      // 养殖企业ID
                sbId,                       // 屠宰批号记录ID
                "SLAU"                      // 记录类型
        );
    }

    @Override
    @Cacheable(key = "'slau:detail:' + #sbId", expire = 600)
    public SlauDetailsDto getSlauDetailById(Integer sbId) {
        if (sbId == null || sbId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "屠宰产品批号ID不能为空");
        }

        SlauDetailsDto detailDto = slauBatchMapper.selectSlauDetailById(sbId);
        if (detailDto == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "屠宰产品批号信息不存在");
        }

        return detailDto;
    }

    @Override
    @Cacheable(key = "'slau:state:' + #queryDto.hashCode()", expire = 300)
    public List<SlauStateDto> getSlauBatchByState(SlauQueryDto queryDto) {
        // 设置默认排除下架状态
        if (queryDto.getState() != null && queryDto.getState() == BatchStateConstants.Other.OFFLINE) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能查询已下架的产品批号");
        }
        return slauBatchMapper.selectSlauBatchByState(queryDto);
    }

    @Override
    public List<WholNameDto> getPendingWholBatchByWholName(String wholName) {
        return wholBatchService.getPendingWholBatchByWholNameWithNode(wholName);
    }

    /**
     * 查询屠宰企业的待确认状态产品批号列表（包含节点名称）
     */
    @Cacheable(key = "'slau:pending:' + #slauName", expire = 300)
    public List<SlauNameDto> getPendingSlauBatchBySlauNameWithNode(String slauName) {
        return slauBatchMapper.selectPendingSlauBatchBySlauNameWithNode(slauName);
    }

    @Override
    @CacheEvict(key = {"'slau:state:*'", "'slau:detail:' + #sbId", "'whol:state:*'", "'reta:state:*'"}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int offlineSlauBatch(Integer sbId) {
        SlauBatch slauBatch = getById(sbId);
        if (slauBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");
        }

        // 更新屠宰批号状态为下架（4）
        LambdaUpdateWrapper<SlauBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SlauBatch::getSbId, sbId)
                .set(SlauBatch::getState, 4); // 下架状态

        int result = baseMapper.update(null, updateWrapper);

        if (result > 0) {
            // 级联下架所有关联的批发商批号
            cascadeOfflineWholBatches(sbId);
        }

        return result;
    }

    /**
     * 级联下架关联的批发商批号
     */
    private void cascadeOfflineWholBatches(Integer sbId) {
        // 查询所有关联的批发商批号
        LambdaQueryWrapper<WholBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WholBatch::getSbId, sbId)
                .ne(WholBatch::getState, 4); // 排除已经是下架状态的

        List<WholBatch> wholBatches = wholBatchService.list(queryWrapper);

        for (WholBatch wholBatch : wholBatches) {
            // 调用批发商批号的下架方法，会继续级联下架下游
            wholBatchService.offlineWholBatch(wholBatch.getWbId());
        }
    }

    @Override
    public NodeInfoDetailsDto getSlauInfoById(Integer slauId) {
        if (slauId == null || slauId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "屠宰企业ID不能为空");
        }

        NodeInfoDetailsDto slauInfo = nodeInfoMapper.selectNodeInfoDetailById(slauId);
        if (slauInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "屠宰企业信息不存在");
        }

        return slauInfo;
    }

    @Override
    public List<String> getDownstreamWholesalerNames(Integer slauId) {
        return slauBatchMapper.selectDownstreamWholesalerNames(slauId);
    }
}