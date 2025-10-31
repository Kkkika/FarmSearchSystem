package com.gala.farmsearchbackend.constants;

/**
 * 管理员角色常量
 */
public class AdminRoleConstant {

    /**
     * 高级管理员
     */
    public static final String ADMIN = "ADMIN";

    /**
     * 普通管理员
     */
    public static final String NORMAL = "NORMAL";

    /**
     * 检查是否具有高级管理员权限
     */
    public static boolean hasAdminPrivilege(String role) {
        return ADMIN.equals(role);
    }
}