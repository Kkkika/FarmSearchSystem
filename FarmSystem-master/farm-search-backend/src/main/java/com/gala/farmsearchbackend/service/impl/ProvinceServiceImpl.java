package com.gala.farmsearchbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gala.farmsearchbackend.annotation.CacheEvict;
import com.gala.farmsearchbackend.annotation.Cacheable;
import com.gala.farmsearchbackend.mapper.ProvinceMapper;
import com.gala.farmsearchbackend.model.domain.Province;
import com.gala.farmsearchbackend.service.ProvinceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * 省信息服务实现
 * @author Gala
 * @description 针对表【province(省行政区信息表)】的数据库操作Service实现
 */
@Service
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province>
        implements ProvinceService {

    @Resource
    private ProvinceMapper provinceMapper;

    @Override
    @Cacheable(key = "'province:list'", expire = 3600) //缓存1小时
    public List<Province> listProvinceAll() {
        return provinceMapper.listProvinceAll();
    }

    // 添加缓存清理方法（在数据更新时调用）
    @CacheEvict(key = "'province:list'")
    public void clearProvinceCache() {
        // 注解会自动清理缓存
    }
}