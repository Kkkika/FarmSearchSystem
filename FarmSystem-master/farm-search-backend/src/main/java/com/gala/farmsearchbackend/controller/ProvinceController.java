package com.gala.farmsearchbackend.controller;

import com.gala.farmsearchbackend.common.BaseResponse;
import com.gala.farmsearchbackend.common.ResultUtils;
import com.gala.farmsearchbackend.model.domain.Province;
import com.gala.farmsearchbackend.service.ProvinceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 省份接口控制器
 * @author Gala
 */
@RestController
@RequestMapping("/province")
public class ProvinceController {

    @Resource
    private ProvinceService provinceService;

    /**
     * 获取所有省份信息
     * @return 省份列表
     */
    @GetMapping("/list")
    public BaseResponse<List<Province>> listProvinceAll() {
        List<Province> provinceList = provinceService.listProvinceAll();
        return ResultUtils.success(provinceList);
    }
}