// TraceCodeService.java
package com.gala.farmsearchbackend.service;

import com.gala.farmsearchbackend.model.dto.traceDto.TraceInfoDto;

public interface TraceCodeService {

    /**
     * 生成溯源码并更新到零售商产品批号
     * @param rbId 零售商产品批号ID
     * @return 生成的溯源码
     */
    String generateAndSaveTraceCode(Integer rbId);

    /**
     * 根据溯源码查询溯源信息
     * @param sourceId 溯源码
     * @return 溯源信息
     */
    TraceInfoDto getTraceInfoBySourceId(String sourceId);
}