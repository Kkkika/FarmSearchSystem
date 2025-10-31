package com.gala.farmsearchbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gala.farmsearchbackend.model.domain.SlauBatch;
import com.gala.farmsearchbackend.model.dto.slauDto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 屠宰企业产品批号信息Mapper接口
 * @author Gala
 * @description 针对表【slau_batch(屠宰企业产品批号信息表)】的数据库操作Mapper
 * @createDate 2025-09-20 20:09:16
 */
public interface SlauBatchMapper extends BaseMapper<SlauBatch> {

    /**
     * 更新屠宰企业产品批号状态
     * @param sbId 屠宰企业产品批号ID
     * @param state 状态
     * @return 更新结果
     */
    int updateSlauBatchState(@Param("sbId") Integer sbId, @Param("state") Integer state, @Param("remarks") String remarks);

    /**
     * 根据屠宰产品批号ID查询详情
     */
    SlauBatchDetailDto selectSlauBatchDetailById(@Param("sbId") Integer sbId);

    /**
     * 根据屠宰产品批号ID查询详情
     */
    SlauDetailsDto selectSlauDetailById(@Param("sbId") Integer sbId);

    /**
     * 根据状态查询屠宰企业产品批号列表
     * @param queryDto 查询条件
     * @return 产品批号列表
     */
    List<SlauStateDto> selectSlauBatchByState(@Param("queryDto") SlauQueryDto queryDto);

    /**
     * 查询屠宰企业的待确认状态产品批号列表（包含节点名称）
     * @param slauName 屠宰企业名称（可选）
     * @return 产品批号列表
     */
    List<SlauNameDto> selectPendingSlauBatchBySlauNameWithNode(@Param("slauName") String slauName);

    List<String> selectDownstreamWholesalerNames(Integer slauId);
}