// SimpleWebSocketService.java
package com.gala.farmsearchbackend.service;

import com.gala.farmsearchbackend.config.SimpleWebSocketHandler;
import com.gala.farmsearchbackend.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 简化的WebSocket服务
 */

@Service
public class SimpleWebSocketService {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 发送确认请求提示
     */
    public void sendConfirmAlert(Integer toEnterpriseId, String fromEnterpriseName) {
        try {
            SimpleWebSocketHandler.sendConfirmAlert(toEnterpriseId, fromEnterpriseName);
            log.info("确认提示发送成功，目标企业ID: {}，来源企业: {}", toEnterpriseId, fromEnterpriseName);
        } catch (Exception e) {
            log.error("发送确认提示失败，目标企业ID: {}", toEnterpriseId, e);
        }
    }
}