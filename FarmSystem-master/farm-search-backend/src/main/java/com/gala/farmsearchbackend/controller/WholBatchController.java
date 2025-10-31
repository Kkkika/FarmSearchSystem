package com.gala.farmsearchbackend.controller;

import com.gala.farmsearchbackend.common.BaseResponse;
import com.gala.farmsearchbackend.common.ResultUtils;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.exception.ThrowUtils;
import com.gala.farmsearchbackend.model.domain.RetaBatch;
import com.gala.farmsearchbackend.model.domain.WholBatch;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.retaDto.RetaNameDto;
import com.gala.farmsearchbackend.model.dto.slauDto.SlauBatchSimpleDto;
import com.gala.farmsearchbackend.model.dto.slauDto.SlaughterhouseDto;
import com.gala.farmsearchbackend.model.dto.wholDto.*;
import com.gala.farmsearchbackend.service.*;
import com.gala.farmsearchbackend.utils.BatchStateUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 批发商产品批号接口控制器
 * @author Gala
 */
@RestController
@RequestMapping("/whol/batch")
public class WholBatchController {

    @Resource
    private WholBatchService wholBatchService;
    @Resource
    private RetaBatchService retaBatchService;

    @Resource
    private SlauBatchService slauBatchService;

    /**
     * 保存批发商产品批号信息
     * @param wholBatch 产品批号信息
     * @return 保存结果
     */
    @PostMapping("/save")
    public BaseResponse<Integer> saveWholBatch(@RequestBody WholBatch wholBatch) {
        ThrowUtils.throwIf(wholBatch == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(wholBatch.getWholId() == null || wholBatch.getWholId() <= 0,
                ErrorCode.PARAMS_ERROR, "批发商ID不能为空");
        ThrowUtils.throwIf(wholBatch.getBatchId() == null || wholBatch.getBatchId().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "批号ID不能为空");
        ThrowUtils.throwIf(wholBatch.getSbId() == null || wholBatch.getSbId() <= 0,
                ErrorCode.PARAMS_ERROR, "屠宰企业进场编号不能为空");
        ThrowUtils.throwIf(wholBatch.getType() == null || wholBatch.getType().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "产品品种不能为空");

        // 检查批号是否已存在
        WholBatch existingBatch = wholBatchService.getWholBatchByWholIdByBatchId(
                wholBatch.getWholId(), wholBatch.getBatchId());
        ThrowUtils.throwIf(existingBatch != null, ErrorCode.PARAMS_ERROR, "该批号已存在");

        int result = wholBatchService.saveWholBatch(wholBatch);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "保存失败");

        return ResultUtils.success(result);
    }

    /**
     * 编辑产品批号信息
     * @param wholBatch 产品批号信息
     * @return 更新结果
     */
    @PutMapping("/edit")
    public BaseResponse<Integer> editWholBatch(@RequestBody WholBatch wholBatch) {
        ThrowUtils.throwIf(wholBatch == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(wholBatch.getWbId() == null || wholBatch.getWbId() <= 0,
                ErrorCode.PARAMS_ERROR, "产品批号ID不能为空");

        // 检查记录是否存在
        WholBatch existingBatch = wholBatchService.getWholBatchById(wholBatch.getWbId());
        ThrowUtils.throwIf(existingBatch == null, ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = wholBatchService.editWholBatch(wholBatch);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "更新失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据ID删除产品批号信息
     * @param wbId 产品批号ID
     * @return 删除结果
     */
    @DeleteMapping("/remove/{wbId}")
    public BaseResponse<Integer> removeWholBatchById(@PathVariable Integer wbId) {
        ThrowUtils.throwIf(wbId == null || wbId <= 0, ErrorCode.PARAMS_ERROR, "产品批号ID不能为空");

        // 检查记录是否存在
        WholBatch existingBatch = wholBatchService.getWholBatchById(wbId);
        ThrowUtils.throwIf(existingBatch == null, ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = wholBatchService.removeWholBatchById(wbId);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "删除失败");

        return ResultUtils.success(result);
    }

    /**
     * 确认操作（将零售商企业产品批号中的状态更新为已确认状态）
     * @param rbId 零售商企业产品批号ID
     * @return 更新结果
     */
    @PutMapping("/confirm/{rbId}")
    public BaseResponse<Integer> confirmRetaBatch(@PathVariable Integer rbId) {
        ThrowUtils.throwIf(rbId == null || rbId <= 0,
                ErrorCode.PARAMS_ERROR, "零售商企业产品批号ID不能为空");

        // 检查记录是否存在
        RetaBatch existingBatch = retaBatchService.getById(rbId);
        ThrowUtils.throwIf(existingBatch == null,
                ErrorCode.NOT_FOUND_ERROR, "零售商企业产品批号信息不存在");

        // 检查当前状态是否为待确认
        ThrowUtils.throwIf(existingBatch.getState() != 2,
                ErrorCode.OPERATION_ERROR, "只有待确认状态的批号才能进行确认操作");

        String confirm = "已确认";
        int result = retaBatchService.confirmRetaBatch(rbId, confirm);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "确认操作失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据省市查询屠宰企业信息
     */
    @GetMapping("/create/slaughterhouses")
    public BaseResponse<List<SlaughterhouseDto>> getSlaughterhousesByArea(
            @RequestParam Integer provId,
            @RequestParam Integer cityId) {
        ThrowUtils.throwIf(provId == null || provId <= 0, ErrorCode.PARAMS_ERROR, "省份ID不能为空");
        ThrowUtils.throwIf(cityId == null || cityId <= 0, ErrorCode.PARAMS_ERROR, "城市ID不能为空");

        List<SlaughterhouseDto> slaughterhouses = slauBatchService.getSlaughterhousesByArea(provId, cityId);
        return ResultUtils.success(slaughterhouses);
    }

    /**
     * 根据屠宰企业ID查询产品批号信息
     */
    @GetMapping("/create/slauBatches/{slauId}")
    public BaseResponse<List<SlauBatchSimpleDto>> getSlauBatchesBySlaughterhouse(@PathVariable Integer slauId) {
        ThrowUtils.throwIf(slauId == null || slauId <= 0, ErrorCode.PARAMS_ERROR, "屠宰企业ID不能为空");

        List<SlauBatchSimpleDto> batches = slauBatchService.getSlauBatchesBySlauId(slauId);
        return ResultUtils.success(batches);
    }

    /**
     * 检查产品批号是否已存在 - 复用现有方法
     */
    @GetMapping("/create/checkBatchId")
    public BaseResponse<Boolean> checkBatchIdExists(
            @RequestParam Integer wholId,
            @RequestParam String batchId) {
        ThrowUtils.throwIf(wholId == null || wholId <= 0, ErrorCode.PARAMS_ERROR, "批发商ID不能为空");
        ThrowUtils.throwIf(batchId == null || batchId.trim().isEmpty(), ErrorCode.PARAMS_ERROR, "产品批号不能为空");

        // 复用现有的检查方法
        boolean exists = wholBatchService.getWholBatchByWholIdByBatchId(wholId, batchId.trim()) != null;
        return ResultUtils.success(exists);
    }

    /**
     * 新建批发商产品批号 - 复用现有的保存方法
     */
    @PostMapping("/create")
    public BaseResponse<Integer> createWholBatch(@RequestBody CreateWholBatchRequestDto requestDto) {
        ThrowUtils.throwIf(requestDto == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(requestDto.getBatchId() == null || requestDto.getBatchId().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "产品批号不能为空");
        ThrowUtils.throwIf(requestDto.getWholId() == null || requestDto.getWholId() <= 0,
                ErrorCode.PARAMS_ERROR, "批发商ID不能为空");
        ThrowUtils.throwIf(requestDto.getSbId() == null || requestDto.getSbId() <= 0,
                ErrorCode.PARAMS_ERROR, "屠宰企业产品批号ID不能为空");
        ThrowUtils.throwIf(requestDto.getType() == null || requestDto.getType().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "产品品种不能为空");

        // 检查批号是否已存在 - 复用现有方法
        boolean exists = wholBatchService.getWholBatchByWholIdByBatchId(requestDto.getWholId(), requestDto.getBatchId()) != null;
        ThrowUtils.throwIf(exists, ErrorCode.PARAMS_ERROR, "该产品批号已存在");

        // 创建WholBatch对象并保存 - 复用现有的save方法
        WholBatch wholBatch = new WholBatch();
        wholBatch.setBatchId(requestDto.getBatchId());
        wholBatch.setWholId(requestDto.getWholId());
        wholBatch.setSbId(requestDto.getSbId());
        wholBatch.setType(requestDto.getType());
        wholBatch.setRemarks(requestDto.getRemarks());
        // 设置默认的批号录入日期和状态
        wholBatch.setBatchDate(LocalDateTime.now());
        wholBatch.setState(1);

        int result = wholBatchService.saveWholBatch(wholBatch);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "新建失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据批发商产品批号ID查询详情--更新
     */
    @GetMapping("/detailBatch/{wbId}")
    public BaseResponse<WholBatchDetailDto> getWholBatchDetail(@PathVariable Integer wbId) {
        WholBatchDetailDto detailDto = wholBatchService.getWholBatchDetailById(wbId);
        return ResultUtils.success(detailDto);
    }

    /**
     * 批发商发送确认请求给屠宰企业
     */
    @PostMapping("/whol-to-slau/{wbId}")
    public BaseResponse<String> sendWholToSlauConfirm(@PathVariable Integer wbId) {
        wholBatchService.sendConfirmToSlau(wbId);
        return ResultUtils.success("确认请求发送成功");
    }

    /**
     * 根据批发商产品批号ID查询详情--查看
     */
    @GetMapping("/detail/{wbId}")
    public BaseResponse<WholDetailsDto> getWholDetail(@PathVariable Integer wbId) {
        ThrowUtils.throwIf(wbId == null || wbId <= 0,
                ErrorCode.PARAMS_ERROR, "批发商产品批号ID不能为空");

        WholDetailsDto detailDto = wholBatchService.getWholDetailById(wbId);
        return ResultUtils.success(detailDto);
    }

    /**
     * 根据状态查询批发商产品批号列表
     * @param wholId 批发商ID
     * @param state 状态（可选）
     * @return 产品批号列表
     */
    @GetMapping("/byState/{wholId}")
    public BaseResponse<List<WholStateDto>> getWholBatchByState(
            @PathVariable Integer wholId,
            @RequestParam(required = false) Integer state) {
        ThrowUtils.throwIf(wholId == null || wholId <= 0, ErrorCode.PARAMS_ERROR, "批发商ID不能为空");

        // 验证状态参数
        if (state != null && !BatchStateUtils.isValidOtherState(state)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的状态参数");
        }

        WholQueryDto queryDto = new WholQueryDto();
        queryDto.setWholId(wholId);
        queryDto.setState(state);

        List<WholStateDto> wholBatchList = wholBatchService.getWholBatchByState(queryDto);
        return ResultUtils.success(wholBatchList);
    }

    /**
     * 查询零售商企业的待确认状态产品批号列表
     * @param retaName 零售商企业名称（可选）
     * @return 待确认状态产品批号列表
     */
    @GetMapping("/pending")
    public BaseResponse<List<RetaNameDto>> getPendingRetaBatch(
            @RequestParam(required = false) String retaName) {

        List<RetaNameDto> retaBatchList = wholBatchService.getPendingRetaBatchByRetaName(
                retaName != null ? retaName.trim() : null
        );
        return ResultUtils.success(retaBatchList);
    }

    /**
     * 下架批发商产品批号
     * @param wbId 批发商产品批号ID
     * @return 下架结果
     */
    @PutMapping("/offline/{wbId}")
    public BaseResponse<Integer> offlineWholBatch(@PathVariable Integer wbId) {
        ThrowUtils.throwIf(wbId == null || wbId <= 0,
                ErrorCode.PARAMS_ERROR, "批发商产品批号ID不能为空");

        // 检查记录是否存在
        WholBatch existingBatch = wholBatchService.getWholBatchById(wbId);
        ThrowUtils.throwIf(existingBatch == null,
                ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = wholBatchService.offlineWholBatch(wbId);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "下架失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据批发商ID查询企业详情
     * @param wholId 批发商ID
     * @return 企业详情
     */
    @GetMapping("/wholInfo/{wholId}")
    public BaseResponse<NodeInfoDetailsDto> getWholInfoById(@PathVariable Integer wholId) {
        ThrowUtils.throwIf(wholId == null || wholId <= 0,
                ErrorCode.PARAMS_ERROR, "批发商ID不能为空");

        NodeInfoDetailsDto wholInfo = wholBatchService.getWholInfoById(wholId);
        return ResultUtils.success(wholInfo);
    }

    /**
     * 根据批发商ID查询下游零售商企业名称列表
     * @param wholId 批发商ID
     * @return 零售商企业名称列表
     */
    @GetMapping("/downstream/retailers/{wholId}")
    public BaseResponse<List<String>> getDownstreamRetailers(@PathVariable Integer wholId) {
        ThrowUtils.throwIf(wholId == null || wholId <= 0,
                ErrorCode.PARAMS_ERROR, "批发商ID不能为空");

        List<String> retailerNames = wholBatchService.getDownstreamRetailerNames(wholId);
        return ResultUtils.success(retailerNames);
    }
}