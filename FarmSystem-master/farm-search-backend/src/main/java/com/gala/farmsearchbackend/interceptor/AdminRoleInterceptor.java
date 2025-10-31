package com.gala.farmsearchbackend.interceptor;
//验证角色权限拦截器

import com.gala.farmsearchbackend.constants.AdminRoleConstant;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminRoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否需要高级管理员权限的接口
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // 注册管理员接口需要高级管理员权限
        if (requestURI.equals("/api/admin/register") && "POST".equals(method)) {
            return checkAdminPrivilege(request);
        }

        // 更新管理员接口需要高级管理员权限
        if (requestURI.equals("/api/admin/update") && "PUT".equals(method)) {
            return checkAdminPrivilege(request);
        }

        // 删除管理员接口需要高级管理员权限
        if (requestURI.matches("/api/admin/delete/\\d+") && "DELETE".equals(method)) {
            return checkAdminPrivilege(request);
        }

        // 查询管理员列表接口需要高级管理员权限
        if (requestURI.equals("/api/admin/list") && "GET".equals(method)) {
            return checkAdminPrivilege(request);
        }

        // 重置管理员密码接口需要高级管理员权限
        if (requestURI.equals("/api/admin/resetPassword") && "POST".equals(method)) {
            return checkAdminPrivilege(request);
        }

        return true;
    }

    private boolean checkAdminPrivilege(HttpServletRequest request) {
        // 直接从请求属性中获取角色信息
        String role = (String) request.getAttribute("role");
        String userType = (String) request.getAttribute("userType");

        // 验证用户类型必须是管理员
        if (!"admin".equals(userType)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "该接口仅限管理员访问");
        }

        // 验证角色权限 - 需要高级管理员权限
        if (role == null || !AdminRoleConstant.hasAdminPrivilege(role)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "需要高级管理员权限");
        }

        return true;
    }
}