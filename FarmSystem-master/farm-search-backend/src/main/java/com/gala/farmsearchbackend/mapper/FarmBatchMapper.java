package com.gala.farmsearchbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gala.farmsearchbackend.model.domain.FarmBatch;
import com.gala.farmsearchbackend.model.dto.farmDto.FarmDetailsDto;
import com.gala.farmsearchbackend.model.dto.farmDto.FarmQueryDto;
import com.gala.farmsearchbackend.model.dto.farmDto.FarmSimpleDto;
import com.gala.farmsearchbackend.model.dto.farmDto.FarmStateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
* @author Gala
* @description 针对表【farm_batch(养殖企业产品批号信息表)】的数据库操作Mapper
* @createDate 2025-09-20 20:08:35
* @Entity com.gala.farmsearchbackend.domain.FarmBatch
*/
@Mapper
public interface FarmBatchMapper extends BaseMapper<FarmBatch> {

    /**
     * 根据养殖产品批号ID查询简略信息
     */
    FarmSimpleDto selectFarmBatchSimpleById(@Param("fbId") Integer fbId);

    /**
     * 根据养殖产品批号ID查询详情
     */
    FarmDetailsDto selectFarmDetailById(@Param("fbId") Integer fbId);

    /**
     * 根据状态查询养殖企业产品批号列表
     * @param queryDto 查询条件
     * @return 产品批号列表
     */
    List<FarmStateDto> selectFarmBatchByState(@Param("queryDto") FarmQueryDto queryDto);

    List<String> selectDownstreamSlaughterhouseNames(Integer farmId);
}




