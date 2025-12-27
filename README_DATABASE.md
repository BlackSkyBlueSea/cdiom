# CDIOM数据库集成指南

## 数据库配置

### 1. 创建数据库
```sql
CREATE DATABASE cdiom_db
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 执行SQL脚本
运行 `cdiom_java/src/main/resources/db/cdiom_schema.sql` 中的SQL语句创建表结构和测试数据。

### 3. 验证数据
执行以下查询验证数据库是否正确初始化：

```sql
-- 检查表数量
SELECT COUNT(*) FROM information_schema.tables
WHERE table_schema = 'cdiom_db';

-- 检查用户数据
SELECT * FROM sys_user;

-- 检查药品数据
SELECT * FROM drug_info;

-- 检查库存数据
SELECT * FROM inventory;
```

## 测试账号

| 用户名 | 密码 | 角色 | 权限说明 |
|--------|------|------|----------|
| admin | 123456 | 系统管理员 | 最高权限，可管理所有功能 |
| warehouse | 123456 | 仓库管理员 | 负责药品库存管理和采购 |
| doctor | 123456 | 医护人员 | 负责药品申领和使用 |

## 数据库表结构

### 核心业务表
1. **sys_role** - 角色表
2. **sys_user** - 用户表
3. **drug_info** - 药品信息表（含合规字段）
4. **inventory** - 库存表
5. **purchase_order** - 采购订单表
6. **purchase_order_item** - 采购订单明细表
7. **outbound_apply** - 出库申领表
8. **outbound_apply_item** - 出库申领明细表
9. **operation_log** - 操作日志表（含合规审计字段）

### 合规特性
- **药品表**: validity_period（有效期）、is_special_drug（特殊药品标识）
- **操作日志**: operator_id（操作人）、operation_time（操作时间）、data_snapshot_before/after（数据快照）

## 开发说明

### Entity类
所有Entity类位于 `com.cdiom.backend.model` 包中，包含：
- Lombok @Data 注解
- MyBatis-Plus @TableName, @TableId, @TableField 注解
- 自动填充字段：createTime, updateTime

### Mapper接口
所有Mapper接口位于 `com.cdiom.backend.mapper` 包中，包含：
- 继承 BaseMapper<T> 的基础CRUD方法
- 自定义业务查询方法
- XML映射文件在 `resources/mappers/` 目录

### 配置要点
- application.yml 中配置了 MyBatis-Plus 自动填充处理器
- @MapperScan 注解已配置在启动类中
- mapper-locations 指向正确的XML文件路径

## 下一步开发建议

1. **启动应用**: 运行 `mvn spring-boot:run` 验证数据库连接
2. **测试接口**: 使用Postman测试登录接口 `/api/v1/auth/login`
3. **业务逻辑**: 开发Service层实现业务逻辑
4. **API开发**: 基于现有Mapper开发REST API接口
5. **前端集成**: 将API接口与React前端集成

## 注意事项

1. **数据库连接**: 确保MySQL服务运行，连接参数正确
2. **字符编码**: 数据库使用UTF-8编码，支持中文
3. **时区设置**: MySQL连接字符串包含serverTimezone=Asia/Shanghai
4. **密码安全**: 生产环境请修改JWT密钥和数据库密码
5. **日志轮转**: 已配置日志文件轮转和保留策略

## 技术栈版本
- Java 17
- Spring Boot 3.2.8
- MyBatis-Plus 3.5.6
- MySQL 8.0+
- Maven 3.6+
