package com.cdiom.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdiom.backend.common.LoginResponse;
import com.cdiom.backend.common.UserCreateRequest;
import com.cdiom.backend.common.UserDTO;
import com.cdiom.backend.common.UserUpdateRequest;
import com.cdiom.backend.model.SysUser;

/**
 * 系统用户服务接口
 *
 * @author cdiom
 */
public interface SysUserService {

    /**
     * 用户登录
     *
     * @param usernameOrPhone 用户名或手机号
     * @param password        密码
     * @return 登录响应信息
     */
    LoginResponse login(String usernameOrPhone, String password);

    /**
     * 根据用户名或手机号查询用户
     *
     * @param usernameOrPhone 用户名或手机号
     * @return 用户信息
     */
    SysUser getUserByUsernameOrPhone(String usernameOrPhone);

    /**
     * 增加登录失败次数
     *
     * @param userId 用户ID
     */
    void incrementLoginFailCount(Long userId);

    /**
     * 重置登录失败次数
     *
     * @param userId 用户ID
     */
    void resetLoginFailCount(Long userId);

    /**
     * 锁定用户账号
     *
     * @param userId 用户ID
     */
    void lockUser(Long userId);

    /**
     * 检查用户是否被锁定
     *
     * @param user 用户信息
     * @return 是否被锁定
     */
    boolean isUserLocked(SysUser user);

    /**
     * 分页查询用户列表
     *
     * @param page     页码
     * @param size     每页大小
     * @param keyword  关键字（用户名/手机号）
     * @param roleId   角色ID
     * @param status   状态
     * @return 用户列表
     */
    Page<UserDTO> getUserList(Integer page, Integer size, String keyword, Long roleId, Integer status);

    /**
     * 根据ID查询用户详情
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserDTO getUserById(Long userId);

    /**
     * 创建用户
     *
     * @param request 创建用户请求
     * @param createBy 创建人ID
     * @return 用户信息
     */
    UserDTO createUser(UserCreateRequest request, Long createBy);

    /**
     * 更新用户
     *
     * @param request 更新用户请求
     * @return 用户信息
     */
    UserDTO updateUser(UserUpdateRequest request);

    /**
     * 禁用/启用用户
     *
     * @param userId 用户ID
     * @param status 状态：0-禁用/1-正常
     */
    void updateUserStatus(Long userId, Integer status);

    /**
     * 解锁用户
     *
     * @param userId 用户ID
     */
    void unlockUser(Long userId);
}



