package com.gala.farmsearchbackend.controller;

import com.gala.farmsearchbackend.common.BaseResponse;
import com.gala.farmsearchbackend.common.ResultUtils;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.exception.ThrowUtils;
import com.gala.farmsearchbackend.model.domain.City;
import com.gala.farmsearchbackend.service.CityService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 城市接口控制器
 * @author Gala
 */
@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private CityService cityService;

    /**
     * 根据省份ID获取城市列表
     * @param provId 省份ID
     * @return 城市列表
     */
    @GetMapping("/listByProvince")
    public BaseResponse<List<City>> listCityByProvId(@RequestParam Integer provId) {
        ThrowUtils.throwIf(provId == null || provId <= 0, ErrorCode.PARAMS_ERROR, "省份ID不能为空");

        List<City> cityList = cityService.listCityByProvId(provId);
        return ResultUtils.success(cityList);
    }
}