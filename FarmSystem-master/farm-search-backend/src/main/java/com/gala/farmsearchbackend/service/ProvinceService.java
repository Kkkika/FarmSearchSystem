package com.gala.farmsearchbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gala.farmsearchbackend.model.domain.Province;

import java.util.List;

/**
 * 省信息服务接口
 * @author Gala
 * @description 针对表【province(省行政区信息表)】的数据库操作Service
 */
public interface ProvinceService extends IService<Province> {

    /**
     * 获取所有省份信息
     * @return 省份列表
     */
    List<Province> listProvinceAll();

}