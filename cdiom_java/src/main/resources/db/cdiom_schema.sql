-- ============================================
-- CDIOM医药管理系统数据库脚本
-- MySQL 8.0.33
-- ============================================

-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS cdiom_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE cdiom_db;

-- 删除已存在的表（如果存在）
-- 注意：需要按照外键依赖关系的逆序删除
-- 先删除有外键依赖的表，再删除被引用的表

-- 删除可能存在的业务表（如果有外键引用sys_user）
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS purchase_order_item;
DROP TABLE IF EXISTS purchase_order;
DROP TABLE IF EXISTS outbound_apply_item;
DROP TABLE IF EXISTS outbound_apply;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS drug_info;
-- 删除系统表
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS operation_log;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_role;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 1. 角色表（sys_role）
-- ============================================
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称：SUPER_ADMIN/WAREHOUSE_ADMIN/PURCHASER/MEDICAL_STAFF/SUPPLIER',
    role_desc VARCHAR(200) COMMENT '角色描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_role_name (role_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ============================================
-- 2. 用户表（sys_user）
-- ============================================
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    role_id BIGINT NOT NULL COMMENT '角色ID（外键关联sys_role.id）',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用/1-正常',
    lock_time DATETIME COMMENT '锁定时间',
    login_fail_count INT DEFAULT 0 COMMENT '登录失败次数',
    create_by BIGINT COMMENT '创建人（默认超级管理员ID）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除/1-已删除',
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_phone (phone),
    INDEX idx_role_id (role_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 3. 用户角色关联表（sys_user_role）
-- ============================================
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID（外键关联sys_user.id）',
    role_id BIGINT NOT NULL COMMENT '角色ID（外键关联sys_role.id）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ============================================
-- 4. 操作日志表（operation_log）
-- ============================================
CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    operator_id BIGINT COMMENT '操作人ID（关联sys_user.id）',
    operation_type VARCHAR(50) COMMENT '操作类型',
    operation_content VARCHAR(500) COMMENT '操作内容',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    data_snapshot JSON COMMENT '数据快照（JSON格式）',
    INDEX idx_operator_id (operator_id),
    INDEX idx_operation_time (operation_time),
    INDEX idx_operation_type (operation_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入超级管理员角色
INSERT INTO sys_role (id, role_name, role_desc) VALUES
(1, 'SUPER_ADMIN', '超级管理员'),
(2, 'WAREHOUSE_ADMIN', '仓库管理员'),
(3, 'PURCHASER', '采购员'),
(4, 'MEDICAL_STAFF', '医护人员'),
(5, 'SUPPLIER', '供应商')
ON DUPLICATE KEY UPDATE role_name=VALUES(role_name), role_desc=VALUES(role_desc);

-- 插入默认超级管理员用户
-- 密码：Super@123 (BCrypt加密后的值)
-- 使用BCryptPasswordEncoder生成，已验证可以匹配密码"Super@123"
INSERT INTO sys_user (id, username, phone, password, role_id, status, create_by) VALUES
(1, 'super_admin', '13800138000', '$2a$10$fmBSzl4eciiNsbcBng310ObfZ71ViTjDFOYYtxf73tEQAaSXM2eYG', 1, 1, 1)
ON DUPLICATE KEY UPDATE username=VALUES(username), phone=VALUES(phone);

-- 插入用户角色关联（超级管理员）
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1)
ON DUPLICATE KEY UPDATE user_id=VALUES(user_id), role_id=VALUES(role_id);

-- ============================================
-- 脚本执行完成
-- ============================================



