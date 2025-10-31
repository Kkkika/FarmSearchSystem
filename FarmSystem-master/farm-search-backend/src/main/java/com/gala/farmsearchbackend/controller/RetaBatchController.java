package com.gala.farmsearchbackend.controller;

import com.gala.farmsearchbackend.common.BaseResponse;
import com.gala.farmsearchbackend.common.ResultUtils;
import com.gala.farmsearchbackend.constants.BatchStateConstants;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.exception.ThrowUtils;
import com.gala.farmsearchbackend.model.domain.City;
import com.gala.farmsearchbackend.model.domain.Province;
import com.gala.farmsearchbackend.model.domain.RetaBatch;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.retaDto.*;
import com.gala.farmsearchbackend.model.dto.wholDto.WholBatchSimpleDto;
import com.gala.farmsearchbackend.model.dto.wholDto.WholesalerDto;
import com.gala.farmsearchbackend.service.CityService;
import com.gala.farmsearchbackend.service.ProvinceService;
import com.gala.farmsearchbackend.service.RetaBatchService;
import com.gala.farmsearchbackend.service.WholBatchService;
import com.gala.farmsearchbackend.utils.BatchStateUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;

/**
 * 零售商产品批号接口控制器
 * @author Gala
 */
@RestController
@RequestMapping("/reta/batch")
public class RetaBatchController {

    @Resource
    private RetaBatchService retaBatchService;

    @Resource
    private WholBatchService wholBatchService;

    /**
     * 保存零售商产品批号信息
     * @param retaBatch 产品批号信息
     * @return 保存结果
     */
    @PostMapping("/save")
    public BaseResponse<Integer> saveRetaBatch(@RequestBody RetaBatch retaBatch) {
        ThrowUtils.throwIf(retaBatch == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(retaBatch.getRetaId() == null || retaBatch.getRetaId() <= 0,
                ErrorCode.PARAMS_ERROR, "零售商ID不能为空");
        ThrowUtils.throwIf(retaBatch.getBatchId() == null || retaBatch.getBatchId().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "批号ID不能为空");
        ThrowUtils.throwIf(retaBatch.getWbId() == null || retaBatch.getWbId() <= 0,
                ErrorCode.PARAMS_ERROR, "批发商进场编号不能为空");
        ThrowUtils.throwIf(retaBatch.getType() == null || retaBatch.getType().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "产品品种不能为空");

        // 检查批号是否已存在
        RetaBatch existingBatch = retaBatchService.getRetaBatchByRetaIdByBatchId(
                retaBatch.getRetaId(), retaBatch.getBatchId());
        ThrowUtils.throwIf(existingBatch != null, ErrorCode.PARAMS_ERROR, "该批号已存在");

        int result = retaBatchService.saveRetaBatch(retaBatch);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "保存失败");

        return ResultUtils.success(result);
    }

    /**
     * 编辑产品批号信息
     * @param retaBatch 产品批号信息
     * @return 更新结果
     */
    @PutMapping("/edit")
    public BaseResponse<Integer> editRetaBatch(@RequestBody RetaBatch retaBatch) {
        ThrowUtils.throwIf(retaBatch == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(retaBatch.getRbId() == null || retaBatch.getRbId() <= 0,
                ErrorCode.PARAMS_ERROR, "产品批号ID不能为空");

        // 检查记录是否存在
        RetaBatch existingBatch = retaBatchService.getRetaBatchById(retaBatch.getRbId());
        ThrowUtils.throwIf(existingBatch == null, ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = retaBatchService.editRetaBatch(retaBatch);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "更新失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据ID删除产品批号信息
     * @param rbId 产品批号ID
     * @return 删除结果
     */
    @DeleteMapping("/remove/{rbId}")
    public BaseResponse<Integer> removeRetaBatchById(@PathVariable Integer rbId) {
        ThrowUtils.throwIf(rbId == null || rbId <= 0, ErrorCode.PARAMS_ERROR, "产品批号ID不能为空");

        // 检查记录是否存在
        RetaBatch existingBatch = retaBatchService.getRetaBatchById(rbId);
        ThrowUtils.throwIf(existingBatch == null, ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = retaBatchService.removeRetaBatchById(rbId);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "删除失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据省市查询批发商信息
     */
    @GetMapping("/create/wholesalers")
    public BaseResponse<List<WholesalerDto>> getWholesalersByArea(
            @RequestParam Integer provId,
            @RequestParam Integer cityId) {
        ThrowUtils.throwIf(provId == null || provId <= 0, ErrorCode.PARAMS_ERROR, "省份ID不能为空");
        ThrowUtils.throwIf(cityId == null || cityId <= 0, ErrorCode.PARAMS_ERROR, "城市ID不能为空");

        List<WholesalerDto> wholesalers = wholBatchService.getWholesalersByArea(provId, cityId);
        return ResultUtils.success(wholesalers);
    }

    /**
     * 根据批发商ID查询产品批号信息
     */
    @GetMapping("/create/wholBatches/{wholId}")
    public BaseResponse<List<WholBatchSimpleDto>> getWholBatchesByWholesaler(@PathVariable Integer wholId) {
        ThrowUtils.throwIf(wholId == null || wholId <= 0, ErrorCode.PARAMS_ERROR, "批发商ID不能为空");

        List<WholBatchSimpleDto> batches = wholBatchService.getWholBatchesByWholId(wholId);
        return ResultUtils.success(batches);
    }

    /**
     * 检查产品批号是否已存在 - 复用现有方法
     */
    @GetMapping("/create/checkBatchId")
    public BaseResponse<Boolean> checkBatchIdExists(
            @RequestParam Integer retaId,
            @RequestParam String batchId) {
        ThrowUtils.throwIf(retaId == null || retaId <= 0, ErrorCode.PARAMS_ERROR, "零售商ID不能为空");
        ThrowUtils.throwIf(batchId == null || batchId.trim().isEmpty(), ErrorCode.PARAMS_ERROR, "产品批号不能为空");

        // 复用现有的检查方法
        boolean exists = retaBatchService.getRetaBatchByRetaIdByBatchId(retaId, batchId.trim()) != null;
        return ResultUtils.success(exists);
    }

    /**
     * 新建零售商产品批号 - 复用现有的保存方法
     */
    @PostMapping("/create")
    public BaseResponse<Integer> createRetaBatch(@RequestBody CreateRetaBatchRequestDto requestDto) {
        ThrowUtils.throwIf(requestDto == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(requestDto.getBatchId() == null || requestDto.getBatchId().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "产品批号不能为空");
        ThrowUtils.throwIf(requestDto.getRetaId() == null || requestDto.getRetaId() <= 0,
                ErrorCode.PARAMS_ERROR, "零售商ID不能为空");
        ThrowUtils.throwIf(requestDto.getWbId() == null || requestDto.getWbId() <= 0,
                ErrorCode.PARAMS_ERROR, "批发商产品批号ID不能为空");
        ThrowUtils.throwIf(requestDto.getType() == null || requestDto.getType().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "产品品种不能为空");
//        ThrowUtils.throwIf(requestDto.getSourceId() == null || requestDto.getSourceId().trim().isEmpty(),
//                ErrorCode.PARAMS_ERROR, "溯源标识码不能为空");

        // 检查批号是否已存在 - 复用现有方法
        boolean exists = retaBatchService.getRetaBatchByRetaIdByBatchId(requestDto.getRetaId(), requestDto.getBatchId()) != null;
        ThrowUtils.throwIf(exists, ErrorCode.PARAMS_ERROR, "该产品批号已存在");

        // 创建RetaBatch对象并保存 - 复用现有的save方法
        RetaBatch retaBatch = new RetaBatch();
        retaBatch.setBatchId(requestDto.getBatchId());
        retaBatch.setRetaId(requestDto.getRetaId());
        retaBatch.setWbId(requestDto.getWbId());
        retaBatch.setType(requestDto.getType());
        //retaBatch.setSourceId(requestDto.getSourceId());
        retaBatch.setRemarks(requestDto.getRemarks());
        // 设置默认的批号录入日期和状态
        retaBatch.setBatchDate(LocalDateTime.now());
        retaBatch.setState(1);

        int result = retaBatchService.saveRetaBatch(retaBatch);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "新建失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据零售商产品批号ID查询详情--更新
     */
    @GetMapping("/detailBatch/{rbId}")
    public BaseResponse<RetaBatchDetailDto> getRetaBatchDetail(@PathVariable Integer rbId) {
        RetaBatchDetailDto detailDto = retaBatchService.getRetaBatchDetailById(rbId);
        return ResultUtils.success(detailDto);
    }

    /**
     * 零售商发送确认请求给批发商
     */
    @PostMapping("/reta-to-whol/{rbId}")
    public BaseResponse<String> sendRetaToWholConfirm(@PathVariable Integer rbId) {
        retaBatchService.sendConfirmToWhol(rbId);
        return ResultUtils.success("确认请求发送成功");
    }

    /**
     * 根据零售商产品批号ID查询详情--查看
     */
    @GetMapping("/detail/{rbId}")
    public BaseResponse<RetaDetailsDto> getRetaDetail(@PathVariable Integer rbId) {
        ThrowUtils.throwIf(rbId == null || rbId <= 0,
                ErrorCode.PARAMS_ERROR, "零售商产品批号ID不能为空");

        RetaDetailsDto detailDto = retaBatchService.getRetaDetailById(rbId);
        return ResultUtils.success(detailDto);
    }

    /**
     * 根据状态查询零售商产品批号列表
     * @param retaId 零售商ID
     * @param state 状态（可选）
     * @return 产品批号列表
     */
    @GetMapping("/byState/{retaId}")
    public BaseResponse<List<RetaStateDto>> getRetaBatchByState(
            @PathVariable Integer retaId,
            @RequestParam(required = false) Integer state) {
        ThrowUtils.throwIf(retaId == null || retaId <= 0, ErrorCode.PARAMS_ERROR, "零售商ID不能为空");

        // 验证状态参数
        if (state != null && !BatchStateUtils.isValidOtherState(state)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的状态参数");
        }

        RetaQueryDto queryDto = new RetaQueryDto();
        queryDto.setRetaId(retaId);
        queryDto.setState(state);

        List<RetaStateDto> retaBatchList = retaBatchService.getRetaBatchByState(queryDto);
        return ResultUtils.success(retaBatchList);
    }

    /**
     * 下架零售商产品批号
     * @param rbId 零售商产品批号ID
     * @return 下架结果
     */
    @PutMapping("/offline/{rbId}")
    public BaseResponse<Integer> offlineRetaBatch(@PathVariable Integer rbId) {
        ThrowUtils.throwIf(rbId == null || rbId <= 0,
                ErrorCode.PARAMS_ERROR, "零售商产品批号ID不能为空");

        // 检查记录是否存在
        RetaBatch existingBatch = retaBatchService.getRetaBatchById(rbId);
        ThrowUtils.throwIf(existingBatch == null,
                ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = retaBatchService.offlineRetaBatch(rbId);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "下架失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据零售商ID查询企业详情
     * @param retaId 零售商ID
     * @return 企业详情
     */
    @GetMapping("/retaInfo/{retaId}")
    public BaseResponse<NodeInfoDetailsDto> getRetaInfoById(@PathVariable Integer retaId) {
        ThrowUtils.throwIf(retaId == null || retaId <= 0,
                ErrorCode.PARAMS_ERROR, "零售商ID不能为空");

        NodeInfoDetailsDto retaInfo = retaBatchService.getRetaInfoById(retaId);
        return ResultUtils.success(retaInfo);
    }
}