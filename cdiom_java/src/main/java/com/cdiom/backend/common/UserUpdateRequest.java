package com.cdiom.backend.common;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 更新用户请求DTO
 *
 * @author cdiom
 */
@Data
public class UserUpdateRequest {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 密码（可选，为空则不修改）
     */
    private String password;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 状态：0-禁用/1-正常
     */
    private Integer status;
}



