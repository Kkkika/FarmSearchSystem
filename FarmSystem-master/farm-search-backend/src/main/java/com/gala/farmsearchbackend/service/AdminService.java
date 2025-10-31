package com.gala.farmsearchbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gala.farmsearchbackend.model.domain.Admin;
import com.gala.farmsearchbackend.model.dto.adminDto.AdminRegisterRequest;
import com.gala.farmsearchbackend.model.dto.adminDto.AdminWithoutPasswordDTO;

import java.util.List;


/**
* @author Gala
* @description 针对表【admin(系统管理员表)】的数据库操作Service
* @createDate 2025-09-20 20:07:08
*/
public interface AdminService extends IService<Admin> {

    Admin getAdminByUsernameAndPassword(String adminName, String adminPassword);

    boolean updateAdminPassword(Integer adminId, String oldPassword, String newPassword);

    /**
     * 注册新管理员（需要高级管理员权限）
     * @param registerRequest 注册请求
     * @param currentAdminId 当前管理员ID
     * @return 注册结果
     */
    boolean registerAdmin(AdminRegisterRequest registerRequest, Integer currentAdminId);

    /**
     * 根据管理员ID获取角色
     * @param adminId 管理员ID
     * @return 管理员角色
     */
    String getAdminRole(Integer adminId);

    /**
     * 检查是否具有高级管理员权限
     * @param adminId 管理员ID
     * @return 是否具有高级管理员权限
     */
    boolean hasAdminPrivilege(Integer adminId);

    /**
     * 更新管理员信息
     * @param admin 管理员信息
     * @param currentAdminId 当前管理员ID
     * @return 更新结果
     */
    boolean updateAdmin(Admin admin, Integer currentAdminId);

    /**
     * 删除管理员
     * @param adminId 要删除的管理员ID
     * @param currentAdminId 当前管理员ID
     * @return 删除结果
     */
    boolean deleteAdmin(Integer adminId, Integer currentAdminId);

    /**
     * 获取所有管理员列表（不包含密码）
     * @return 不包含密码的管理员列表
     */
    /**
     * 获取所有管理员列表（不包含密码）
     * @param currentAdminId 当前管理员ID（用于权限验证）
     * @return 不包含密码的管理员列表
     */
    List<AdminWithoutPasswordDTO> getAllAdminsWithoutPassword(Integer currentAdminId);

    /**
     * 重置管理员密码（高级管理员权限）
     * @param adminId 要重置密码的管理员ID
     * @param currentAdminId 当前管理员ID
     * @return 重置后的新密码
     */
    String resetAdminPassword(Integer adminId, Integer currentAdminId);
}
