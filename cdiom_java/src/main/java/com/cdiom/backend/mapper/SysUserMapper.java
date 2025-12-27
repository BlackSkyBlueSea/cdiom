package com.cdiom.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdiom.backend.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 根据角色ID查询用户列表
     */
    @Select("SELECT * FROM sys_user WHERE role_id = #{roleId} AND status = 1 ORDER BY create_time DESC")
    List<SysUser> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据状态查询用户列表
     */
    @Select("SELECT u.*, r.role_name FROM sys_user u LEFT JOIN sys_role r ON u.role_id = r.id WHERE u.status = #{status} ORDER BY u.create_time DESC")
    List<SysUser> selectByStatusWithRole(@Param("status") Integer status);

    /**
     * 分页查询用户（包含角色信息）
     */
    @Select("SELECT u.*, r.role_name FROM sys_user u LEFT JOIN sys_role r ON u.role_id = r.id " +
            "WHERE (u.username LIKE CONCAT('%', #{keyword}, '%') OR u.real_name LIKE CONCAT('%', #{keyword}, '%') OR u.email LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY u.create_time DESC")
    IPage<SysUser> selectPageWithRole(Page<SysUser> page, @Param("keyword") String keyword);

    /**
     * 更新用户最后登录时间
     */
    @Select("UPDATE sys_user SET last_login_time = NOW() WHERE id = #{userId}")
    void updateLastLoginTime(@Param("userId") Long userId);
}
