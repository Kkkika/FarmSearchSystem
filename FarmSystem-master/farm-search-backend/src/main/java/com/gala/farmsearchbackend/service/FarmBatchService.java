package com.gala.farmsearchbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gala.farmsearchbackend.model.domain.FarmBatch;
import com.gala.farmsearchbackend.model.dto.farmDto.*;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.slauDto.SlauNameDto;

import java.util.List;


/**
* @author Gala
* @description 针对表【farm_batch(养殖企业产品批号信息表)】的数据库操作Service
* @createDate 2025-09-20 20:08:35
*/
public interface FarmBatchService extends IService<FarmBatch> {
    /**
     * 根据养殖企业ID和批号ID获取产品批号信息
     * @param farmId 养殖企业ID
     * @param batchId 批号ID
     * @return 产品批号信息
     */
    FarmBatch getFarmBatchByFarmIdByBatchId(Integer farmId, String batchId);

    /**
     * 保存产品批号信息
     * @param farmBatch 产品批号信息
     * @return 保存结果
     */
    int saveFarmBatch(FarmBatch farmBatch);

    /**
     * 根据ID获取产品批号信息
     * @param fbId 产品批号ID
     * @return 产品批号信息
     */
    FarmBatch getFarmBatchById(Integer fbId);

    /**
     * 编辑产品批号信息
     * @param farmBatch 产品批号信息
     * @return 更新结果
     */
    int editFarmBatch(FarmBatch farmBatch);

    /**
     * 根据ID删除产品批号信息
     * @param fbId 产品批号ID
     * @return 删除结果
     */
    int removeFarmBatchById(Integer fbId);

    /**
     * 根据省市查询养殖企业信息
     */
    List<FarmDto> getFarmsByArea(Integer provId, Integer cityId);

    /**
     * 根据养殖企业ID查询产品批号简单信息
     */
    List<FarmBatchSimpleDto> getFarmBatchesByFarmId(Integer farmId);

    /**
     * 根据养殖产品批号ID查询简略信息
     * @param fbId 养殖产品批号ID
     * @return 养殖产品批号简略DTO
     */
    FarmSimpleDto getFarmBatchSimpleById(Integer fbId);

    /**
     * 根据养殖产品批号ID查询详情
     */
    FarmDetailsDto getFarmDetailById(Integer fbId);

    /**
     * 根据状态查询养殖企业产品批号列表
     * @param queryDto 查询条件
     * @return 产品批号列表
     */
    List<FarmStateDto> getFarmBatchByState(FarmQueryDto queryDto);

    /**
     * 查询屠宰企业的待确认状态产品批号列表
     * @param slauName 屠宰企业名称（可选）
     * @return 产品批号列表
     */
    List<SlauNameDto> getPendingSlauBatchBySlauName(String slauName);

    /**
     * 下架养殖企业产品批号
     * @param fbId 产品批号ID
     * @return 更新结果
     */
    int offlineFarmBatch(Integer fbId);

    /**
     * 根据养殖企业ID查询企业详情
     * @param farmId 养殖企业ID
     * @return 企业详情
     */
    NodeInfoDetailsDto getFarmInfoById(Integer farmId);

    /**
     * 根据养殖企业ID获取下游屠宰企业名称列表
     */
    List<String> getDownstreamSlaughterhouseNames(Integer farmId);
}
