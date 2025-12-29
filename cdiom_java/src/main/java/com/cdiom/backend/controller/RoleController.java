package com.cdiom.backend.controller;

import com.cdiom.backend.common.Result;
import com.cdiom.backend.model.SysRole;
import com.cdiom.backend.mapper.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色管理控制器
 *
 * @author cdiom
 */
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final SysRoleMapper sysRoleMapper;

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    @GetMapping
    public Result<List<SysRole>> getRoleList() {
        List<SysRole> roles = sysRoleMapper.selectList(null);
        return Result.success(roles);
    }
}

