# 登录功能说明文档

## 功能概述

已实现完整的登录认证功能，包括：
- 用户名/手机号 + 密码登录
- JWT Token认证（8小时有效期）
- 登录失败锁定机制（5次失败锁定1小时）
- 权限角色管理
- 前后端Token同步

## 后端实现

### 1. 核心组件

- **Model**: `SysUser`, `SysRole` - 用户和角色实体
- **Mapper**: `SysUserMapper`, `SysRoleMapper` - 数据访问层
- **Service**: `SysUserService` - 业务逻辑层
- **Controller**: `AuthController` - 登录接口
- **JWT工具**: `JwtUtil` - Token生成和验证
- **过滤器**: `JwtAuthenticationFilter` - Token验证拦截器

### 2. 登录接口

**POST** `/api/v1/auth/login`

请求体：
```json
{
  "usernameOrPhone": "super_admin",
  "password": "Super@123"
}
```

响应：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "username": "super_admin",
    "phone": "13800138000",
    "roleName": "SUPER_ADMIN",
    "roleDesc": "超级管理员"
  }
}
```

### 3. 安全特性

- **密码加密**: BCrypt加密存储
- **登录失败锁定**: 连续5次失败锁定1小时
- **Token过期**: 8小时自动过期
- **统一错误提示**: 登录失败统一提示"用户名或密码错误"

### 4. 默认账号

- **用户名**: `super_admin`
- **手机号**: `13800138000`
- **密码**: `Super@123`
- **角色**: `SUPER_ADMIN` (超级管理员)

## 前端实现

### 1. 登录页面

路径: `/login`

功能：
- 用户名/手机号登录
- 密码输入
- 自动存储Token到Cookie和localStorage
- 登录成功后跳转首页

### 2. Token管理

- **存储位置**: Cookie (`cdiom_token`) + localStorage（备用）
- **有效期**: 8小时
- **自动携带**: 所有API请求自动在Header中携带Token

### 3. 路由守卫

- **PrivateRoute**: 保护需要登录的路由
- **RoleRoute**: 保护需要特定角色权限的路由

### 4. 权限检查

使用 `hasRole(roleName)` 检查用户权限：
```javascript
import { hasRole } from '@/utils/auth';

if (hasRole('SUPER_ADMIN')) {
  // 显示管理员功能
}
```

## 使用说明

### 1. 启动后端

```bash
cd cdiom_java
mvn spring-boot:run
```

### 2. 启动前端

```bash
cd cdiom_react
npm run dev
```

### 3. 访问登录页

打开浏览器访问: http://localhost:3000/login

### 4. 登录测试

使用默认账号登录：
- 用户名: `super_admin` 或 `13800138000`
- 密码: `Super@123`

## 配置说明

### JWT配置 (application.yml)

```yaml
jwt:
  secret: cdiom-secret-key-for-jwt-token-generation-minimum-32-characters
  expiration: 28800000  # 8小时（毫秒）
```

### 安全配置

- 登录接口: `/auth/login` - 允许匿名访问
- 其他接口: 需要Token认证
- 首页: `/` - 需要登录

## 注意事项

1. **Token存储**: Token同时存储在Cookie和localStorage中，优先使用Cookie
2. **Token过期**: Token过期后会自动清除用户信息并跳转登录页
3. **账号锁定**: 连续5次登录失败会锁定账号1小时，需要管理员解锁
4. **密码安全**: 所有密码使用BCrypt加密，不可逆

## 后续扩展

可以在此基础上扩展：
- 用户管理功能
- 角色权限管理
- 操作日志记录
- 密码修改功能
- 忘记密码功能




