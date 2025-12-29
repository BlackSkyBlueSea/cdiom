package com.cdiom.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdiom.backend.common.UserDTO;
import com.cdiom.backend.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户Mapper接口
 *
 * @author cdiom
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名或手机号查询用户（包含角色信息）
     *
     * @param usernameOrPhone 用户名或手机号
     * @return 用户信息
     */
    SysUser selectByUsernameOrPhone(@Param("usernameOrPhone") String usernameOrPhone);

    /**
     * 分页查询用户列表（包含角色信息）
     *
     * @param page    分页对象
     * @param keyword 关键字（用户名/手机号）
     * @param roleId  角色ID
     * @param status  状态
     * @return 用户列表
     */
    Page<UserDTO> selectUserList(Page<UserDTO> page,
                                  @Param("keyword") String keyword,
                                  @Param("roleId") Long roleId,
                                  @Param("status") Integer status);

    /**
     * 根据ID查询用户详情（包含角色信息）
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserDTO selectUserById(@Param("userId") Long userId);
}



