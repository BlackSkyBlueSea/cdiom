package com.cdiom.backend.common;

import lombok.Data;

/**
 * 登录响应DTO
 *
 * @author cdiom
 */
@Data
public class LoginResponse {

    /**
     * Token
     */
    private String token;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;
}





