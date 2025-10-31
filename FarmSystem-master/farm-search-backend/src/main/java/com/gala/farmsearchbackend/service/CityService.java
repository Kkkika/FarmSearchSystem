package com.gala.farmsearchbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gala.farmsearchbackend.model.domain.City;

import java.util.List;

/**
 * 市信息服务接口
 * @author Gala
 * @description 针对表【city(市行政区域信息表)】的数据库操作Service
 */
public interface CityService extends IService<City> {

    /**
     * 根据省份ID获取城市列表
     * @param provId 省份ID
     * @return 城市列表
     */
    List<City> listCityByProvId(Integer provId);
}