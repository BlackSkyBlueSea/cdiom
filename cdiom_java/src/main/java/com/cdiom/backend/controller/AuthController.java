package com.cdiom.backend.controller;

import com.cdiom.backend.common.LoginRequest;
import com.cdiom.backend.common.LoginResponse;
import com.cdiom.backend.common.Result;
import com.cdiom.backend.service.SysUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author cdiom
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SysUserService sysUserService;

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @param response     HTTP响应
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                       HttpServletResponse response) {
        try {
            LoginResponse loginResponse = sysUserService.login(
                    loginRequest.getUsernameOrPhone(),
                    loginRequest.getPassword()
            );

            // 将Token存储到Cookie中
            Cookie cookie = new Cookie("cdiom_token", loginResponse.getToken());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(8 * 60 * 60); // 8小时
            response.addCookie(cookie);

            return Result.success("登录成功", loginResponse);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("登录失败，请稍后重试");
        }
    }

    /**
     * 用户登出
     *
     * @param response HTTP响应
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletResponse response) {
        // 清除Cookie
        Cookie cookie = new Cookie("cdiom_token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return Result.success("登出成功", null);
    }
}


