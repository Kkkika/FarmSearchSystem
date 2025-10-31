package com.gala.farmsearchbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gala.farmsearchbackend.model.domain.Province;

import java.util.List;

/**
 * 省信息Mapper接口
 * @author Gala
 * @description 针对表【province(省行政区信息表)】的数据库操作Mapper
 */
public interface ProvinceMapper extends BaseMapper<Province> {

    /**
     * 获取所有省份信息
     * @return 省份列表
     */
    List<Province> listProvinceAll();
}