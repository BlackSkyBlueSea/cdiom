package com.cdiom.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdiom.backend.model.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据角色编码查询角色
     */
    @Select("SELECT * FROM sys_role WHERE role_code = #{roleCode}")
    SysRole selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据状态查询角色列表
     */
    @Select("SELECT * FROM sys_role WHERE status = #{status} ORDER BY create_time DESC")
    List<SysRole> selectByStatus(@Param("status") Integer status);

    /**
     * 分页查询角色
     */
    @Select("SELECT * FROM sys_role WHERE (role_name LIKE CONCAT('%', #{keyword}, '%') OR role_code LIKE CONCAT('%', #{keyword}, '%')) ORDER BY create_time DESC")
    IPage<SysRole> selectPageByKeyword(Page<SysRole> page, @Param("keyword") String keyword);
}
