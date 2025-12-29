package com.cdiom.backend.common;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息DTO（用于列表展示和详情）
 *
 * @author cdiom
 */
@Data
public class UserDTO {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 状态：0-禁用/1-正常
     */
    private Integer status;

    /**
     * 状态文本
     */
    private String statusText;

    /**
     * 锁定时间
     */
    private LocalDateTime lockTime;

    /**
     * 是否被锁定
     */
    private Boolean isLocked;

    /**
     * 登录失败次数
     */
    private Integer loginFailCount;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建人用户名
     */
    private String createByName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}



