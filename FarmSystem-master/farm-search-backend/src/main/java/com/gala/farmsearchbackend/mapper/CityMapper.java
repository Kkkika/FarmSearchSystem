package com.gala.farmsearchbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gala.farmsearchbackend.model.domain.City;
import org.apache.ibatis.annotations.Mapper;


/**
* @author Gala
* @description 针对表【city(市行政区域信息表)】的数据库操作Mapper
* @createDate 2025-09-20 20:08:08
* @Entity com.gala.farmsearchbackend.domain.City
*/
@Mapper
public interface CityMapper extends BaseMapper<City> {

}




