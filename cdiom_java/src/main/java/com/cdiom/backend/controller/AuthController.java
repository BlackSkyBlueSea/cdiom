package com.cdiom.backend.controller;

import com.cdiom.backend.common.JwtUtil;
import com.cdiom.backend.common.Result;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供登录、注册等认证相关接口
 *
 * @author cdiom
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    // 密码编码器，用于注册时加密密码（暂时未使用，后续实现注册功能时使用）
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        // TODO: 这里应该从数据库验证用户信息
        // 目前为示例代码，实际应该查询数据库验证用户名和密码

        // 示例：假设用户名为admin，密码为admin123
        String username = request.getUsername();
        String password = request.getPassword();

        // 这里应该从数据库查询用户并验证密码
        // User user = userService.findByUsername(username);
        // if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
        //     return Result.error("用户名或密码错误");
        // }

        // 临时示例：简单验证
        if (!"admin".equals(username) || !"admin123".equals(password)) {
            return Result.error("用户名或密码错误");
        }

        // 生成JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("userId", 1L); // 实际应该从数据库获取
        // 可以添加权限信息
        // claims.put("authorities", Arrays.asList("ROLE_USER", "ROLE_ADMIN"));

        String token = jwtUtil.generateToken(claims);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", username);
        data.put("expiresIn", jwtUtil.getExpiration() / 1000); // 转换为秒

        return Result.success("登录成功", data);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterRequest request) {
        // TODO: 实现用户注册逻辑
        // 1. 检查用户名是否已存在
        // 2. 加密密码
        // 3. 保存用户信息到数据库

        log.info("用户注册: {}", request.getUsername());
        return Result.success("注册成功，请登录");
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    public Result<Map<String, Object>> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error("无效的Token");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return Result.error("Token已过期或无效");
        }

        // 从旧Token中获取用户信息
        var claims = jwtUtil.getClaimsFromToken(token);
        String username = claims.getSubject();

        // 生成新Token
        Map<String, Object> newClaims = new HashMap<>();
        newClaims.put("username", username);
        if (claims.get("userId") != null) {
            newClaims.put("userId", claims.get("userId"));
        }

        String newToken = jwtUtil.generateToken(newClaims);

        Map<String, Object> data = new HashMap<>();
        data.put("token", newToken);
        data.put("expiresIn", jwtUtil.getExpiration() / 1000);

        return Result.success("Token刷新成功", data);
    }

    /**
     * 登录请求DTO
     */
    @Data
    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;
    }

    /**
     * 注册请求DTO
     */
    @Data
    public static class RegisterRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;

        private String email;
        private String phone;
    }
}

