package com.gala.farmsearchbackend.config;

import com.gala.farmsearchbackend.interceptor.AdminRoleInterceptor;
import com.gala.farmsearchbackend.interceptor.JwtTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;
    private final AdminRoleInterceptor adminRoleInterceptor;

    public WebConfig(JwtTokenInterceptor jwtTokenInterceptor,
                     AdminRoleInterceptor adminRoleInterceptor) {
        this.jwtTokenInterceptor = jwtTokenInterceptor;
        this.adminRoleInterceptor = adminRoleInterceptor;
    }

    //拦截器配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // JWT token 拦截器 - 用于身份验证
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/**");
        // 管理员角色权限拦截器 - 用于权限验证
        registry.addInterceptor(adminRoleInterceptor)
                .addPathPatterns(
                        "/api/admin/register",      // 注册管理员
                        "/api/admin/update",        // 更新管理员
                        "/api/admin/delete/*",      // 删除管理员
                        "/api/adm   in/list",          // 查询管理员列表
                        "/api/admin/resetPassword"  // 重置管理员密码
                )
                .order(1); // 设置执行顺序，在JWT拦截器之后执行
    }

    //全局跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求
        registry.addMapping("/**")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 放行哪些域名（必须用 patterns，否则 * 会和 allowCredentials 冲突）
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}