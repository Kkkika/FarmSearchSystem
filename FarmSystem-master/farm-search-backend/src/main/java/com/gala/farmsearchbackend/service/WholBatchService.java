package com.gala.farmsearchbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gala.farmsearchbackend.model.domain.WholBatch;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.retaDto.RetaNameDto;
import com.gala.farmsearchbackend.model.dto.wholDto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 批发商产品批号信息服务接口
 * @author Gala
 * @description 针对表【whol_batch(批发商产品批号信息表)】的数据库操作Service
 * @createDate 2025-09-20 20:09:26
 */
public interface WholBatchService extends IService<WholBatch> {

    /**
     * 根据批发商ID和批号ID获取产品批号信息
     * @param wholId 批发商ID
     * @param batchId 批号ID
     * @return 产品批号信息
     */
    WholBatch getWholBatchByWholIdByBatchId(Integer wholId, String batchId);

    /**
     * 保存批发商产品批号信息
     * @param wholBatch 产品批号信息
     * @return 保存结果
     */
    int saveWholBatch(WholBatch wholBatch);

    /**
     * 根据ID获取产品批号信息
     * @param wbId 产品批号ID
     * @return 产品批号信息
     */
    WholBatch getWholBatchById(Integer wbId);

    /**
     * 编辑产品批号信息
     * @param wholBatch 产品批号信息
     * @return 更新结果
     */
    int editWholBatch(WholBatch wholBatch);

    /**
     * 根据ID删除产品批号信息
     * @param wbId 产品批号ID
     * @return 删除结果
     */
    int removeWholBatchById(Integer wbId);

    /**
     * 确认批发商产品批号
     * @param wbId 产品批号ID
     * @param confirm 确认备注
     * @return 更新结果
     */
    int confirmWholBatch(Integer wbId, String confirm);

    /**
     * 根据省市查询批发商信息
     */
    List<WholesalerDto> getWholesalersByArea(Integer provId, Integer cityId);

    /**
     * 根据批发商ID查询产品批号简单信息
     */
    List<WholBatchSimpleDto> getWholBatchesByWholId(Integer wholId);

    /**
     * 根据批发商产品批号ID查询详情
     * @param wbId 批发商产品批号ID
     * @return 批发商产品批号详情DTO
     */
    WholBatchDetailDto getWholBatchDetailById(Integer wbId);

    @Transactional
    void sendConfirmToSlau(Integer wbId);

    /**
     * 根据批发商产品批号ID查询详情
     */
    WholDetailsDto getWholDetailById(Integer wbId);

    /**
     * 根据状态查询批发商产品批号列表
     * @param queryDto 查询条件
     * @return 产品批号列表
     */
    List<WholStateDto> getWholBatchByState(WholQueryDto queryDto);

    /**
     * 查询零售商的待确认状态产品批号列表
     * @param retaName 零售商名称（可选）
     * @return 产品批号列表
     */
    List<RetaNameDto> getPendingRetaBatchByRetaName(String retaName);

    /**
     * 下架批发商产品批号
     * @param wbId 产品批号ID
     * @return 更新结果
     */
    int offlineWholBatch(Integer wbId);

    /**
     * 根据批发商ID查询企业详情
     * @param wholId 批发商ID
     * @return 企业详情
     */
    NodeInfoDetailsDto getWholInfoById(Integer wholId);

    /**
     * 根据批发商ID获取下游零售商企业名称列表
     */
    List<String> getDownstreamRetailerNames(Integer wholId);
}