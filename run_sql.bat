@echo off
echo ============================================
echo CDIOM数据库初始化脚本
echo ============================================
echo.

set MYSQL_PATH=D:\mysql-8.0.37-winx64\bin
set SQL_FILE=%~dp0cdiom_java\src\main\resources\db\cdiom_schema.sql

echo MySQL路径: %MYSQL_PATH%
echo SQL文件路径: %SQL_FILE%
echo.

echo 正在连接到MySQL数据库...
echo 请确保MySQL服务正在运行
echo.

set /p MYSQL_USER=请输入MySQL用户名 (默认: root):
if "%MYSQL_USER%"=="" set MYSQL_USER=root

set /p MYSQL_PASS=请输入MySQL密码:

echo.
echo 正在创建数据库...
"%MYSQL_PATH%\mysql.exe" -u %MYSQL_USER% -p%MYSQL_PASS% -e "CREATE DATABASE IF NOT EXISTS cdiom_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo 数据库创建失败！请检查MySQL连接信息。
    pause
    exit /b 1
)

echo.
echo 正在执行SQL脚本...
"%MYSQL_PATH%\mysql.exe" -u %MYSQL_USER% -p%MYSQL_PASS% cdiom_db < "%SQL_FILE%"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo ✅ SQL脚本执行成功！
    echo 数据库 cdiom_db 已创建并初始化完成
    echo ============================================
) else (
    echo.
    echo ❌ SQL脚本执行失败！
    echo 请检查SQL文件和数据库连接。
)

echo.
echo 按任意键退出...
pause > nul
