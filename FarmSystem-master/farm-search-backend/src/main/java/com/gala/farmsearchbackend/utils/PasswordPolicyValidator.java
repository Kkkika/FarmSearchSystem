package com.gala.farmsearchbackend.utils;

import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 密码策略验证工具
 */
@Component
public class PasswordPolicyValidator {

    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 20;
    private static final Pattern COMPLEXITY_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{" + MIN_LENGTH + "," + MAX_LENGTH + "}$");

    public void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能为空");
        }

        if (password.length() < MIN_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,
                    String.format("密码长度不能少于%d位", MIN_LENGTH));
        }

        if (password.length() > MAX_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,
                    String.format("密码长度不能超过%d位", MAX_LENGTH));
        }

        if (!COMPLEXITY_PATTERN.matcher(password).matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,
                    "密码必须包含字母和数字，且不能包含特殊字符");
        }
    }

    public void validateNotSameAsOld(String newPassword, String oldPassword) {
        if (newPassword.equals(oldPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码不能与旧密码相同");
        }
    }
}
