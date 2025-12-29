package com.cdiom.backend.common;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 *
 * @author cdiom
 */
@Data
public class LoginRequest {

    /**
     * 用户名或手机号
     */
    @NotBlank(message = "用户名或手机号不能为空")
    private String usernameOrPhone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}





