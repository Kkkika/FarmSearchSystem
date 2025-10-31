package com.gala.farmsearchbackend.controller;
import com.gala.farmsearchbackend.annotation.RateLimit;
import com.gala.farmsearchbackend.constants.AdminRoleConstant;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.model.dto.adminDto.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gala.farmsearchbackend.common.BaseResponse;
import com.gala.farmsearchbackend.common.ResultUtils;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.exception.ThrowUtils;
import com.gala.farmsearchbackend.model.domain.Admin;
import com.gala.farmsearchbackend.service.AdminService;
import com.gala.farmsearchbackend.utils.JwtTokenUtil;
import com.gala.farmsearchbackend.config.JwtConfig;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
系统管理员接口
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private JwtConfig jwtConfig;

    @PostMapping("/login")
    @RateLimit(key = "admin_login:", time = 300, count = 5, limitType = RateLimit.LimitType.IP)
    public BaseResponse<LoginResponse> adminLogin(@RequestBody AdminLoginRequest loginRequest) {
        ThrowUtils.throwIf(loginRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(loginRequest.getAdminName() == null || loginRequest.getAdminName().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "管理员名称不能为空");
        ThrowUtils.throwIf(loginRequest.getAdminPassword() == null || loginRequest.getAdminPassword().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "密码不能为空");

        Admin admin = adminService.getAdminByUsernameAndPassword(
                loginRequest.getAdminName(),
                loginRequest.getAdminPassword()
        );

        ThrowUtils.throwIf(admin == null, ErrorCode.PARAMS_ERROR, "管理员名称或密码错误");

        String accessToken = jwtTokenUtil.generateAdminToken(admin.getAdminId(), admin.getAdminName(), admin.getRole());
        String refreshToken = jwtTokenUtil.generateRefreshToken(admin.getAdminId(), "admin");

        LoginResponse response = new LoginResponse();
        response.setUserId(admin.getAdminId());
        response.setUsername(admin.getAdminName());
        response.setUserType(admin.getRole());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtConfig.getExpiration());
        response.setRefreshExpiresIn(jwtConfig.getRefreshExpiration());
        response.setMessage("管理员登录成功");

        return ResultUtils.success(response);
    }

    @PostMapping("/refreshToken")
    public BaseResponse<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest refreshRequest) {
        ThrowUtils.throwIf(refreshRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(refreshRequest.getRefreshToken() == null,
                ErrorCode.PARAMS_ERROR, "刷新令牌不能为空");

        try {
            if (!jwtTokenUtil.isRefreshToken(refreshRequest.getRefreshToken())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的刷新令牌");
            }

            if (jwtTokenUtil.isTokenExpired(refreshRequest.getRefreshToken())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "刷新令牌已过期");
            }

            Integer userId = jwtTokenUtil.getUserIdFromToken(refreshRequest.getRefreshToken());
            String userType = jwtTokenUtil.getUserTypeFromToken(refreshRequest.getRefreshToken());

            if (!"admin".equals(userType)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的令牌类型");
            }

            Admin admin = adminService.getById(userId);
            ThrowUtils.throwIf(admin == null, ErrorCode.NOT_FOUND_ERROR, "管理员不存在");

            String newAccessToken = jwtTokenUtil.generateAdminToken(admin.getAdminId(), admin.getAdminName(), admin.getRole());
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(admin.getAdminId(), "admin");

            LoginResponse response = new LoginResponse();
            response.setUserId(admin.getAdminId());
            response.setUsername(admin.getAdminName());
            response.setUserType("admin");
            response.setAccessToken(newAccessToken);
            response.setRefreshToken(newRefreshToken);
            response.setExpiresIn(jwtConfig.getExpiration());
            response.setRefreshExpiresIn(jwtConfig.getRefreshExpiration());
            response.setMessage("令牌刷新成功");

            return ResultUtils.success(response);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "刷新令牌失败");
        }
    }

    @PostMapping("/updatePassword")
    public BaseResponse<Boolean> updateAdminPassword(@RequestBody AdminLoginRequest passwordRequest) {
        ThrowUtils.throwIf(passwordRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(passwordRequest.getAdminId() == null,
                ErrorCode.PARAMS_ERROR, "管理员ID不能为空");
        ThrowUtils.throwIf(passwordRequest.getOldPassword() == null || passwordRequest.getOldPassword().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "原密码不能为空");
        ThrowUtils.throwIf(passwordRequest.getNewPassword() == null || passwordRequest.getNewPassword().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "新密码不能为空");

        boolean result = adminService.updateAdminPassword(
                passwordRequest.getAdminId(),
                passwordRequest.getOldPassword(),
                passwordRequest.getNewPassword()
        );

        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "密码修改失败");
        return ResultUtils.success(true);
    }

    /**
     * 获取当前管理员信息（不返回密码）
     */
    @GetMapping("/profile")
    public BaseResponse<AdminWithoutPasswordDTO> getAdminProfile(HttpServletRequest request) {
        Integer adminId = (Integer) request.getAttribute("userId");
        Admin admin = adminService.getById(adminId);
        ThrowUtils.throwIf(admin == null, ErrorCode.NOT_FOUND_ERROR, "管理员不存在");

        // 转换为不包含密码的DTO
        AdminWithoutPasswordDTO profileDTO = new AdminWithoutPasswordDTO();
        profileDTO.setAdminId(admin.getAdminId());
        profileDTO.setAdminName(admin.getAdminName());
        profileDTO.setRole(admin.getRole());
        profileDTO.setRemarks(admin.getRemarks());

        return ResultUtils.success(profileDTO);
    }

    /**
     * 注册新管理员（需要高级管理员权限）
     * @param registerRequest 注册请求
     * @param request HTTP请求
     * @return 注册结果
     */
    /**
     * 注册新管理员（需要高级管理员权限）
     */
    @PostMapping("/register")
    public BaseResponse<Boolean> registerAdmin(@RequestBody AdminRegisterRequest registerRequest,
                                               HttpServletRequest request) {
        ThrowUtils.throwIf(registerRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(registerRequest.getAdminName() == null || registerRequest.getAdminName().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "管理员名称不能为空");
        ThrowUtils.throwIf(registerRequest.getAdminPassword() == null || registerRequest.getAdminPassword().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "密码不能为空");

        // 从请求属性中获取当前管理员ID和角色
        Integer currentAdminId = (Integer) request.getAttribute("userId");
        String currentUserType = (String) request.getAttribute("userType");
        String currentRole = (String) request.getAttribute("role");

        ThrowUtils.throwIf(currentAdminId == null, ErrorCode.NO_AUTH_ERROR, "未登录或登录已过期");
        ThrowUtils.throwIf(!"admin".equals(currentUserType), ErrorCode.NO_AUTH_ERROR, "该接口仅限管理员访问");
        ThrowUtils.throwIf(!AdminRoleConstant.hasAdminPrivilege(currentRole),
                ErrorCode.NO_AUTH_ERROR, "需要高级管理员权限");

        boolean result = adminService.registerAdmin(registerRequest, currentAdminId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "管理员注册失败");

        return ResultUtils.success(true);
    }

    /**
     * 获取当前管理员角色信息
     * @param request HTTP请求
     * @return 角色信息
     */
    @GetMapping("/role")
    public BaseResponse<String> getAdminRole(HttpServletRequest request) {
        Integer adminId = (Integer) request.getAttribute("userId");
        ThrowUtils.throwIf(adminId == null, ErrorCode.NO_AUTH_ERROR, "未登录或登录已过期");

        String role = adminService.getAdminRole(adminId);
        ThrowUtils.throwIf(role == null, ErrorCode.NOT_FOUND_ERROR, "管理员不存在");

        return ResultUtils.success(role);
    }

    /**
     * 删除管理员（需要高级管理员权限）
     * @param adminId 要删除的管理员ID
     * @param request HTTP请求
     * @return 删除结果
     */
    @DeleteMapping("/delete/{adminId}")
    public BaseResponse<Boolean> deleteAdmin(@PathVariable Integer adminId, HttpServletRequest request) {
        ThrowUtils.throwIf(adminId == null || adminId <= 0, ErrorCode.PARAMS_ERROR, "管理员ID不能为空");

        // 从请求中获取当前管理员ID
        Integer currentAdminId = (Integer) request.getAttribute("userId");
        ThrowUtils.throwIf(currentAdminId == null, ErrorCode.NO_AUTH_ERROR, "未登录或登录已过期");

        boolean result = adminService.deleteAdmin(adminId, currentAdminId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "管理员删除失败");

        return ResultUtils.success(true);
    }

    /**
     * 更新管理员信息（需要高级管理员权限）
     * @param admin 管理员信息
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/update")
    public BaseResponse<Boolean> updateAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        ThrowUtils.throwIf(admin == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(admin.getAdminId() == null, ErrorCode.PARAMS_ERROR, "管理员ID不能为空");

        // 从请求中获取当前管理员ID
        Integer currentAdminId = (Integer) request.getAttribute("userId");
        ThrowUtils.throwIf(currentAdminId == null, ErrorCode.NO_AUTH_ERROR, "未登录或登录已过期");

        boolean result = adminService.updateAdmin(admin, currentAdminId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "管理员更新失败");

        return ResultUtils.success(true);
    }

    /**
     * 获取所有管理员列表（需要高级管理员权限，不返回密码）
     * @return 不包含密码的管理员列表
     */
    @GetMapping("/list")
    public BaseResponse<List<AdminWithoutPasswordDTO>> getAllAdmins(HttpServletRequest request) {
        // 验证登录状态
        Integer currentAdminId = (Integer) request.getAttribute("userId");
        ThrowUtils.throwIf(currentAdminId == null, ErrorCode.NO_AUTH_ERROR, "未登录或登录已过期");

        List<AdminWithoutPasswordDTO> admins = adminService.getAllAdminsWithoutPassword(currentAdminId);
        return ResultUtils.success(admins);
    }

    /**
     * 重置管理员密码（需要高级管理员权限）
     * @param resetRequest 重置请求
     * @param request HTTP请求
     * @return 重置结果和新密码
     */
    @PostMapping("/resetPassword")
    public BaseResponse<ResetPasswordResponse> resetAdminPassword(@RequestBody ResetPasswordRequest resetRequest,
                                                                  HttpServletRequest request) {
        ThrowUtils.throwIf(resetRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(resetRequest.getAdminId() == null, ErrorCode.PARAMS_ERROR, "管理员ID不能为空");

        // 从请求中获取当前管理员ID
        Integer currentAdminId = (Integer) request.getAttribute("userId");
        ThrowUtils.throwIf(currentAdminId == null, ErrorCode.NO_AUTH_ERROR, "未登录或登录已过期");

        // 重置密码
        String newPassword = adminService.resetAdminPassword(resetRequest.getAdminId(), currentAdminId);

        // 获取管理员信息
        Admin admin = adminService.getById(resetRequest.getAdminId());

        // 构建响应
        ResetPasswordResponse response = new ResetPasswordResponse();
        response.setAdminId(admin.getAdminId());
        response.setAdminName(admin.getAdminName());
        response.setNewPassword(newPassword);

        return ResultUtils.success(response);
    }
}
