package com.cdiom.backend.controller;

import com.cdiom.backend.common.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于测试Spring Security配置是否生效
 *
 * @author cdiom
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 测试公开接口（不需要认证）
     */
    @GetMapping("/public")
    public Result<String> publicEndpoint() {
        return Result.success("这是一个公开接口，不需要认证");
    }

    /**
     * 测试需要认证的接口
     */
    @GetMapping("/protected")
    public Result<Map<String, Object>> protectedEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> data = new HashMap<>();
        data.put("message", "这是一个受保护的接口，需要JWT认证");
        data.put("username", authentication.getName());
        data.put("authorities", authentication.getAuthorities());

        return Result.success("认证成功", data);
    }

    /**
     * 测试需要管理员权限的接口
     */
    @GetMapping("/admin")
    public Result<String> adminEndpoint() {
        return Result.success("这是管理员接口");
    }
}

