package com.gala.farmsearchbackend.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        // 配置所有路径的跨域请求
//        registry.addMapping("/**")
//                // 允许所有来源的域，实际生产环境建议指定具体的域
//                .allowedOriginPatterns("*")
//                // 允许发送凭证信息（如 Cookie）
//                .allowCredentials(true)
//                // 允许所有类型的 HTTP 请求方法
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                // 允许所有请求头
//                .allowedHeaders("*")
//                // 预检请求的有效期，单位为秒
//                .maxAge(3600);
    }
}

