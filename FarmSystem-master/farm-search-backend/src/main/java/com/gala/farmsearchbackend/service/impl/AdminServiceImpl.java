package com.gala.farmsearchbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gala.farmsearchbackend.constants.AdminRoleConstant;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.mapper.AdminMapper;
import com.gala.farmsearchbackend.model.domain.Admin;
import com.gala.farmsearchbackend.model.dto.adminDto.AdminRegisterRequest;
import com.gala.farmsearchbackend.model.dto.adminDto.AdminWithoutPasswordDTO;
import com.gala.farmsearchbackend.service.AdminService;

import com.gala.farmsearchbackend.utils.PasswordEncoderUtil;
import com.gala.farmsearchbackend.utils.PasswordPolicyValidator;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author Gala
* @description 针对表【admin(系统管理员表)】的数据库操作Service实现
* @createDate 2025-09-20 20:07:08
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService {
    @Resource
    private PasswordEncoderUtil passwordEncoderUtil;

    @Resource
    private PasswordPolicyValidator passwordPolicyValidator;

    @Override
    public Admin getAdminByUsernameAndPassword(String adminName, String adminPassword) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getAdminName, adminName);
        Admin admin = getOne(queryWrapper);

        if (admin == null) {
            return null;
        }

        if (!passwordEncoderUtil.matches(adminPassword, admin.getAdminPassword())) {
            return null;
        }

        return admin;
    }

    @Override
    public boolean updateAdminPassword(Integer adminId, String oldPassword, String newPassword) {
        Admin admin = getById(adminId);
        if (admin == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "管理员不存在");
        }

        if (!passwordEncoderUtil.matches(oldPassword, admin.getAdminPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "原密码错误");
        }

        passwordPolicyValidator.validatePassword(newPassword);
        passwordPolicyValidator.validateNotSameAsOld(newPassword, oldPassword);

        String encodedNewPassword = passwordEncoderUtil.encodePassword(newPassword);

        Admin updateAdmin = new Admin();
        updateAdmin.setAdminId(adminId);
        updateAdmin.setAdminPassword(encodedNewPassword);

        return updateById(updateAdmin);
    }

    public boolean createDefaultAdmin(String adminName, String rawPassword) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getAdminName, adminName);
        if (getOne(queryWrapper) != null) {
            return false;
        }

        passwordPolicyValidator.validatePassword(rawPassword);
        String encodedPassword = passwordEncoderUtil.encodePassword(rawPassword);

        Admin admin = new Admin();
        admin.setAdminName(adminName);
        admin.setAdminPassword(encodedPassword);
        admin.setRemarks("系统默认管理员");

        return save(admin);
    }

    @Override
    public boolean registerAdmin(AdminRegisterRequest registerRequest, Integer currentAdminId) {
        // 1. 验证当前管理员是否存在且具有高级管理员权限
        Admin currentAdmin = getById(currentAdminId);
        if (currentAdmin == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "当前管理员不存在");
        }

        // 检查权限 - 只有高级管理员可以注册新管理员
        if (!AdminRoleConstant.hasAdminPrivilege(currentAdmin.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "权限不足，需要高级管理员权限");
        }

        // 2. 验证用户名是否已存在
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getAdminName, registerRequest.getAdminName());
        if (getOne(queryWrapper) != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "管理员名称已存在");
        }

        // 3. 验证密码策略
        passwordPolicyValidator.validatePassword(registerRequest.getAdminPassword());

        // 4. 设置默认角色（普通管理员）
        String role = registerRequest.getRole();
        if (role == null || role.trim().isEmpty()) {
            role = AdminRoleConstant.NORMAL; // 默认创建普通管理员
        } else {
            // 验证角色值是否有效
            if (!AdminRoleConstant.ADMIN.equals(role) && !AdminRoleConstant.NORMAL.equals(role)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的角色类型");
            }
        }

        // 5. 创建管理员
        String encodedPassword = passwordEncoderUtil.encodePassword(registerRequest.getAdminPassword());

        Admin admin = new Admin();
        admin.setAdminName(registerRequest.getAdminName());
        admin.setAdminPassword(encodedPassword);
        admin.setRole(role);
        admin.setRemarks(registerRequest.getRemarks());

        return save(admin);
    }

    @Override
    public String getAdminRole(Integer adminId) {
        Admin admin = getById(adminId);
        return admin != null ? admin.getRole() : null;
    }

    @Override
    public boolean hasAdminPrivilege(Integer adminId) {
        String role = getAdminRole(adminId);
        return AdminRoleConstant.hasAdminPrivilege(role);
    }

    @Override
    public boolean deleteAdmin(Integer adminId, Integer currentAdminId) {
        // 1. 验证当前管理员权限
        Admin currentAdmin = getById(currentAdminId);
        if (currentAdmin == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "当前管理员不存在");
        }

        // 只有高级管理员可以删除管理员
        if (!AdminRoleConstant.hasAdminPrivilege(currentAdmin.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "权限不足，需要高级管理员权限");
        }

        // 2. 不能删除自己
        if (adminId.equals(currentAdminId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不能删除自己的账户");
        }

        // 3. 验证要删除的管理员是否存在
        Admin targetAdmin = getById(adminId);
        if (targetAdmin == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "要删除的管理员不存在");
        }

        // 4. 权限验证：普通管理员不能删除高级管理员
        if (AdminRoleConstant.hasAdminPrivilege(targetAdmin.getRole()) &&
                !AdminRoleConstant.hasAdminPrivilege(currentAdmin.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "普通管理员不能删除高级管理员");
        }

        return removeById(adminId);
    }

    @Override
    public boolean updateAdmin(Admin admin, Integer currentAdminId) {
        // 1. 验证当前管理员权限
        Admin currentAdmin = getById(currentAdminId);
        if (currentAdmin == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "当前管理员不存在");
        }

        // 只有高级管理员可以更新管理员信息
        if (!AdminRoleConstant.hasAdminPrivilege(currentAdmin.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "权限不足，需要高级管理员权限");
        }

        // 2. 验证要更新的管理员是否存在
        Admin targetAdmin = getById(admin.getAdminId());
        if (targetAdmin == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "要更新的管理员不存在");
        }

        // 3. 验证用户名唯一性（如果修改了用户名）
        if (!targetAdmin.getAdminName().equals(admin.getAdminName())) {
            LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Admin::getAdminName, admin.getAdminName());
            if (getOne(queryWrapper) != null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "管理员名称已存在");
            }
        }

        // 4. 权限验证：普通管理员不能修改高级管理员的信息
        if (AdminRoleConstant.hasAdminPrivilege(targetAdmin.getRole()) &&
                !AdminRoleConstant.hasAdminPrivilege(currentAdmin.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "普通管理员不能修改高级管理员信息");
        }

        // 5. 创建更新对象，排除密码字段
        Admin updateAdmin = new Admin();
        updateAdmin.setAdminId(admin.getAdminId());
        updateAdmin.setAdminName(admin.getAdminName());
        updateAdmin.setRole(admin.getRole());
        updateAdmin.setRemarks(admin.getRemarks());

        // 不更新密码字段，保持原密码

        return updateById(updateAdmin);
    }

    @Override
    public List<AdminWithoutPasswordDTO> getAllAdminsWithoutPassword(Integer currentAdminId) {
        // 验证当前管理员权限
        Admin currentAdmin = getById(currentAdminId);
        if (currentAdmin == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "当前管理员不存在");
        }

        // 只有高级管理员可以查询管理员列表
        if (!AdminRoleConstant.hasAdminPrivilege(currentAdmin.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "权限不足，需要高级管理员权限");
        }

        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Admin::getAdminId);
        List<Admin> admins = list(queryWrapper);

        // 转换为不包含密码的DTO
        return admins.stream()
                .map(this::convertToWithoutPasswordDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将Admin实体转换为不包含密码的DTO
     */
    private AdminWithoutPasswordDTO convertToWithoutPasswordDTO(Admin admin) {
        if (admin == null) {
            return null;
        }

        AdminWithoutPasswordDTO dto = new AdminWithoutPasswordDTO();
        dto.setAdminId(admin.getAdminId());
        dto.setAdminName(admin.getAdminName());
        dto.setRole(admin.getRole());
        dto.setRemarks(admin.getRemarks());

        return dto;
    }

    @Override
    public String resetAdminPassword(Integer adminId, Integer currentAdminId) {
        // 1. 验证当前管理员权限
        Admin currentAdmin = getById(currentAdminId);
        if (currentAdmin == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "当前管理员不存在");
        }

        // 只有高级管理员可以重置密码
        if (!AdminRoleConstant.hasAdminPrivilege(currentAdmin.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "权限不足，需要高级管理员权限");
        }

        // 2. 验证要重置密码的管理员是否存在
        Admin targetAdmin = getById(adminId);
        if (targetAdmin == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "要重置密码的管理员不存在");
        }

        // 3. 权限验证：普通管理员不能重置高级管理员的密码
        if (AdminRoleConstant.hasAdminPrivilege(targetAdmin.getRole()) &&
                !AdminRoleConstant.hasAdminPrivilege(currentAdmin.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "普通管理员不能重置高级管理员密码");
        }

        // 4. 生成新密码：管理员名称 + 4位随机数字
        String newPassword = generateRandomPassword(targetAdmin.getAdminName());

        // 5. 验证密码策略
        try {
            passwordPolicyValidator.validatePassword(newPassword);
        } catch (BusinessException e) {
            // 如果生成的密码不符合策略，重新生成
            newPassword = generateStrongRandomPassword(targetAdmin.getAdminName());
        }

        // 6. 更新密码
        String encodedPassword = passwordEncoderUtil.encodePassword(newPassword);

        Admin updateAdmin = new Admin();
        updateAdmin.setAdminId(adminId);
        updateAdmin.setAdminPassword(encodedPassword);

        boolean updateResult = updateById(updateAdmin);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "密码重置失败");
        }

        return newPassword;
    }

    /**
     * 生成随机密码：管理员名称 + 4位随机数字
     */
    private String generateRandomPassword(String adminName) {
        // 生成4位随机数字
        int randomNum = (int) (Math.random() * 9000) + 1000;
        return adminName + randomNum;
    }

    /**
     * 生成更强的随机密码（如果简单密码不符合策略）
     */
    private String generateStrongRandomPassword(String adminName) {
        // 生成6位随机数字和字母组合
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randomPart.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return adminName + randomPart.toString();
    }
}




