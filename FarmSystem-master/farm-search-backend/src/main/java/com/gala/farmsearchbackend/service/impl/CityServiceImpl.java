package com.gala.farmsearchbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gala.farmsearchbackend.annotation.CacheEvict;
import com.gala.farmsearchbackend.annotation.Cacheable;
import com.gala.farmsearchbackend.mapper.CityMapper;
import com.gala.farmsearchbackend.model.domain.City;
import com.gala.farmsearchbackend.service.CityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 市信息服务实现
 * @author Gala
 * @description 针对表【city(市行政区域信息表)】的数据库操作Service实现
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City>
        implements CityService {

    @Override
    @Cacheable(key = "'city:list:' + #provId", expire = 3600) //缓存1小时
    public List<City> listCityByProvId(Integer provId) {
        LambdaQueryWrapper<City> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(City::getProvId, provId)
                .orderByAsc(City::getCityName);
        return list(queryWrapper);
    }

    @CacheEvict(key = "'city:list:' + #provId")
    public void clearCityCache(Integer provId) {
        // 注解会自动清理缓存
    }
}