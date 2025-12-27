-- ============================================
-- CDIOM数据库验证脚本
-- 用于验证数据库是否正确创建和初始化
-- ============================================

USE cdiom_db;

-- 1. 显示数据库信息
SELECT 'CDIOM Database Verification' as verification_title;
SELECT DATABASE() as current_database;
SELECT @@character_set_database as database_charset, @@collation_database as database_collation;

-- 2. 显示所有表
SHOW TABLES;

-- 3. 验证表结构和数据
SELECT '=== 角色表验证 ===' as section;
SELECT COUNT(*) as role_count FROM sys_role;
SELECT * FROM sys_role ORDER BY id;

SELECT '=== 用户表验证 ===' as section;
SELECT COUNT(*) as user_count FROM sys_user;
SELECT id, username, real_name, role_id, status FROM sys_user ORDER BY id;

SELECT '=== 药品表验证 ===' as section;
SELECT COUNT(*) as drug_count FROM drug_info;
SELECT id, drug_code, drug_name, category, is_special_drug FROM drug_info ORDER BY id;

SELECT '=== 库存表验证 ===' as section;
SELECT COUNT(*) as inventory_count FROM inventory;
SELECT i.drug_id, d.drug_name, i.quantity, i.location FROM inventory i
LEFT JOIN drug_info d ON i.drug_id = d.id ORDER BY i.drug_id;

SELECT '=== 采购订单验证 ===' as section;
SELECT COUNT(*) as purchase_order_count FROM purchase_order;
SELECT id, order_number, supplier, status, total_amount FROM purchase_order ORDER BY id;

SELECT '=== 出库申领验证 ===' as section;
SELECT COUNT(*) as outbound_apply_count FROM outbound_apply;
SELECT id, apply_number, department, status, total_quantity FROM outbound_apply ORDER BY id;

SELECT '=== 操作日志验证 ===' as section;
SELECT COUNT(*) as operation_log_count FROM operation_log;
SELECT id, operator_name, operation_type, operation_module, status FROM operation_log ORDER BY id;

-- 4. 验证外键关系
SELECT '=== 外键关系验证 ===' as section;
SELECT '用户角色关联正常' as result WHERE EXISTS (
    SELECT 1 FROM sys_user u JOIN sys_role r ON u.role_id = r.id
);

SELECT '库存药品关联正常' as result WHERE EXISTS (
    SELECT 1 FROM inventory i JOIN drug_info d ON i.drug_id = d.id
);

SELECT '订单明细关联正常' as result WHERE EXISTS (
    SELECT 1 FROM purchase_order_item poi
    JOIN purchase_order po ON poi.order_id = po.id
    JOIN drug_info d ON poi.drug_id = d.id
);

SELECT '申领明细关联正常' as result WHERE EXISTS (
    SELECT 1 FROM outbound_apply_item oai
    JOIN outbound_apply oa ON oai.apply_id = oa.id
    JOIN drug_info d ON oai.drug_id = d.id
);

-- 5. 验证索引
SELECT '=== 索引验证 ===' as section;
SHOW INDEX FROM sys_user;
SHOW INDEX FROM drug_info;
SHOW INDEX FROM inventory;
SHOW INDEX FROM purchase_order;
SHOW INDEX FROM outbound_apply;
SHOW INDEX FROM operation_log;

-- 6. 验证字符编码
SELECT '=== 字符编码验证 ===' as section;
SELECT TABLE_NAME, TABLE_COLLATION
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'cdiom_db'
ORDER BY TABLE_NAME;

SELECT '=== 数据库验证完成 ===' as verification_complete;
