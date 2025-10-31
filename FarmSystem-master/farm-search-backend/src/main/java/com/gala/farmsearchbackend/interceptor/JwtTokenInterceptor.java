package com.gala.farmsearchbackend.interceptor;

import com.gala.farmsearchbackend.config.JwtConfig;
import com.gala.farmsearchbackend.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

/**
 * JWT令牌拦截器
 */
@Component
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtConfig jwtConfig;

    public JwtTokenInterceptor(JwtTokenUtil jwtTokenUtil, JwtConfig jwtConfig) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否为浏览器的CORS预检请求（OPTIONS请求），如果是，则直接放行。
        // 这必须是方法中的第一个检查，以确保预检请求不会进入后续的Token验证逻辑。
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            System.out.println("!!!!!!!!!!!!!!!!!! OPTIONS request received, allowing CORS preflight. !!!!!!!!!!!!!!!!!!");
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String requestURI = request.getRequestURI();

        // 排除特定的路径模式
        if (shouldExclude(requestURI)) {
            return true; // 直接放行
        }
        System.out.println("!!!!!!!!!!!!!!!!!! JwtTokenInterceptor IS RUNNING for URI: " + request.getRequestURI() + " !!!!!!!!!!!!!!!!!!");

        // 放行公开接口
        if (isPublicEndpoint(request.getRequestURI())) {
            return true;
        }

        String token = getTokenFromRequest(request);

        if (token == null) {
            sendErrorResponse(response, 401, "未提供访问令牌");
            return false;
        }

        try {
            if (jwtTokenUtil.isTokenExpired(token)) {
                sendErrorResponse(response, 401, "令牌已过期");
                return false;
            }

            if (jwtTokenUtil.isRefreshToken(token)) {
                sendErrorResponse(response, 401, "请使用访问令牌，而不是刷新令牌");
                return false;
            }

            // 将用户信息存入请求属性
            request.setAttribute("userId", jwtTokenUtil.getUserIdFromToken(token));
            request.setAttribute("userType", jwtTokenUtil.getUserTypeFromToken(token));
            request.setAttribute("username", jwtTokenUtil.getUsernameFromToken(token));

            // 新增：设置角色信息到请求属性
            String role = jwtTokenUtil.getRoleFromToken(token);
            if (role != null) {
                request.setAttribute("role", role);
            }
        } catch (Exception e) {
            sendErrorResponse(response, 401, "无效的访问令牌");
            return false;
        }

        return true;
    }

    private boolean shouldExclude(String requestURI) {
        // 定义需要排除的路径模式
        List<String> excludePatterns = Arrays.asList(
                "/api/admin/login",
                "/api/node/login",
                "/api/node/forgotPassword",
                "/api/admin/refreshToken",
                "/api/node/refreshToken",
                "/api/doc/",
                "/doc/",
                "/v3/api-docs/",
                "/swagger-ui/",
                "/swagger-resources/"
        );

        // 检查consumer相关路径（使用正则匹配）
        if (requestURI.startsWith("/api/consumer")) {
            return true;
        }

        // 检查其他排除路径
        for (String pattern : excludePatterns) {
            if (requestURI.startsWith(pattern)) {
                return true;
            }
        }

        return false;
    }


    private boolean isPublicEndpoint(String uri) {
        return uri.contains("/login") ||
                uri.contains("/forgotPassword") ||
                uri.contains("/refreshToken") ||
                uri.contains("/doc") ||
                uri.contains("/v3/api-docs") ||
                uri.contains("/swagger");
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtConfig.getTokenHeader());
        if (bearerToken != null && bearerToken.startsWith(jwtConfig.getTokenPrefix())) {
            return bearerToken.substring(jwtConfig.getTokenPrefix().length());
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"code\": %d, \"message\": \"%s\"}", status, message));
    }
}