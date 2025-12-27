@echo off
chcp 65001 > nul
echo ============================================
echo CDIOM数据库初始化脚本 (UTF-8编码)
echo ============================================
echo.

set MYSQL_PATH=D:\mysql-8.0.37-winx64\bin

echo MySQL路径: %MYSQL_PATH%
echo.
echo 可用的SQL脚本文件：
echo [1] cdiom_schema.sql (标准版)
echo [2] cdiom_schema_compatible.sql (最大兼容性)
echo [3] cdiom_schema_mysql8.sql (MySQL 8.0.37优化版 - 推荐)
echo.

set /p SQL_CHOICE=请选择SQL脚本 (1-3，默认3):
if "%SQL_CHOICE%"=="1" set SQL_FILE=%~dp0cdiom_java\src\main\resources\db\cdiom_schema.sql
if "%SQL_CHOICE%"=="2" set SQL_FILE=%~dp0cdiom_java\src\main\resources\db\cdiom_schema_compatible.sql
if "%SQL_CHOICE%"=="3" set SQL_FILE=%~dp0cdiom_java\src\main\resources\db\cdiom_schema_mysql8.sql
if "%SQL_CHOICE%"=="" set SQL_FILE=%~dp0cdiom_java\src\main\resources\db\cdiom_schema_mysql8.sql

echo 选择的SQL文件: %SQL_FILE%
echo.

echo 正在连接到MySQL数据库...
echo 请确保MySQL服务正在运行
echo.

set /p MYSQL_USER=请输入MySQL用户名 (默认: root):
if "%MYSQL_USER%"=="" set MYSQL_USER=root

set /p MYSQL_PASS=请输入MySQL密码:

echo.
echo 正在创建数据库...
"%MYSQL_PATH%\mysql.exe" --default-character-set=utf8mb4 -u %MYSQL_USER% -p%MYSQL_PASS% -e "DROP DATABASE IF EXISTS cdiom_db; CREATE DATABASE cdiom_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ 数据库创建失败！请检查MySQL连接信息。
    echo 错误代码: %ERRORLEVEL%
    pause
    exit /b 1
)

echo.
echo 正在执行SQL脚本...
"%MYSQL_PATH%\mysql.exe" --default-character-set=utf8mb4 -u %MYSQL_USER% -p%MYSQL_PASS% cdiom_db < "%SQL_FILE%"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo ✅ SQL脚本执行成功！
    echo 数据库 cdiom_db 已创建并初始化完成
    echo ============================================
    echo.
    echo 测试数据说明：
    echo - 用户账号: admin/123456 (系统管理员)
    echo - 用户账号: warehouse/123456 (仓库管理员)
    echo - 用户账号: doctor/123456 (医护人员)
    echo - 药品数据: 5条测试记录
    echo - 库存数据: 对应5条药品的库存信息
    echo.
    echo 正在运行数据库验证...
    echo.
    "%MYSQL_PATH%\mysql.exe" --default-character-set=utf8mb4 -u %MYSQL_USER% -p%MYSQL_PASS% cdiom_db < "%~dp0verify_db.sql"
    echo.
    echo ============================================
    echo 🎉 数据库初始化和验证完成！
    echo 现在可以启动Spring Boot应用进行测试
    echo ============================================
) else (
    echo.
    echo ❌ SQL脚本执行失败！
    echo 错误代码: %ERRORLEVEL%
    echo 请检查SQL文件路径和数据库连接。
)

echo.
echo 按任意键退出...
pause > nul
