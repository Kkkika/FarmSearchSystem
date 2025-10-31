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
import com.gala.farmsearchbackend.exception.GlobalExceptionHandler;
import com.gala.farmsearchbackend.mapper.NodeInfoMapper;
import com.gala.farmsearchbackend.mapper.RetaBatchMapper;
import com.gala.farmsearchbackend.model.domain.RetaBatch;
import com.gala.farmsearchbackend.model.domain.WholBatch;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.retaDto.*;
import com.gala.farmsearchbackend.service.ConfirmRequestService;
import com.gala.farmsearchbackend.service.RetaBatchService;
import com.gala.farmsearchbackend.service.TraceCodeService;
import com.gala.farmsearchbackend.service.WholBatchService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 零售商产品批号信息服务实现
 * @author Gala
 * @description 针对表【reta_batch(零售商产品批号信息表)】的数据库操作Service实现
 * @createDate 2025-09-20 20:09:09
 */
@Service
public class RetaBatchServiceImpl extends ServiceImpl<RetaBatchMapper, RetaBatch>
        implements RetaBatchService {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Resource
    private RetaBatchMapper retaBatchMapper;

    @Resource
    private ConfirmRequestService confirmRequestService;

    @Resource
    @Lazy
    private WholBatchService wholBatchService;

    @Resource
    private TraceCodeService traceCodeService;

    @Resource
    private NodeInfoMapper nodeInfoMapper;
    @Override
    public RetaBatch getRetaBatchByRetaIdByBatchId(Integer retaId, String batchId) {
        LambdaQueryWrapper<RetaBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RetaBatch::getRetaId, retaId)
                .eq(RetaBatch::getBatchId, batchId);
        return getOne(queryWrapper);
    }

    @Override
    @CacheEvict(key = "'reta:state:*'", allEntries = true)
    public int saveRetaBatch(RetaBatch retaBatch) {
        return retaBatchMapper.insert(retaBatch);
    }

    @Override
    public RetaBatch getRetaBatchById(Integer rbId) {
        return retaBatchMapper.selectById(rbId);
    }

    @Override
    @CacheEvict(key = "'reta:detail:' + #retaBatch.rbId", allEntries = true)
    public int editRetaBatch(RetaBatch retaBatch) {
        if (retaBatch.getRbId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "产品批号ID不能为空");
        }

        // 使用 LambdaUpdateWrapper 只更新非空字段
        LambdaUpdateWrapper<RetaBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RetaBatch::getRbId, retaBatch.getRbId());

        updateWrapper
                .set(retaBatch.getBatchId() != null, RetaBatch::getBatchId, retaBatch.getBatchId())
                .set(retaBatch.getRetaId() != null, RetaBatch::getRetaId, retaBatch.getRetaId())
                .set(retaBatch.getWbId() != null, RetaBatch::getWbId, retaBatch.getWbId())
                .set(retaBatch.getType() != null, RetaBatch::getType, retaBatch.getType())
                .set(retaBatch.getRemarks() != null, RetaBatch::getRemarks, retaBatch.getRemarks())
                .set(retaBatch.getState() != null, RetaBatch::getState, retaBatch.getState());
        // 不设置 createTime，保持原值

        return retaBatchMapper.update(null, updateWrapper);
    }

    @Override
    @CacheEvict(key = {
            "'reta:state:*'",
            "'reta:detail:' + #rbId",
    }, allEntries = true)
    public int removeRetaBatchById(Integer rbId) {
        return retaBatchMapper.deleteById(rbId);
    }

    /**
     * 确认零售商产品批号（批发商确认时调用）
     */
    @Override
    @CacheEvict(key = {"'reta:state:*'", "'reta:detail:' + #rbId", "'slau:pending:*'"}, allEntries = true)
    @Transactional
    public int confirmRetaBatch(Integer rbId, String confirm) {
        // 1. 更新状态为已确认
        int result = retaBatchMapper.updateRetaBatchState(rbId, 3, confirm);

        if (result > 0) {
            // 2. 生成并保存溯源码
            try {
                traceCodeService.generateAndSaveTraceCode(rbId);
            } catch (Exception e) {
                // 记录日志，但不回滚事务
                log.error("生成溯源码失败，但确认操作已完成。rbId: {}", rbId, e);
            }
        }

        return result;
    }

    /**
     * 根据零售商产品批号ID查询详情
     */
    @Override
    public RetaBatchDetailDto getRetaBatchDetailById(Integer rbId) {
        if (rbId == null || rbId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "零售商产品批号ID不能为空");
        }

        RetaBatchDetailDto detailDto = retaBatchMapper.selectRetaBatchDetailById(rbId);
        if (detailDto == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "零售商产品批号信息不存在");
        }

        return detailDto;
    }

    /**
     * 零售商发送确认请求给批发商
     */
    @Override
    @RateLimit(key = "confirm_request:", time = 60, count = 10, limitType = RateLimit.LimitType.USER)
    @CacheEvict(key = {"'reta:state:*'", "'reta:pending:*'"}, allEntries = true) // 清理零售批号状态
    @Transactional
    public void sendConfirmToWhol(Integer rbId) {
        RetaBatch retaBatch = getById(rbId);
        if (retaBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "零售商批号信息不存在");
        }

        WholBatch wholBatch = wholBatchService.getById(retaBatch.getWbId());
        if (wholBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "批发商批号信息不存在");
        }

        // 发送确认请求
        confirmRequestService.sendConfirmRequest(
                retaBatch.getRetaId(),      // 零售商ID
                wholBatch.getWholId(),      // 批发商ID
                rbId,                       // 零售批号记录ID
                "RETA"                      // 记录类型
        );
    }

    @Override
    @Cacheable(key = "'reta:detail:' + #rbId", expire = 600)
    public RetaDetailsDto getRetaDetailById(Integer rbId) {
        if (rbId == null || rbId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "零售商产品批号ID不能为空");
        }

        RetaDetailsDto detailDto = retaBatchMapper.selectRetaDetailById(rbId);
        if (detailDto == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "零售商产品批号信息不存在");
        }

        return detailDto;
    }

    @Override
    @Cacheable(key = "'reta:state:' + #queryDto.hashCode()", expire = 300)
    public List<RetaStateDto> getRetaBatchByState(RetaQueryDto queryDto) {
        // 设置默认排除下架状态
        if (queryDto.getState() != null && queryDto.getState() == BatchStateConstants.Other.OFFLINE) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能查询已下架的产品批号");
        }
        return retaBatchMapper.selectRetaBatchByState(queryDto);
    }

    /**
     * 查询零售商的待确认状态产品批号列表（包含节点名称）
     */
    @Cacheable(key = "'reta:pending:' + #retaName", expire = 300)
    public List<RetaNameDto> getPendingRetaBatchByRetaNameWithNode(String retaName) {
        return retaBatchMapper.selectPendingRetaBatchByRetaNameWithNode(retaName);
    }

    @Override
    @CacheEvict(key = {"'reta:state:*'", "'reta:detail:' + #rbId"}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public int offlineRetaBatch(Integer rbId) {
        RetaBatch retaBatch = getById(rbId);
        if (retaBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");
        }

        // 更新状态为下架（4）
        LambdaUpdateWrapper<RetaBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RetaBatch::getRbId, rbId)
                .set(RetaBatch::getState, 4); // 下架状态

        return baseMapper.update(null, updateWrapper);
    }

    @Override
    public NodeInfoDetailsDto getRetaInfoById(Integer retaId) {
        if (retaId == null || retaId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "零售商ID不能为空");
        }

        NodeInfoDetailsDto retaInfo = nodeInfoMapper.selectNodeInfoDetailById(retaId);
        if (retaInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "零售商信息不存在");
        }

        return retaInfo;
    }
}
