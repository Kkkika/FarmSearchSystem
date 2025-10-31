// ConfirmRequestService.java
package com.gala.farmsearchbackend.service;

import com.gala.farmsearchbackend.exception.GlobalExceptionHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 确认请求服务
 */

@Service
public class ConfirmRequestService {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Resource
    private SimpleWebSocketService webSocketService;
    @Resource
    @Lazy
    private NodeInfoService nodeInfoService;
    @Resource
    private UpdateStateService updateStateService;

    // 构造器注入

    /**
     * 发送确认请求（通用方法）
     * @param fromEnterpriseId 发送方企业ID
     * @param toEnterpriseId 接收方企业ID
     * @param batchRecordId 批号记录ID
     * @param batchRecordType 批号记录类型：SLAU/WHOL/RETA
     */
    @Transactional
    public void sendConfirmRequest(Integer fromEnterpriseId, Integer toEnterpriseId,
                                   Integer batchRecordId, String batchRecordType) {
        try {
            // 1. 获取企业信息
            String fromEnterpriseName = nodeInfoService.getById(fromEnterpriseId).getName();

            // 2. 发送WebSocket提示信息
            webSocketService.sendConfirmAlert(toEnterpriseId, fromEnterpriseName);

            // 3. 更新批号状态为"待确认"
            updateBatchStatus(batchRecordId, batchRecordType, 2); // 2表示待确认状态

            log.info("确认请求处理完成：{} -> {}，批号记录：{}[{}]",
                    fromEnterpriseName, toEnterpriseId, batchRecordType, batchRecordId);

        } catch (Exception e) {
            log.error("发送确认请求失败", e);
            throw new RuntimeException("确认请求发送失败", e);
        }
    }

    /**
     * 更新批号状态
     */
    private void updateBatchStatus(Integer batchRecordId, String batchRecordType, Integer status) {
        switch (batchRecordType) {
            case "SLAU":
                // 更新屠宰企业批号状态为"待确认"(状态2)
                int slauResult = updateStateService.updateSlauState(batchRecordId, status);
                if (slauResult <= 0) {
                    throw new RuntimeException("更新屠宰批号状态失败");
                }
                break;
            case "WHOL":
                // 更新批发商批号状态为"待确认"(状态2)
                int wholResult = updateStateService.updateWholState(batchRecordId, status);
                if (wholResult <= 0) {
                    throw new RuntimeException("更新批发商批号状态失败");
                }
                break;
            case "RETA":
                // 更新零售商批号状态为"待确认"(状态2)
                int retaResult = updateStateService.updateRetaState(batchRecordId, status);
                if (retaResult <= 0) {
                    throw new RuntimeException("更新零售商批号状态失败");
                }
                break;
            default:
                throw new IllegalArgumentException("不支持的批号记录类型: " + batchRecordType);
        }
    }
}