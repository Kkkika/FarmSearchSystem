package com.gala.farmsearchbackend.controller;

import com.gala.farmsearchbackend.annotation.RateLimit;
import com.gala.farmsearchbackend.common.BaseResponse;
import com.gala.farmsearchbackend.common.ResultUtils;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.exception.ThrowUtils;
import com.gala.farmsearchbackend.model.domain.NodeInfo;
import com.gala.farmsearchbackend.model.dto.adminDto.LoginResponse;
import com.gala.farmsearchbackend.model.dto.adminDto.RefreshTokenRequest;
import com.gala.farmsearchbackend.model.dto.nodeDto.*;
import com.gala.farmsearchbackend.service.NodeInfoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import com.gala.farmsearchbackend.utils.JwtTokenUtil;
import com.gala.farmsearchbackend.config.JwtConfig;
import jakarta.servlet.http.HttpServletRequest;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 节点企业接口控制器
 * @author Gala
 */
@RestController
@RequestMapping("/node")
public class NodeInfoController {

    @Resource
    private NodeInfoService nodeInfoService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private JwtConfig jwtConfig;

    private static final Map<Integer, String> NODE_TYPE_MAP = new HashMap<>();
    static {
        NODE_TYPE_MAP.put(1, "farm");
        NODE_TYPE_MAP.put(2, "slaughter");
        NODE_TYPE_MAP.put(3, "wholesaler");
        NODE_TYPE_MAP.put(4, "retailer");
    }

    @PostMapping("/login")
    @RateLimit(key = "node_login:", time = 300, count = 5, limitType = RateLimit.LimitType.IP)
    public BaseResponse<LoginResponse> nodeLogin(@RequestBody NodeLoginRequest loginRequest) {
        ThrowUtils.throwIf(loginRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(loginRequest.getCode() == null || loginRequest.getCode().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "登录编码不能为空");
        ThrowUtils.throwIf(loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "密码不能为空");

        NodeInfo nodeInfo = nodeInfoService.getNodeInfoByCodeByPass(
                loginRequest.getCode(),
                loginRequest.getPassword()
        );

        ThrowUtils.throwIf(nodeInfo == null, ErrorCode.PARAMS_ERROR, "登录编码或密码错误");
        // 新增状态检查
        ThrowUtils.throwIf(nodeInfo.getStatus() == 1, ErrorCode.FORBIDDEN_ERROR, "该账户已被禁用");

        String accessToken = jwtTokenUtil.generateNodeToken(
                nodeInfo.getNodeId(),
                nodeInfo.getName(),
                nodeInfo.getType()
        );
        String refreshToken = jwtTokenUtil.generateRefreshToken(nodeInfo.getNodeId(),
                getNodeTypeString(nodeInfo.getType()));

        LoginResponse response = new LoginResponse();
        response.setUserId(nodeInfo.getNodeId());
        response.setUsername(nodeInfo.getName());
        response.setUserType(getNodeTypeString(nodeInfo.getType()));
        response.setEnterpriseType(nodeInfo.getType());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtConfig.getExpiration());
        response.setRefreshExpiresIn(jwtConfig.getRefreshExpiration());
        response.setMessage("节点企业登录成功");

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

            NodeInfo nodeInfo = nodeInfoService.getById(userId);
            ThrowUtils.throwIf(nodeInfo == null, ErrorCode.NOT_FOUND_ERROR, "节点企业不存在");

            String newAccessToken = jwtTokenUtil.generateNodeToken(
                    nodeInfo.getNodeId(),
                    nodeInfo.getName(),
                    nodeInfo.getType()
            );
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(nodeInfo.getNodeId(), userType);

            LoginResponse response = new LoginResponse();
            response.setUserId(nodeInfo.getNodeId());
            response.setUsername(nodeInfo.getName());
            response.setUserType(userType);
            response.setEnterpriseType(nodeInfo.getType());
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

    @GetMapping("/profile")
    public BaseResponse<NodeInfo> getNodeProfile(HttpServletRequest request) {
        Integer nodeId = (Integer) request.getAttribute("userId");
        NodeInfo nodeInfo = nodeInfoService.getById(nodeId);
        ThrowUtils.throwIf(nodeInfo == null, ErrorCode.NOT_FOUND_ERROR, "节点企业不存在");

        // 隐藏密码信息
        nodeInfo.setPassword(null);
        return ResultUtils.success(nodeInfo);
    }

    private String getNodeTypeString(Integer nodeType) {
        return NODE_TYPE_MAP.getOrDefault(nodeType, "unknown");
    }


    /**
     * 分页查询节点企业信息
     * @param requestDto 分页请求参数（status: 0-启用状态, 1-禁用状态, null-查询所有状态）
     * @return 分页响应结果
     */
    @PostMapping("/page")
    public BaseResponse<NodeInfoPageResponseDto> listNodeInfoPage(@RequestBody NodeInfoPageRequestDto requestDto) {
        ThrowUtils.throwIf(requestDto == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(requestDto.getPageNum() == null || requestDto.getPageNum() < 1,
                ErrorCode.PARAMS_ERROR, "页码不能小于1");
        ThrowUtils.throwIf(requestDto.getMaxPageNum() == null || requestDto.getMaxPageNum() <= 0,
                ErrorCode.PARAMS_ERROR, "每页大小必须大于0");

        NodeInfoPageResponseDto responseDto = nodeInfoService.listNodeInfoPage(requestDto);
        return ResultUtils.success(responseDto);
    }

    /**
     * 保存节点企业信息
     * @param nodeInfo 节点企业信息
     * @return 保存结果
     */
    @PostMapping("/save") //有必填的项
    public BaseResponse<Integer> saveNodeInfo(@RequestBody NodeInfo nodeInfo) {
        ThrowUtils.throwIf(nodeInfo == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(nodeInfo.getCode() == null || nodeInfo.getCode().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "登录编码不能为空");
        ThrowUtils.throwIf(nodeInfo.getPassword() == null || nodeInfo.getPassword().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "密码不能为空");
        ThrowUtils.throwIf(nodeInfo.getName() == null || nodeInfo.getName().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "企业名称不能为空");

        // 检查编码是否已存在
        long count = nodeInfoService.getNodeInfoCountByCode(nodeInfo.getCode());
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "登录编码已存在");

        //设置默认密码为企业的登录编码
        nodeInfo.setPassword(nodeInfo.getCode());

        int result = nodeInfoService.saveNodeInfo(nodeInfo);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "保存失败");

        return ResultUtils.success(result);
    }

    /**
     * 编辑节点企业信息
     * @param nodeInfo 节点企业信息
     * @return 更新结果
     */
    @PutMapping("/edit")
    public BaseResponse<Integer> editNodeInfo(@RequestBody NodeInfo nodeInfo) {
        ThrowUtils.throwIf(nodeInfo == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(nodeInfo.getNodeId() == null, ErrorCode.PARAMS_ERROR, "节点企业ID不能为空");

        int result = nodeInfoService.editNodeInfo(nodeInfo);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "更新失败");

        return ResultUtils.success(result);
    }

    /**
     * 禁用节点企业（逻辑删除）
     */
    @PutMapping("/disable/{nodeId}")
    public BaseResponse<Integer> disableNodeInfo(@PathVariable Integer nodeId) {
        ThrowUtils.throwIf(nodeId == null || nodeId <= 0, ErrorCode.PARAMS_ERROR, "节点企业ID不能为空");

        int result = nodeInfoService.disableNodeInfo(nodeId);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "禁用失败");

        return ResultUtils.success(result);
    }

    /**
     * 批量禁用节点企业（逻辑删除）
     */
    @PutMapping("/batchDisable")
    public BaseResponse<Integer> batchDisableNodeInfo(@RequestBody List<Integer> nodeIds) {
        ThrowUtils.throwIf(nodeIds == null || nodeIds.isEmpty(),
                ErrorCode.PARAMS_ERROR, "节点企业ID列表不能为空");

        int result = nodeInfoService.batchDisableNodeInfo(nodeIds);
        return ResultUtils.success(result);
    }

    /**
     * 启用节点企业
     */
    @PutMapping("/enable/{nodeId}")
    public BaseResponse<Integer> enableNodeInfo(@PathVariable Integer nodeId) {
        ThrowUtils.throwIf(nodeId == null || nodeId <= 0, ErrorCode.PARAMS_ERROR, "节点企业ID不能为空");

        int result = nodeInfoService.enableNodeInfo(nodeId);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "启用失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据ID获取节点企业详情
     * @param nodeId 节点企业ID
     * @return 节点企业详情
     */
    @GetMapping("/detail/{nodeId}")
    public BaseResponse<NodeInfoDetailsDto> getNodeInfoDetailById(@PathVariable Integer nodeId) {
        ThrowUtils.throwIf(nodeId == null || nodeId <= 0,
                ErrorCode.PARAMS_ERROR, "节点企业ID不能为空");

        NodeInfoDetailsDto detailDto = nodeInfoService.getNodeInfoDetailById(nodeId);
        return ResultUtils.success(detailDto);
    }

    /**
     * 按省份统计节点企业数据
     * @return 省份统计列表
     */
    @GetMapping("/stats/prov")
    public BaseResponse<List<NodeInfoTotalByProvDto>> listNodeInfoDataByProv() {
        List<NodeInfoTotalByProvDto> result = nodeInfoService.listNodeInfoDataByProv();
        return ResultUtils.success(result);
    }

    /**
     * 按类型统计节点企业数据
     * @return 类型统计列表
     */
    @GetMapping("/stats/type")
    public BaseResponse<List<NodeInfoTotalByTypeDto>> listNodeInfoDataByType() {
        List<NodeInfoTotalByTypeDto> result = nodeInfoService.listNodeInfoDataByType();
        return ResultUtils.success(result);
    }

    /**
     * 按月统计节点企业数据
     * @return 月度统计列表
     */
    @GetMapping("/stats/month")
    public BaseResponse<List<NodeInfoTotalByMonthDto>> listNodeInfoDataByMonth() {
        List<NodeInfoTotalByMonthDto> result = nodeInfoService.listNodeInfoDataByMonth();
        return ResultUtils.success(result);
    }

    /**
     * 更新密码
     * @param nodeId 节点企业ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 更新结果
     */
    @PutMapping("/updatePassword")
    public BaseResponse<Integer> updatePassword(@RequestParam Integer nodeId,
                                                @RequestParam String oldPassword,
                                                @RequestParam String newPassword) {
        ThrowUtils.throwIf(nodeId == null || nodeId <= 0, ErrorCode.PARAMS_ERROR, "节点企业ID不能为空");
        ThrowUtils.throwIf(oldPassword == null || oldPassword.trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "原密码不能为空");
        ThrowUtils.throwIf(newPassword == null || newPassword.trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "新密码不能为空");
        ThrowUtils.throwIf(oldPassword.equals(newPassword),
                ErrorCode.PARAMS_ERROR, "新密码不能与原密码相同");

        int result = nodeInfoService.updatePassword(nodeId, oldPassword, newPassword);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "密码更新失败");

        return ResultUtils.success(result);
    }

    /**
     * 重置密码（管理员操作）
     * @param nodeId 节点企业ID
     * @param newPassword 新密码(默认企业编号)
     * @return 重置结果
     */
    @PutMapping("/resetPassword")
    public BaseResponse<Integer> resetPassword(@RequestParam Integer nodeId,
                                               @RequestParam String newPassword) {
        ThrowUtils.throwIf(nodeId == null || nodeId <= 0, ErrorCode.PARAMS_ERROR, "节点企业ID不能为空");
        ThrowUtils.throwIf(newPassword == null || newPassword.trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "新密码不能为空");

        int result = nodeInfoService.resetPassword(nodeId, newPassword);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "密码重置失败");

        return ResultUtils.success(result);
    }

    /**
     * 通过登录编码重置密码（忘记密码功能）
     * @param code 登录编码
     * @param newPassword 新密码
     * @return 重置结果
     */
    @PutMapping("/forgotPassword")
    public BaseResponse<Integer> forgotPassword(@RequestParam String code,
                                                @RequestParam String newPassword) {
        ThrowUtils.throwIf(code == null || code.trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "登录编码不能为空");
        ThrowUtils.throwIf(newPassword == null || newPassword.trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "新密码不能为空");

        // 根据登录编码查找节点企业
        NodeInfo nodeInfo = nodeInfoService.getOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<NodeInfo>()
                        .eq(NodeInfo::getCode, code)
        );
        ThrowUtils.throwIf(nodeInfo == null, ErrorCode.NOT_FOUND_ERROR, "未找到对应的节点企业");

        int result = nodeInfoService.resetPassword(nodeInfo.getNodeId(), newPassword);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "密码重置失败");

        return ResultUtils.success(result);
    }

    /**
     * 根据ID永久删除节点企业（物理删除），并级联删除所有关联的下游产品批号
     * @param nodeId 节点企业ID
     * @return 删除结果
     */
    @DeleteMapping("/permanent/{nodeId}")
    public BaseResponse<Integer> permanentlyDeleteNodeInfo(@PathVariable Integer nodeId) {
        ThrowUtils.throwIf(nodeId == null || nodeId <= 0,
                ErrorCode.PARAMS_ERROR, "节点企业ID不能为空");

        int result = nodeInfoService.permanentlyDeleteNodeInfo(nodeId);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR, "删除失败");

        return ResultUtils.success(result);
    }
}