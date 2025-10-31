package com.gala.farmsearchbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gala.farmsearchbackend.model.domain.WholBatch;
import com.gala.farmsearchbackend.model.dto.wholDto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批发商产品批号信息Mapper接口
 * @author Gala
 * @description 针对表【whol_batch(批发商产品批号信息表)】的数据库操作Mapper
 * @createDate 2025-09-20 20:09:26
 */
public interface WholBatchMapper extends BaseMapper<WholBatch> {

    /**
     * 更新批发商产品批号状态
     * @param wbId 产品批号ID
     * @param state 状态
     * @param remarks 备注
     * @return 更新结果
     */
    int updateWholBatchState(@Param("wbId") Integer wbId, @Param("state") Integer state, @Param("remarks") String remarks);

    /**
     * 根据批发商产品批号ID查询详情
     */
    WholBatchDetailDto selectWholBatchDetailById(@Param("wbId") Integer wbId);

    /**
     * 根据批发商产品批号ID查询详情
     */
    WholDetailsDto selectWholDetailById(@Param("wbId") Integer wbId);

    /**
     * 根据状态查询批发商产品批号列表
     * @param queryDto 查询条件
     * @return 产品批号列表
     */
    List<WholStateDto> selectWholBatchByState(@Param("queryDto") WholQueryDto queryDto);

    /**
     * 查询批发商的待确认状态产品批号列表（包含节点名称）
     * @param wholName 批发商名称（可选）
     * @return 产品批号列表
     */
    List<WholNameDto> selectPendingWholBatchByWholNameWithNode(@Param("wholName") String wholName);

    List<String> selectDownstreamRetailerNames(Integer wholId);
}