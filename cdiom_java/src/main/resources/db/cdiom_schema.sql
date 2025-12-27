-- CDIOM医药管理系统数据库脚本
-- 版本：1.0.0
-- 创建时间：2025-12-27

-- 创建数据库
CREATE DATABASE IF NOT EXISTS cdiom_db
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE cdiom_db;

-- =============================================
-- 1. 角色表 (sys_role)
-- =============================================
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    role_name VARCHAR(100) NOT NULL COMMENT '角色名称',
    description VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '角色表';

-- =============================================
-- 2. 用户表 (sys_user)
-- =============================================
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (role_id) REFERENCES sys_role(id)
) COMMENT '用户表';

-- =============================================
-- 3. 药品信息表 (drug_info)
-- =============================================
CREATE TABLE drug_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '药品ID',
    drug_code VARCHAR(50) NOT NULL UNIQUE COMMENT '药品编码',
    drug_name VARCHAR(200) NOT NULL COMMENT '药品名称',
    generic_name VARCHAR(200) COMMENT '通用名称',
    specification VARCHAR(100) COMMENT '规格',
    manufacturer VARCHAR(200) COMMENT '生产厂家',
    batch_number VARCHAR(50) COMMENT '批号',
    production_date DATE COMMENT '生产日期',
    validity_period DATE COMMENT '有效期',
    unit VARCHAR(20) COMMENT '单位',
    unit_price DECIMAL(10,2) COMMENT '单价',
    category VARCHAR(50) COMMENT '分类',
    is_special_drug TINYINT DEFAULT 0 COMMENT '是否特殊药品：0-否，1-是',
    storage_condition VARCHAR(100) COMMENT '储存条件',
    description TEXT COMMENT '药品描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '药品信息表';

-- =============================================
-- 4. 库存表 (inventory)
-- =============================================
CREATE TABLE inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库存ID',
    drug_id BIGINT NOT NULL COMMENT '药品ID',
    batch_number VARCHAR(50) NOT NULL COMMENT '批号',
    quantity INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    location VARCHAR(100) COMMENT '存放位置',
    expiry_date DATE COMMENT '过期日期',
    warning_quantity INT DEFAULT 10 COMMENT '预警数量',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (drug_id) REFERENCES drug_info(id),
    UNIQUE KEY uk_inventory_drug_batch (drug_id, batch_number)
) COMMENT '库存表';

-- =============================================
-- 5. 采购订单表 (purchase_order)
-- =============================================
CREATE TABLE purchase_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_number VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    supplier VARCHAR(200) COMMENT '供应商',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '下单日期',
    expected_delivery_date DATE COMMENT '预计到货日期',
    total_amount DECIMAL(12,2) COMMENT '订单总金额',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING-待处理，APPROVED-已批准，DELIVERED-已到货，CANCELLED-已取消',
    approver_id BIGINT COMMENT '审批人ID',
    approval_time DATETIME COMMENT '审批时间',
    remarks TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (approver_id) REFERENCES sys_user(id)
) COMMENT '采购订单表';

-- =============================================
-- 6. 采购订单明细表 (purchase_order_item)
-- =============================================
CREATE TABLE purchase_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    drug_id BIGINT NOT NULL COMMENT '药品ID',
    quantity INT NOT NULL COMMENT '采购数量',
    unit_price DECIMAL(10,2) COMMENT '单价',
    total_amount DECIMAL(10,2) COMMENT '总金额',
    batch_number VARCHAR(50) COMMENT '批号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (order_id) REFERENCES purchase_order(id),
    FOREIGN KEY (drug_id) REFERENCES drug_info(id)
) COMMENT '采购订单明细表';

-- =============================================
-- 7. 出库申领表 (outbound_apply)
-- =============================================
CREATE TABLE outbound_apply (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申领ID',
    apply_number VARCHAR(50) NOT NULL UNIQUE COMMENT '申领单号',
    applicant_id BIGINT NOT NULL COMMENT '申领人ID',
    department VARCHAR(100) COMMENT '申领部门',
    apply_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申领日期',
    reason VARCHAR(200) COMMENT '申领原因',
    urgency_level VARCHAR(20) DEFAULT 'NORMAL' COMMENT '紧急程度：LOW-低，NORMAL-普通，HIGH-高，URGENT-紧急',
    total_quantity INT COMMENT '总申领数量',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING-待审批，APPROVED-已批准，REJECTED-已拒绝，COMPLETED-已完成',
    approver_id BIGINT COMMENT '审批人ID',
    approval_time DATETIME COMMENT '审批时间',
    approval_opinion TEXT COMMENT '审批意见',
    remarks TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (applicant_id) REFERENCES sys_user(id),
    FOREIGN KEY (approver_id) REFERENCES sys_user(id)
) COMMENT '出库申领表';

-- =============================================
-- 8. 出库申领明细表 (outbound_apply_item)
-- =============================================
CREATE TABLE outbound_apply_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    apply_id BIGINT NOT NULL COMMENT '申领ID',
    drug_id BIGINT NOT NULL COMMENT '药品ID',
    apply_quantity INT NOT NULL COMMENT '申领数量',
    approved_quantity INT COMMENT '批准数量',
    actual_quantity INT COMMENT '实际出库数量',
    batch_number VARCHAR(50) COMMENT '批号',
    remarks VARCHAR(200) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (apply_id) REFERENCES outbound_apply(id),
    FOREIGN KEY (drug_id) REFERENCES drug_info(id)
) COMMENT '出库申领明细表';

-- =============================================
-- 9. 操作日志表 (operation_log)
-- =============================================
CREATE TABLE operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    operator_id BIGINT NOT NULL COMMENT '操作人ID',
    operator_name VARCHAR(100) COMMENT '操作人姓名',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型',
    operation_module VARCHAR(50) NOT NULL COMMENT '操作模块',
    operation_description VARCHAR(500) COMMENT '操作描述',
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    request_url VARCHAR(500) COMMENT '请求URL',
    request_method VARCHAR(10) COMMENT '请求方法',
    request_params TEXT COMMENT '请求参数',
    response_result TEXT COMMENT '响应结果',
    execution_time BIGINT COMMENT '执行时间（毫秒）',
    status TINYINT DEFAULT 1 COMMENT '状态：0-失败，1-成功',
    error_message TEXT COMMENT '错误信息',
    data_snapshot_before TEXT COMMENT '操作前数据快照',
    data_snapshot_after TEXT COMMENT '操作后数据快照',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '操作日志表';

-- =============================================
-- 插入测试数据
-- =============================================

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

-- 创建索引以提升查询性能
CREATE INDEX idx_sys_user_username ON sys_user(username);
CREATE INDEX idx_sys_user_role_id ON sys_user(role_id);
CREATE INDEX idx_drug_info_drug_code ON drug_info(drug_code);
CREATE INDEX idx_drug_info_name ON drug_info(drug_name);
CREATE INDEX idx_inventory_drug_id ON inventory(drug_id);
CREATE INDEX idx_purchase_order_status ON purchase_order(status);
CREATE INDEX idx_purchase_order_date ON purchase_order(order_date);
CREATE INDEX idx_outbound_apply_status ON outbound_apply(status);
CREATE INDEX idx_outbound_apply_applicant ON outbound_apply(applicant_id);
CREATE INDEX idx_operation_log_operator ON operation_log(operator_id);
CREATE INDEX idx_operation_log_time ON operation_log(operation_time);
CREATE INDEX idx_operation_log_type ON operation_log(operation_type, operation_module);
