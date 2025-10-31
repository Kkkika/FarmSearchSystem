package com.gala.farmsearchbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gala.farmsearchbackend.model.domain.RetaBatch;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.retaDto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 零售商产品批号信息服务接口
 * @author Gala
 * @description 针对表【reta_batch(零售商产品批号信息表)】的数据库操作Service
 * @createDate 2025-09-20 20:09:09
 */
public interface RetaBatchService extends IService<RetaBatch> {

    /**
     * 根据零售商ID和批号ID获取产品批号信息
     * @param retaId 零售商ID
     * @param batchId 批号ID
     * @return 产品批号信息
     */
    RetaBatch getRetaBatchByRetaIdByBatchId(Integer retaId, String batchId);

    /**
     * 保存零售商产品批号信息
     * @param retaBatch 产品批号信息
     * @return 保存结果
     */
    int saveRetaBatch(RetaBatch retaBatch);

    /**
     * 根据ID获取产品批号信息
     * @param rbId 产品批号ID
     * @return 产品批号信息
     */
    RetaBatch getRetaBatchById(Integer rbId);

    /**
     * 编辑产品批号信息
     * @param retaBatch 产品批号信息
     * @return 更新结果
     */
    int editRetaBatch(RetaBatch retaBatch);

    /**
     * 根据ID删除产品批号信息
     * @param rbId 产品批号ID
     * @return 删除结果
     */
    int removeRetaBatchById(Integer rbId);

    /**
     * 确认零售商产品批号
     * @param rbId 产品批号ID
     * @param confirm 确认备注
     * @return 更新结果
     */
    int confirmRetaBatch(Integer rbId, String confirm);

    /**
     * 根据零售商产品批号ID查询详情
     * @param rbId 零售商产品批号ID
     * @return 零售商产品批号详情DTO
     */
    RetaBatchDetailDto getRetaBatchDetailById(Integer rbId);

    @Transactional
    void sendConfirmToWhol(Integer rbId);

    /**
     * 根据零售商产品批号ID查询详情
     */
    RetaDetailsDto getRetaDetailById(Integer rbId);

    /**
     * 根据状态查询零售商产品批号列表
     * @param queryDto 查询条件
     * @return 产品批号列表
     */
    List<RetaStateDto> getRetaBatchByState(RetaQueryDto queryDto);

    /**
     * 下架零售商产品批号
     * @param rbId 产品批号ID
     * @return 更新结果
     */
    int offlineRetaBatch(Integer rbId);

    /**
     * 根据零售商ID查询企业详情
     * @param retaId 零售商ID
     * @return 企业详情
     */
    NodeInfoDetailsDto getRetaInfoById(Integer retaId);
}