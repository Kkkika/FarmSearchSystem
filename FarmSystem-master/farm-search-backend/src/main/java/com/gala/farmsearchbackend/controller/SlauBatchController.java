package com.gala.farmsearchbackend.controller;

import com.gala.farmsearchbackend.common.BaseResponse;
import com.gala.farmsearchbackend.common.ResultUtils;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.exception.ThrowUtils;
import com.gala.farmsearchbackend.model.domain.SlauBatch;
import com.gala.farmsearchbackend.model.domain.WholBatch;
import com.gala.farmsearchbackend.model.dto.farmDto.FarmBatchSimpleDto;
import com.gala.farmsearchbackend.model.dto.farmDto.FarmDto;
import com.gala.farmsearchbackend.model.dto.nodeDto.NodeInfoDetailsDto;
import com.gala.farmsearchbackend.model.dto.slauDto.*;
import com.gala.farmsearchbackend.model.dto.wholDto.WholNameDto;
import com.gala.farmsearchbackend.service.*;
import com.gala.farmsearchbackend.utils.BatchStateUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 屠宰企业产品批号接口控制器
 * @author Gala
 */
@RestController
@RequestMapping("/slau/batch")
public class SlauBatchController {

    @Resource
    private SlauBatchService slauBatchService;
    @Resource
    private WholBatchService wholBatchService;

    @Resource
    private FarmBatchService farmBatchService;

    /**
     * 保存屠宰企业产品批号信息
     * @param slauBatch 产品批号信息
     * @return 保存结果
     */
    @PostMapping("/save")
    public BaseResponse<Integer> saveSlauBatch(@RequestBody SlauBatch slauBatch) {
        ThrowUtils.throwIf(slauBatch == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(slauBatch.getSlauId() == null || slauBatch.getSlauId() <= 0,
                ErrorCode.PARAMS_ERROR, "屠宰企业ID不能为空");
        ThrowUtils.throwIf(slauBatch.getBatchId() == null || slauBatch.getBatchId().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "批号ID不能为空");
        ThrowUtils.throwIf(slauBatch.getFbId() == null || slauBatch.getFbId() <= 0,
                ErrorCode.PARAMS_ERROR, "养殖企业进场编号不能为空");
        ThrowUtils.throwIf(slauBatch.getType() == null || slauBatch.getType().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "产品品种不能为空");

        // 检查批号是否已存在
        SlauBatch existingBatch = slauBatchService.getSlauBatchBySlauIdByBatchId(
                slauBatch.getSlauId(), slauBatch.getBatchId());
        ThrowUtils.throwIf(existingBatch != null, ErrorCode.PARAMS_ERROR, "该批号已存在");

        int result = slauBatchService.saveSlauBatch(slauBatch);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "保存失败");

        return ResultUtils.success(result);
    }

    /**
     * 编辑产品批号信息
     * @param slauBatch 产品批号信息
     * @return 更新结果
     */
    @PutMapping("/edit")
    public BaseResponse<Integer> editSlauBatch(@RequestBody SlauBatch slauBatch) {
        ThrowUtils.throwIf(slauBatch == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(slauBatch.getSbId() == null || slauBatch.getSbId() <= 0,
                ErrorCode.PARAMS_ERROR, "产品批号ID不能为空");

        // 检查记录是否存在
        SlauBatch existingBatch = slauBatchService.getSlauBatchById(slauBatch.getSbId());
        ThrowUtils.throwIf(existingBatch == null, ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = slauBatchService.editSlauBatch(slauBatch);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "更新失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据ID删除产品批号信息
     * @param sbId 产品批号ID
     * @return 删除结果
     */
    @DeleteMapping("/remove/{sbId}")
    public BaseResponse<Integer> removeSlauBatchById(@PathVariable Integer sbId) {
        ThrowUtils.throwIf(sbId == null || sbId <= 0, ErrorCode.PARAMS_ERROR, "产品批号ID不能为空");

        // 检查记录是否存在
        SlauBatch existingBatch = slauBatchService.getSlauBatchById(sbId);
        ThrowUtils.throwIf(existingBatch == null, ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = slauBatchService.removeSlauBatchById(sbId);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "删除失败");

        return ResultUtils.success(result);
    }

    /**
     * 确认操作（将批发商企业产品批号中的状态更新为已确认状态）
     * @param wbId 批发商企业产品批号ID
     * @return 更新结果
     */
    @PutMapping("/confirm/{wbId}")
    public BaseResponse<Integer> confirmWholBatch(@PathVariable Integer wbId) {
        ThrowUtils.throwIf(wbId == null || wbId <= 0,
                ErrorCode.PARAMS_ERROR, "批发商企业产品批号ID不能为空");

        // 检查记录是否存在
        WholBatch existingBatch = wholBatchService.getById(wbId);
        ThrowUtils.throwIf(existingBatch == null,
                ErrorCode.NOT_FOUND_ERROR, "批发商企业产品批号信息不存在");

        // 检查当前状态是否为待确认
        ThrowUtils.throwIf(existingBatch.getState() != 2,
                ErrorCode.OPERATION_ERROR, "只有待确认状态的批号才能进行确认操作");

        String confirm = "已确认";
        int result = wholBatchService.confirmWholBatch(wbId, confirm);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "确认操作失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据省市查询养殖企业信息
     */
    @GetMapping("/create/farms")
    public BaseResponse<List<FarmDto>> getFarmsByArea(
            @RequestParam Integer provId,
            @RequestParam Integer cityId) {
        ThrowUtils.throwIf(provId == null || provId <= 0, ErrorCode.PARAMS_ERROR, "省份ID不能为空");
        ThrowUtils.throwIf(cityId == null || cityId <= 0, ErrorCode.PARAMS_ERROR, "城市ID不能为空");

        List<FarmDto> farms = farmBatchService.getFarmsByArea(provId, cityId);
        return ResultUtils.success(farms);
    }

    /**
     * 根据养殖企业ID查询产品批号信息
     */
    @GetMapping("/create/farmBatches/{farmId}")
    public BaseResponse<List<FarmBatchSimpleDto>> getFarmBatchesByFarm(@PathVariable Integer farmId) {
        ThrowUtils.throwIf(farmId == null || farmId <= 0, ErrorCode.PARAMS_ERROR, "养殖企业ID不能为空");

        List<FarmBatchSimpleDto> batches = farmBatchService.getFarmBatchesByFarmId(farmId);
        return ResultUtils.success(batches);
    }

    /**
     * 检查产品批号是否已存在 - 复用现有方法
     */
    @GetMapping("/create/checkBatchId")
    public BaseResponse<Boolean> checkBatchIdExists(
            @RequestParam Integer slauId,
            @RequestParam String batchId) {
        ThrowUtils.throwIf(slauId == null || slauId <= 0, ErrorCode.PARAMS_ERROR, "屠宰企业ID不能为空");
        ThrowUtils.throwIf(batchId == null || batchId.trim().isEmpty(), ErrorCode.PARAMS_ERROR, "产品批号不能为空");

        // 复用现有的检查方法
        boolean exists = slauBatchService.getSlauBatchBySlauIdByBatchId(slauId, batchId.trim()) != null;
        return ResultUtils.success(exists);
    }

    /**
     * 新建屠宰企业产品批号 - 复用现有的保存方法
     */
    @PostMapping("/create")
    public BaseResponse<Integer> createSlauBatch(@RequestBody CreateSlauBatchRequestDto requestDto) {
        ThrowUtils.throwIf(requestDto == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(requestDto.getBatchId() == null || requestDto.getBatchId().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "产品批号不能为空");
        ThrowUtils.throwIf(requestDto.getSlauId() == null || requestDto.getSlauId() <= 0,
                ErrorCode.PARAMS_ERROR, "屠宰企业ID不能为空");
        ThrowUtils.throwIf(requestDto.getFbId() == null || requestDto.getFbId() <= 0,
                ErrorCode.PARAMS_ERROR, "养殖企业产品批号ID不能为空");
        ThrowUtils.throwIf(requestDto.getType() == null || requestDto.getType().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "产品品种不能为空");
        ThrowUtils.throwIf(requestDto.getQuaId() == null || requestDto.getQuaId().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "检验检疫合格证编号不能为空");
        ThrowUtils.throwIf(requestDto.getTestName() == null || requestDto.getTestName().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "检验员名称不能为空");

        // 检查批号是否已存在 - 复用现有方法
        boolean exists = slauBatchService.getSlauBatchBySlauIdByBatchId(requestDto.getSlauId(), requestDto.getBatchId()) != null;
        ThrowUtils.throwIf(exists, ErrorCode.PARAMS_ERROR, "该产品批号已存在");

        // 创建SlauBatch对象并保存 - 复用现有的save方法
        SlauBatch slauBatch = new SlauBatch();
        slauBatch.setBatchId(requestDto.getBatchId());
        slauBatch.setSlauId(requestDto.getSlauId());
        slauBatch.setFbId(requestDto.getFbId());
        slauBatch.setType(requestDto.getType());
        slauBatch.setQuaId(requestDto.getQuaId());
        slauBatch.setTestName(requestDto.getTestName());
        slauBatch.setRemarks(requestDto.getRemarks());
        // 设置默认的批号录入日期和状态
        slauBatch.setBatchDate(LocalDateTime.now());
        slauBatch.setState(1);

        int result = slauBatchService.saveSlauBatch(slauBatch);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "新建失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据屠宰产品批号ID查询详情--更新
     */
    @GetMapping("/detailBatch/{sbId}")
    public BaseResponse<SlauBatchDetailDto> getSlauBatchDetail(@PathVariable Integer sbId) {
        SlauBatchDetailDto detailDto = slauBatchService.getSlauBatchDetailById(sbId);
        return ResultUtils.success(detailDto);
    }

    /**
     * 屠宰企业发送确认请求给养殖企业
     */
    @PostMapping("/slau-to-farm/{sbId}")
    public BaseResponse<String> sendSlauToFarmConfirm(@PathVariable Integer sbId) {
        slauBatchService.sendConfirmToFarm(sbId);
        return ResultUtils.success("确认请求发送成功");
    }

    /**
     * 根据屠宰产品批号ID查询详情--查看
     */
    @GetMapping("/detail/{sbId}")
    public BaseResponse<SlauDetailsDto> getSlauDetail(@PathVariable Integer sbId) {
        ThrowUtils.throwIf(sbId == null || sbId <= 0,
                ErrorCode.PARAMS_ERROR, "屠宰产品批号ID不能为空");

        SlauDetailsDto detailDto = slauBatchService.getSlauDetailById(sbId);
        return ResultUtils.success(detailDto);
    }

    /**
     * 根据状态查询屠宰企业产品批号列表
     * @param slauId 屠宰企业ID
     * @param state 状态（可选）
     * @return 产品批号列表
     */
    @GetMapping("/byState/{slauId}")
    public BaseResponse<List<SlauStateDto>> getSlauBatchByState(
            @PathVariable Integer slauId,
            @RequestParam(required = false) Integer state) {
        ThrowUtils.throwIf(slauId == null || slauId <= 0, ErrorCode.PARAMS_ERROR, "屠宰企业ID不能为空");

        // 验证状态参数
        if (state != null && !BatchStateUtils.isValidOtherState(state)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的状态参数");
        }

        SlauQueryDto queryDto = new SlauQueryDto();
        queryDto.setSlauId(slauId);
        queryDto.setState(state);

        List<SlauStateDto> slauBatchList = slauBatchService.getSlauBatchByState(queryDto);
        return ResultUtils.success(slauBatchList);
    }

    /**
     * 查询批发商企业的待确认状态产品批号列表
     * @param wholName 批发商企业名称（可选）
     * @return 待确认状态产品批号列表
     */
    @GetMapping("/pending")
    public BaseResponse<List<WholNameDto>> getPendingWholBatch(
            @RequestParam(required = false) String wholName) {

        List<WholNameDto> wholBatchList = slauBatchService.getPendingWholBatchByWholName(
                wholName != null ? wholName.trim() : null
        );
        return ResultUtils.success(wholBatchList);
    }

    /**
     * 下架屠宰企业产品批号
     * @param sbId 屠宰企业产品批号ID
     * @return 下架结果
     */
    @PutMapping("/offline/{sbId}")
    public BaseResponse<Integer> offlineSlauBatch(@PathVariable Integer sbId) {
        ThrowUtils.throwIf(sbId == null || sbId <= 0,
                ErrorCode.PARAMS_ERROR, "屠宰企业产品批号ID不能为空");

        // 检查记录是否存在
        SlauBatch existingBatch = slauBatchService.getSlauBatchById(sbId);
        ThrowUtils.throwIf(existingBatch == null,
                ErrorCode.NOT_FOUND_ERROR, "产品批号信息不存在");

        int result = slauBatchService.offlineSlauBatch(sbId);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "下架失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据屠宰企业ID查询企业详情
     * @param slauId 屠宰企业ID
     * @return 企业详情
     */
    @GetMapping("/slauInfo/{slauId}")
    public BaseResponse<NodeInfoDetailsDto> getSlauInfoById(@PathVariable Integer slauId) {
        ThrowUtils.throwIf(slauId == null || slauId <= 0,
                ErrorCode.PARAMS_ERROR, "屠宰企业ID不能为空");

        NodeInfoDetailsDto slauInfo = slauBatchService.getSlauInfoById(slauId);
        return ResultUtils.success(slauInfo);
    }

    /**
     * 根据屠宰企业ID查询下游批发商企业名称列表
     * @param slauId 屠宰企业ID
     * @return 批发商企业名称列表
     */
    @GetMapping("/downstream/wholesalers/{slauId}")
    public BaseResponse<List<String>> getDownstreamWholesalers(@PathVariable Integer slauId) {
        ThrowUtils.throwIf(slauId == null || slauId <= 0,
                ErrorCode.PARAMS_ERROR, "屠宰企业ID不能为空");

        List<String> wholesalerNames = slauBatchService.getDownstreamWholesalerNames(slauId);
        return ResultUtils.success(wholesalerNames);
    }
}