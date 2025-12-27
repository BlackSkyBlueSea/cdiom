-- ============================================
-- CDIOM医药管理系统数据库脚本 (兼容版本)
-- 版本：1.0.2 (最大兼容性)
-- 创建时间：2025-12-27
-- 适用于各种MySQL版本
-- ============================================

-- 创建数据库 (兼容性版本)
DROP DATABASE IF EXISTS cdiom_db;
CREATE DATABASE cdiom_db DEFAULT CHARACTER SET utf8mb4;

USE cdiom_db;

-- 设置字符编码
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- ============================================
-- 1. 角色表 (sys_role)
-- ============================================
CREATE TABLE sys_role (
    id BIGINT NOT NULL AUTO_INCREMENT,
    role_code VARCHAR(50) NOT NULL,
    role_name VARCHAR(100) NOT NULL,
    description VARCHAR(200),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 2. 用户表 (sys_user)
-- ============================================
CREATE TABLE sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    role_id BIGINT NOT NULL,
    status TINYINT DEFAULT 1,
    last_login_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_role_id (role_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 3. 药品信息表 (drug_info)
-- ============================================
CREATE TABLE drug_info (
    id BIGINT NOT NULL AUTO_INCREMENT,
    drug_code VARCHAR(50) NOT NULL,
    drug_name VARCHAR(200) NOT NULL,
    generic_name VARCHAR(200),
    specification VARCHAR(100),
    manufacturer VARCHAR(200),
    batch_number VARCHAR(50),
    production_date DATE,
    validity_period DATE,
    unit VARCHAR(20),
    unit_price DECIMAL(10,2),
    category VARCHAR(50),
    is_special_drug TINYINT DEFAULT 0,
    storage_condition VARCHAR(100),
    description TEXT,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_drug_code (drug_code),
    KEY idx_drug_name (drug_name),
    KEY idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 4. 库存表 (inventory)
-- ============================================
CREATE TABLE inventory (
    id BIGINT NOT NULL AUTO_INCREMENT,
    drug_id BIGINT NOT NULL,
    batch_number VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    location VARCHAR(100),
    expiry_date DATE,
    warning_quantity INT DEFAULT 10,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_drug_batch (drug_id, batch_number),
    KEY idx_drug_id (drug_id),
    FOREIGN KEY (drug_id) REFERENCES drug_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 5. 采购订单表 (purchase_order)
-- ============================================
CREATE TABLE purchase_order (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_number VARCHAR(50) NOT NULL,
    supplier VARCHAR(200),
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    expected_delivery_date DATE,
    total_amount DECIMAL(12,2),
    status VARCHAR(20) DEFAULT 'PENDING',
    approver_id BIGINT,
    approval_time DATETIME,
    remarks TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_number (order_number),
    KEY idx_status (status),
    KEY idx_order_date (order_date),
    KEY idx_approver (approver_id),
    FOREIGN KEY (approver_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 6. 采购订单明细表 (purchase_order_item)
-- ============================================
CREATE TABLE purchase_order_item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    drug_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2),
    total_amount DECIMAL(10,2),
    batch_number VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_order_id (order_id),
    KEY idx_drug_id (drug_id),
    FOREIGN KEY (order_id) REFERENCES purchase_order(id),
    FOREIGN KEY (drug_id) REFERENCES drug_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 7. 出库申领表 (outbound_apply)
-- ============================================
CREATE TABLE outbound_apply (
    id BIGINT NOT NULL AUTO_INCREMENT,
    apply_number VARCHAR(50) NOT NULL,
    applicant_id BIGINT NOT NULL,
    department VARCHAR(100),
    apply_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    reason VARCHAR(200),
    urgency_level VARCHAR(20) DEFAULT 'NORMAL',
    total_quantity INT,
    status VARCHAR(20) DEFAULT 'PENDING',
    approver_id BIGINT,
    approval_time DATETIME,
    approval_opinion TEXT,
    remarks TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_apply_number (apply_number),
    KEY idx_applicant (applicant_id),
    KEY idx_status (status),
    KEY idx_approver (approver_id),
    FOREIGN KEY (applicant_id) REFERENCES sys_user(id),
    FOREIGN KEY (approver_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 8. 出库申领明细表 (outbound_apply_item)
-- ============================================
CREATE TABLE outbound_apply_item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    apply_id BIGINT NOT NULL,
    drug_id BIGINT NOT NULL,
    apply_quantity INT NOT NULL,
    approved_quantity INT,
    actual_quantity INT,
    batch_number VARCHAR(50),
    remarks VARCHAR(200),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_apply_id (apply_id),
    KEY idx_drug_id (drug_id),
    FOREIGN KEY (apply_id) REFERENCES outbound_apply(id),
    FOREIGN KEY (drug_id) REFERENCES drug_info(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 9. 操作日志表 (operation_log)
-- ============================================
CREATE TABLE operation_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    operator_id BIGINT NOT NULL,
    operator_name VARCHAR(100),
    operation_type VARCHAR(50) NOT NULL,
    operation_module VARCHAR(50) NOT NULL,
    operation_description VARCHAR(500),
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50),
    user_agent TEXT,
    request_url VARCHAR(500),
    request_method VARCHAR(10),
    request_params TEXT,
    response_result TEXT,
    execution_time BIGINT,
    status TINYINT DEFAULT 1,
    error_message TEXT,
    data_snapshot_before TEXT,
    data_snapshot_after TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_operator (operator_id),
    KEY idx_operation_time (operation_time),
    KEY idx_operation_type (operation_type, operation_module)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 插入测试数据
-- ============================================

-- 插入角色数据
INSERT INTO sys_role (role_code, role_name, description, status) VALUES
('ADMIN', '系统管理员', '系统管理员，具有最高权限', 1),
('WAREHOUSE_MANAGER', '仓库管理员', '负责药品库存管理', 1),
('MEDICAL_STAFF', '医护人员', '负责药品申领和使用', 1);

-- 插入用户数据（密码都是123456，经过BCrypt加密）
INSERT INTO sys_user (username, password, real_name, email, phone, role_id, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxp7zKJyJk6x7AS', '系统管理员', 'admin@cdiom.com', '13800000001', 1, 1),
('warehouse', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxp7zKJyJk6x7AS', '仓库管理员', 'warehouse@cdiom.com', '13800000002', 2, 1),
('doctor', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxp7zKJyJk6x7AS', '张医生', 'doctor@cdiom.com', '13800000003', 3, 1);

-- 插入药品数据
INSERT INTO drug_info (drug_code, drug_name, generic_name, specification, manufacturer, batch_number, production_date, validity_period, unit, unit_price, category, is_special_drug, storage_condition, description, status) VALUES
('DRUG001', '阿莫西林胶囊', 'Amoxicillin Capsules', '0.25g*24粒/盒', '国药集团', 'BATCH001', '2024-01-01', '2026-01-01', '盒', 25.50, '抗生素', 0, '阴凉干燥处', '青霉素类抗生素，用于治疗细菌感染', 1),
('DRUG002', '布洛芬缓释胶囊', 'Ibuprofen Sustained-release Capsules', '0.3g*20粒/盒', '辉瑞制药', 'BATCH002', '2024-02-01', '2026-02-01', '盒', 18.80, '解热镇痛药', 0, '阴凉处', '非甾体抗炎药，用于缓解疼痛和发热', 1),
('DRUG003', '盐酸左氧氟沙星片', 'Levofloxacin Hydrochloride Tablets', '0.1g*10片/盒', '拜耳医药', 'BATCH003', '2024-03-01', '2026-03-01', '盒', 32.00, '抗生素', 0, '阴凉干燥处', '喹诺酮类抗菌药，用于治疗细菌感染', 1),
('DRUG004', '硝酸甘油片', 'Nitroglycerin Tablets', '0.3mg*100片/瓶', '葛兰素史克', 'BATCH004', '2024-04-01', '2025-04-01', '瓶', 45.00, '心血管药', 1, '阴凉避光处', '硝酸酯类药，用于治疗心绞痛', 1),
('DRUG005', '维生素C片', 'Vitamin C Tablets', '100mg*100片/瓶', '罗氏制药', 'BATCH005', '2024-05-01', '2027-05-01', '瓶', 12.50, '维生素', 0, '阴凉干燥处', '维生素C补充剂，增强免疫力', 1);

-- 插入库存数据
INSERT INTO inventory (drug_id, batch_number, quantity, location, expiry_date, warning_quantity, status) VALUES
(1, 'BATCH001', 100, 'A1-01', '2026-01-01', 10, 1),
(2, 'BATCH002', 80, 'A1-02', '2026-02-01', 8, 1),
(3, 'BATCH003', 60, 'A1-03', '2026-03-01', 6, 1),
(4, 'BATCH004', 30, 'A2-01', '2025-04-01', 5, 1),
(5, 'BATCH005', 120, 'A1-04', '2027-05-01', 12, 1);

-- 插入采购订单数据
INSERT INTO purchase_order (order_number, supplier, expected_delivery_date, total_amount, status, remarks) VALUES
('PO20241227001', '国药控股有限公司', '2025-01-15', 2550.00, 'PENDING', '常规药品采购'),
('PO20241227002', '华润医药商业集团', '2025-01-20', 1880.00, 'APPROVED', '解热镇痛药补充');

-- 插入采购订单明细
INSERT INTO purchase_order_item (order_id, drug_id, quantity, unit_price, total_amount, batch_number) VALUES
(1, 1, 100, 25.50, 2550.00, 'BATCH001_NEW'),
(2, 2, 100, 18.80, 1880.00, 'BATCH002_NEW');

-- 插入出库申领数据
INSERT INTO outbound_apply (apply_number, applicant_id, department, reason, urgency_level, total_quantity, status) VALUES
('OA20241227001', 3, '内科', '日常诊疗用药', 'NORMAL', 50, 'PENDING'),
('OA20241227002', 3, '外科', '手术用药', 'HIGH', 20, 'APPROVED');

-- 插入出库申领明细
INSERT INTO outbound_apply_item (apply_id, drug_id, apply_quantity, approved_quantity, batch_number) VALUES
(1, 1, 20, 15, 'BATCH001'),
(1, 2, 30, 25, 'BATCH002'),
(2, 4, 20, 20, 'BATCH004');

-- 插入操作日志数据
INSERT INTO operation_log (operator_id, operator_name, operation_type, operation_module, operation_description, operation_time, ip_address, request_method, request_url, status, execution_time) VALUES
(1, '系统管理员', 'LOGIN', 'AUTH', '用户登录', '2025-12-27 09:00:00', '192.168.1.100', 'POST', '/api/v1/auth/login', 1, 150),
(2, '仓库管理员', 'CREATE', 'INVENTORY', '新增库存记录', '2025-12-27 10:30:00', '192.168.1.101', 'POST', '/api/v1/inventory', 1, 200),
(3, '张医生', 'CREATE', 'OUTBOUND_APPLY', '提交药品申领申请', '2025-12-27 11:15:00', '192.168.1.102', 'POST', '/api/v1/outbound/apply', 1, 180);

-- ============================================
-- 显示创建结果
-- ============================================
SELECT 'Database cdiom_db created successfully!' as result;
SHOW TABLES;
