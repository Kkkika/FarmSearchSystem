package com.gala.farmsearchbackend.controller;
import com.gala.farmsearchbackend.common.BaseResponse;
import com.gala.farmsearchbackend.common.ResultUtils;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.exception.ThrowUtils;
import com.gala.farmsearchbackend.model.domain.FarmBatch;
import com.gala.farmsearchbackend.model.domain.SlauBatch;
import com.gala.farmsearchbackend.model.dto.farmDto.FarmDetailsDto;
import com.gala.farmsearchbackend.model.dto.farmDto.FarmQueryDto;
import com.gala.farmsearchbackend.model.dto.farmDto.FarmSimpleDto;
import com.gala.farmsearchbackend.model.dto.farmDto.FarmStateDto;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.slauDto.SlauNameDto;
import com.gala.farmsearchbackend.service.FarmBatchService;
import com.gala.farmsearchbackend.service.SlauBatchService;
import com.gala.farmsearchbackend.utils.BatchStateUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
养殖企业产品批号接口
 */
@RestController
@RequestMapping("/farm")
public class FarmBatchController {
    @Resource
    private FarmBatchService farmBatchService;

    @Resource
    private SlauBatchService slauBatchService;

    /**
     * 保存产品批号信息
     * @param farmBatch 产品批号信息
     * @return 保存结果
     */
    @PostMapping("/save")
    public BaseResponse<Integer> saveFarmBatch(@RequestBody FarmBatch farmBatch) {
        ThrowUtils.throwIf(farmBatch == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(farmBatch.getFarmId() == null || farmBatch.getFarmId() <= 0,
                ErrorCode.PARAMS_ERROR, "养殖企业ID不能为空");
        ThrowUtils.throwIf(farmBatch.getBatchId() == null || farmBatch.getBatchId().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "批号ID不能为空");
        ThrowUtils.throwIf(farmBatch.getType() == null || farmBatch.getType().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "产品品种不能为空");

        // 检查批号是否已存在
        FarmBatch existingBatch = farmBatchService.getFarmBatchByFarmIdByBatchId(
                farmBatch.getFarmId(), farmBatch.getBatchId());
        ThrowUtils.throwIf(existingBatch != null, ErrorCode.PARAMS_ERROR, "该批号已存在");

        int result = farmBatchService.saveFarmBatch(farmBatch);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "保存失败");

        return ResultUtils.success(result);
    }

    /**
     * 编辑产品批号信息
     * @param farmBatch 产品批号信息
     * @return 更新结果
     */
    @PutMapping("/edit")
    public BaseResponse<Integer> editFarmBatch(@RequestBody FarmBatch farmBatch) {
        ThrowUtils.throwIf(farmBatch == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(farmBatch.getFbId() == null || farmBatch.getFbId() <= 0,
                ErrorCode.PARAMS_ERROR, "产品批号ID不能为空");

        // 检查记录是否存在
        FarmBatch existingBatch = farmBatchService.getFarmBatchById(farmBatch.getFbId());
        ThrowUtils.throwIf(existingBatch == null, ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = farmBatchService.editFarmBatch(farmBatch);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "更新失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据ID删除产品批号信息
     * @param fbId 产品批号ID
     * @return 删除结果
     */
    @DeleteMapping("/remove/{fbId}")
    public BaseResponse<Integer> removeFarmBatchById(@PathVariable Integer fbId) {
        ThrowUtils.throwIf(fbId == null || fbId <= 0, ErrorCode.PARAMS_ERROR, "产品批号ID不能为空");

        // 检查记录是否存在
        FarmBatch existingBatch = farmBatchService.getFarmBatchById(fbId);
        ThrowUtils.throwIf(existingBatch == null, ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = farmBatchService.removeFarmBatchById(fbId);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "删除失败");

        return ResultUtils.success(result);
    }

    /**
     * 确认操作（将屠宰企业产品批号中的状态更新为已确认状态）
     * @param sbId 屠宰企业产品批号ID
     * @return 更新结果
     */
    @PutMapping("/slauBatch/confirm/{sbId}")
    public BaseResponse<Integer> confirmSlauBatch(@PathVariable Integer sbId) {
        ThrowUtils.throwIf(sbId == null || sbId <= 0,
                ErrorCode.PARAMS_ERROR, "屠宰企业产品批号ID不能为空");

        // 检查记录是否存在
        SlauBatch existingBatch = slauBatchService.getById(sbId);
        ThrowUtils.throwIf(existingBatch == null,
                ErrorCode.NOT_FOUND_ERROR, "屠宰企业产品批号信息不存在");

        // 检查当前状态是否为待确认
        ThrowUtils.throwIf(existingBatch.getState() != 2,
                ErrorCode.OPERATION_ERROR, "只有待确认状态的批号才能进行确认操作");

        String confirm = "已确认";
        int result = slauBatchService.confirmSlauBatch(sbId, confirm);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "确认操作失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据养殖产品批号ID查询简略信息--更新
     */
    @GetMapping("/simple/{fbId}")
    public BaseResponse<FarmSimpleDto> getFarmBatchSimple(@PathVariable Integer fbId) {
        FarmSimpleDto simpleDto = farmBatchService.getFarmBatchSimpleById(fbId);
        return ResultUtils.success(simpleDto);
    }

    /**
     * 根据养殖产品批号ID查询详情--查看
     */
    @GetMapping("/detail/{fbId}")
    public BaseResponse<FarmDetailsDto> getFarmBatchDetail(@PathVariable Integer fbId) {
        ThrowUtils.throwIf(fbId == null || fbId <= 0,
                ErrorCode.PARAMS_ERROR, "养殖产品批号ID不能为空");

        FarmDetailsDto detailDto = farmBatchService.getFarmDetailById(fbId);
        return ResultUtils.success(detailDto);
    }

    /**
     * 根据状态查询养殖企业产品批号列表
     * @param farmId 养殖企业ID
     * @param state 状态（可选）
     * @return 产品批号列表
     */
    @GetMapping("/byState/{farmId}")
    public BaseResponse<List<FarmStateDto>> getFarmBatchByState(
            @PathVariable Integer farmId,
            @RequestParam(required = false) Integer state) {
        ThrowUtils.throwIf(farmId == null || farmId <= 0, ErrorCode.PARAMS_ERROR, "养殖企业ID不能为空");

        // 验证状态参数
        if (state != null && !BatchStateUtils.isValidFarmState(state)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的状态参数");
        }

        FarmQueryDto queryDto = new FarmQueryDto();
        queryDto.setFarmId(farmId);
        queryDto.setState(state);

        List<FarmStateDto> farmBatchList = farmBatchService.getFarmBatchByState(queryDto);
        return ResultUtils.success(farmBatchList);
    }

    /**
     * 查询屠宰企业的待确认状态产品批号列表
     * @param slauName 屠宰企业名称（可选）
     * @return 待确认状态产品批号列表
     */
    @GetMapping("/slauBatch/pending")
    public BaseResponse<List<SlauNameDto>> getPendingSlauBatch(
            @RequestParam(required = false) String slauName) {

        List<SlauNameDto> slauBatchList = farmBatchService.getPendingSlauBatchBySlauName(
                slauName != null ? slauName.trim() : null
        );
        return ResultUtils.success(slauBatchList);
    }

    /**
     * 下架养殖企业产品批号
     * @param fbId 养殖企业产品批号ID
     * @return 下架结果
     */
    @PutMapping("/offline/{fbId}")
    public BaseResponse<Integer> offlineFarmBatch(@PathVariable Integer fbId) {
        ThrowUtils.throwIf(fbId == null || fbId <= 0,
                ErrorCode.PARAMS_ERROR, "养殖企业产品批号ID不能为空");

        // 检查记录是否存在
        FarmBatch existingBatch = farmBatchService.getFarmBatchById(fbId);
        ThrowUtils.throwIf(existingBatch == null,
                ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = farmBatchService.offlineFarmBatch(fbId);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "下架失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据养殖企业ID查询企业详情
     * @param farmId 养殖企业ID
     * @return 企业详情
     */
    @GetMapping("/farmInfo/{farmId}")
    public BaseResponse<NodeInfoDetailsDto> getFarmInfoById(@PathVariable Integer farmId) {
        ThrowUtils.throwIf(farmId == null || farmId <= 0,
                ErrorCode.PARAMS_ERROR, "养殖企业ID不能为空");

        NodeInfoDetailsDto farmInfo = farmBatchService.getFarmInfoById(farmId);
        return ResultUtils.success(farmInfo);
    }

    /**
     * 根据养殖企业ID查询下游屠宰企业名称列表
     * @param farmId 养殖企业ID
     * @return 屠宰企业名称列表
     */
    @GetMapping("/downstream/slaughterhouses/{farmId}")
    public BaseResponse<List<String>> getDownstreamSlaughterhouses(@PathVariable Integer farmId) {
        ThrowUtils.throwIf(farmId == null || farmId <= 0,
                ErrorCode.PARAMS_ERROR, "养殖企业ID不能为空");

        List<String> slaughterhouseNames = farmBatchService.getDownstreamSlaughterhouseNames(farmId);
        return ResultUtils.success(slaughterhouseNames);
    }
}
