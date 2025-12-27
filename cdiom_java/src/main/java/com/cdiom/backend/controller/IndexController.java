package com.cdiom.backend.controller;

import com.cdiom.backend.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 根路径控制器
 * 提供API基本信息和导航
 *
 * @author cdiom
 */
@RestController
public class IndexController {

    /**
     * API根路径信息（公开接口）
     */
    @GetMapping("/")
    public Result<Map<String, Object>> apiInfo() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "欢迎使用CDIOM API服务");
        data.put("version", "1.0.0");
        data.put("status", "运行正常");
        data.put("publicEndpoints", new String[]{
            "/api/v1/test/public - 公开测试接口",
            "/api/v1/auth/login - 用户登录",
            "/api/v1/auth/register - 用户注册"
        });
        data.put("protectedEndpoints", new String[]{
            "/api/v1/test/protected - 受保护测试接口（需要JWT认证）",
            "/api/v1/test/admin - 管理员接口（需要管理员权限）"
        });
        data.put("frontend", "http://localhost:3000");
        data.put("documentation", "请通过前端界面访问完整功能");

        return Result.success("CDIOM API服务运行正常", data);
    }
}
