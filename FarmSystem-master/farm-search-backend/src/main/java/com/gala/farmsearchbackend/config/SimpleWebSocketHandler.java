// SimpleWebSocketHandler.java
package com.gala.farmsearchbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gala.farmsearchbackend.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SimpleWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 存储WebSocket会话：key=enterpriseId, value=WebSocketSession
    private static final Map<Integer, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer enterpriseId = extractEnterpriseIdFromSession(session);
        if (enterpriseId != null) {
            SESSIONS.put(enterpriseId, session);
            log.info("企业WebSocket连接建立，企业ID: {}，当前连接数: {}", enterpriseId, SESSIONS.size());

            // 发送连接成功消息
            sendMessage(session, createSuccessMessage("连接成功"));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理心跳消息
        if ("PING".equals(message.getPayload())) {
            sendMessage(session, createPongMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Integer enterpriseId = extractEnterpriseIdFromSession(session);
        if (enterpriseId != null) {
            SESSIONS.remove(enterpriseId);
            log.info("企业WebSocket连接关闭，企业ID: {}，当前连接数: {}", enterpriseId, SESSIONS.size());
        }
    }

    /**
     * 发送确认请求提示信息
     */
    public static void sendConfirmAlert(Integer enterpriseId, String fromEnterpriseName) {
        WebSocketSession session = SESSIONS.get(enterpriseId);
        if (session != null && session.isOpen()) {
            try {
                Map<String, Object> message = createConfirmAlertMessage(fromEnterpriseName);
                String messageJson = new ObjectMapper().writeValueAsString(message);
                session.sendMessage(new TextMessage(messageJson));
                log.info("向企业ID: {} 发送确认提示成功", enterpriseId);
            } catch (IOException e) {
                log.error("向企业ID: {} 发送确认提示失败", enterpriseId, e);
                SESSIONS.remove(enterpriseId);
            }
        } else {
            log.info("企业ID: {} 不在线，确认提示暂不发送", enterpriseId);
        }
    }

    /**
     * 创建确认提示消息
     */
    private static Map<String, Object> createConfirmAlertMessage(String fromEnterpriseName) {
        return Map.of(
                "type", "CONFIRM_ALERT",
                "message", "有新的产品批号需要出场确认，请及时处理",
                "fromEnterprise", fromEnterpriseName,
                "timestamp", System.currentTimeMillis()
        );
    }

    /**
     * 创建成功消息
     */
    private Map<String, Object> createSuccessMessage(String message) {
        return Map.of(
                "type", "CONNECTION_SUCCESS",
                "message", message,
                "timestamp", System.currentTimeMillis()
        );
    }

    /**
     * 创建心跳响应消息
     */
    private Map<String, Object> createPongMessage() {
        return Map.of(
                "type", "PONG",
                "timestamp", System.currentTimeMillis()
        );
    }

    /**
     * 发送消息
     */
    private void sendMessage(WebSocketSession session, Map<String, Object> message) throws IOException {
        String messageJson = objectMapper.writeValueAsString(message);
        session.sendMessage(new TextMessage(messageJson));
    }

    private Integer extractEnterpriseIdFromSession(WebSocketSession session) {
        try {
            String path = session.getUri().getPath();
            String[] pathSegments = path.split("/");
            return Integer.parseInt(pathSegments[pathSegments.length - 1]);
        } catch (Exception e) {
            log.error("从WebSocket session中提取企业ID失败", e);
            return null;
        }
    }
}