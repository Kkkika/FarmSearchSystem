package com.gala.farmsearchbackend.config;

import com.gala.farmsearchbackend.exception.GlobalExceptionHandler;
import com.gala.farmsearchbackend.service.AdminService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component

public class SystemInitializer  {
//    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//    private final AdminService adminService;
//
//    public SystemInitializer(AdminService adminService) {
//        this.adminService = adminService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        initDefaultAdmin();
//    }
//
//    private void initDefaultAdmin() {
//        try {
//            boolean success = ((com.gala.farmsearchbackend.service.impl.AdminServiceImpl) adminService)
//                    .createDefaultAdmin("admin", "admin123456");
//
//            if (success) {
//                log.info("默认管理员创建成功 - 用户名: admin, 密码: admin123456");
//            } else {
//                log.info("默认管理员已存在");
//            }
//        } catch (Exception e) {
//            log.error("创建默认管理员失败", e);
//        }
//    }
}