package com.gala.farmsearchbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gala.farmsearchbackend.model.domain.RetaBatch;
import com.gala.farmsearchbackend.model.dto.retaDto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 零售商产品批号信息Mapper接口
 * @author Gala
 * @description 针对表【reta_batch(零售商产品批号信息表)】的数据库操作Mapper
 * @createDate 2025-09-20 20:09:09
 */
public interface RetaBatchMapper extends BaseMapper<RetaBatch> {

    /**
     * 更新零售商产品批号状态
     * @param rbId 产品批号ID
     * @param state 状态
     * @param remarks 备注
     * @return 更新结果
     */
    int updateRetaBatchState(@Param("rbId") Integer rbId, @Param("state") Integer state, @Param("remarks") String remarks);

    /**
     * 根据零售商产品批号ID查询详情
     */
    RetaBatchDetailDto selectRetaBatchDetailById(@Param("rbId") Integer rbId);

    /**
     * 根据零售商产品批号ID查询详情
     */
    RetaDetailsDto selectRetaDetailById(@Param("rbId") Integer rbId);

    /**
     * 根据状态查询零售商产品批号列表
     * @param queryDto 查询条件
     * @return 产品批号列表
     */
    List<RetaStateDto> selectRetaBatchByState(@Param("queryDto") RetaQueryDto queryDto);

    /**
     * 查询零售商的待确认状态产品批号列表（包含节点名称）
     * @param retaName 零售商名称（可选）
     * @return 产品批号列表
     */
    List<RetaNameDto> selectPendingRetaBatchByRetaNameWithNode(@Param("retaName") String retaName);
}