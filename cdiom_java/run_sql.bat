@echo off
chcp 65001 >nul
echo ============================================
echo 执行CDIOM数据库脚本
echo ============================================
echo.

cd /d %~dp0

set SQL_FILE=src\main\resources\db\cdiom_schema.sql

if not exist "%SQL_FILE%" (
    echo 错误：找不到SQL文件: %SQL_FILE%
    pause
    exit /b 1
)

echo SQL文件路径: %CD%\%SQL_FILE%
echo.
echo 请输入MySQL root密码：
mysql -h localhost -u root -p cdiom_db < "%SQL_FILE%"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo 脚本执行成功！
    echo ============================================
) else (
    echo.
    echo ============================================
    echo 脚本执行失败，错误代码: %ERRORLEVEL%
    echo ============================================
)

pause









