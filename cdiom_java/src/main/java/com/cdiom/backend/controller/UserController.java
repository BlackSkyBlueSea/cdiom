package com.cdiom.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdiom.backend.common.Result;
import com.cdiom.backend.common.UserCreateRequest;
import com.cdiom.backend.common.UserDTO;
import com.cdiom.backend.common.UserUpdateRequest;
import com.cdiom.backend.service.SysUserService;
import com.cdiom.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 *
 * @author cdiom
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService sysUserService;
    private final JwtUtil jwtUtil;

    /**
     * 分页查询用户列表
     *
     * @param page   页码
     * @param size   每页大小
     * @param keyword 关键字（用户名/手机号）
     * @param roleId 角色ID
     * @param status 状态
     * @return 用户列表
     */
    @GetMapping
    public Result<Page<UserDTO>> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) Integer status) {
        Page<UserDTO> userList = sysUserService.getUserList(page, size, keyword, roleId, status);
        return Result.success(userList);
    }

    /**
     * 根据ID查询用户详情
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    public Result<UserDTO> getUserById(@PathVariable Long userId) {
        UserDTO user = sysUserService.getUserById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 创建用户
     *
     * @param request 创建用户请求
     * @param httpRequest HTTP请求
     * @return 用户信息
     */
    @PostMapping
    public Result<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request,
                                      HttpServletRequest httpRequest) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(getTokenFromRequest(httpRequest));
            UserDTO user = sysUserService.createUser(request, userId);
            return Result.success("创建用户成功", user);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("创建用户失败，请稍后重试");
        }
    }

    /**
     * 更新用户
     *
     * @param request 更新用户请求
     * @return 用户信息
     */
    @PutMapping
    public Result<UserDTO> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        try {
            UserDTO user = sysUserService.updateUser(request);
            return Result.success("更新用户成功", user);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("更新用户失败，请稍后重试");
        }
    }

    /**
     * 禁用/启用用户
     *
     * @param userId 用户ID
     * @param status 状态：0-禁用/1-正常
     * @return 操作结果
     */
    @PutMapping("/{userId}/status")
    public Result<String> updateUserStatus(@PathVariable Long userId,
                                            @RequestParam Integer status) {
        try {
            sysUserService.updateUserStatus(userId, status);
            String message = status == 1 ? "启用用户成功" : "禁用用户成功";
            return Result.success(message);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("操作失败，请稍后重试");
        }
    }

    /**
     * 解锁用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/{userId}/unlock")
    public Result<String> unlockUser(@PathVariable Long userId) {
        try {
            sysUserService.unlockUser(userId);
            return Result.success("解锁用户成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("解锁用户失败，请稍后重试");
        }
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        // 从Cookie获取
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("cdiom_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // 从Header获取（Bearer Token）
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}

