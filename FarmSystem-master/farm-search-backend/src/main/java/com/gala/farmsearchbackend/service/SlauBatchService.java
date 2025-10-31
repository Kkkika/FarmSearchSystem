package com.gala.farmsearchbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gala.farmsearchbackend.model.domain.SlauBatch;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.slauDto.*;
import com.gala.farmsearchbackend.model.dto.wholDto.WholNameDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 屠宰企业产品批号信息服务接口
 * @author Gala
 * @description 针对表【slau_batch(屠宰企业产品批号信息表)】的数据库操作Service
 * @createDate 2025-09-20 20:09:16
 */
public interface SlauBatchService extends IService<SlauBatch> {

    /**
     * 根据屠宰企业ID和批号ID获取产品批号信息
     * @param slauId 屠宰企业ID
     * @param batchId 批号ID
     * @return 产品批号信息
     */
    SlauBatch getSlauBatchBySlauIdByBatchId(Integer slauId, String batchId);

    /**
     * 保存屠宰企业产品批号信息
     * @param slauBatch 产品批号信息
     * @return 保存结果
     */
    int saveSlauBatch(SlauBatch slauBatch);

    /**
     * 根据ID获取产品批号信息
     * @param sbId 产品批号ID
     * @return 产品批号信息
     */
    SlauBatch getSlauBatchById(Integer sbId);

    /**
     * 编辑产品批号信息
     * @param slauBatch 产品批号信息
     * @return 更新结果
     */
    int editSlauBatch(SlauBatch slauBatch);

    /**
     * 根据ID删除产品批号信息
     * @param sbId 产品批号ID
     * @return 删除结果
     */
    int removeSlauBatchById(Integer sbId);

    /**
     * 确认屠宰企业产品批号（更新状态为已确认）
     * @param sbId 屠宰企业产品批号ID
     * @return 更新结果
     */
    int confirmSlauBatch(Integer sbId, String confirm);

    /**
     * 根据省市查询屠宰企业信息
     */
    List<SlaughterhouseDto> getSlaughterhousesByArea(Integer provId, Integer cityId);

    /**
     * 根据屠宰企业ID查询产品批号简单信息
     */
    List<SlauBatchSimpleDto> getSlauBatchesBySlauId(Integer slauId);

    /**
     * 根据屠宰产品批号ID查询详情
     * @param sbId 屠宰产品批号ID
     * @return 屠宰产品批号详情DTO
     */
    SlauBatchDetailDto getSlauBatchDetailById(Integer sbId);

    @Transactional
    void sendConfirmToFarm(Integer sbId);

    /**
     * 根据屠宰产品批号ID查询详情
     */
    SlauDetailsDto getSlauDetailById(Integer sbId);

    /**
     * 根据状态查询屠宰企业产品批号列表
     * @param queryDto 查询条件
     * @return 产品批号列表
     */
    List<SlauStateDto> getSlauBatchByState(SlauQueryDto queryDto);

    /**
     * 查询批发商的待确认状态产品批号列表
     * @param wholName 批发商名称（可选）
     * @return 产品批号列表
     */
    List<WholNameDto> getPendingWholBatchByWholName(String wholName);

    /**
     * 下架屠宰企业产品批号
     * @param sbId 产品批号ID
     * @return 更新结果
     */
    int offlineSlauBatch(Integer sbId);

    /**
     * 根据屠宰企业ID查询企业详情
     * @param slauId 屠宰企业ID
     * @return 企业详情
     */
    NodeInfoDetailsDto getSlauInfoById(Integer slauId);

    /**
     * 根据屠宰企业ID获取下游批发商企业名称列表
     */
    List<String> getDownstreamWholesalerNames(Integer slauId);
}