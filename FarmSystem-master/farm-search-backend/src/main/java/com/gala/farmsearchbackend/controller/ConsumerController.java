package com.gala.farmsearchbackend.controller;

import com.gala.farmsearchbackend.annotation.RateLimit;
import com.gala.farmsearchbackend.common.BaseResponse;
import com.gala.farmsearchbackend.common.ResultUtils;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.exception.ThrowUtils;
import com.gala.farmsearchbackend.model.dto.traceDto.TraceInfoDto;
import com.gala.farmsearchbackend.service.TraceCodeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
消费者接口
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    @Resource
    private TraceCodeService traceCodeService;

    /**
     * 消费者输入溯源码获取溯源信息
     */
    @GetMapping("/trace/{sourceId}")
    @RateLimit(key = "trace_query:", time = 60, count = 30, limitType = RateLimit.LimitType.IP)
    public BaseResponse<TraceInfoDto> getProductTraceInfo(@PathVariable String sourceId) {
        ThrowUtils.throwIf(sourceId == null || sourceId.trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "溯源码不能为空");

        TraceInfoDto traceInfo = traceCodeService.getTraceInfoBySourceId(sourceId);
        return ResultUtils.success(traceInfo);
    }

}
