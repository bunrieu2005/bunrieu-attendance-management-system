@echo off
chcp 65001
title UPDATE RIENG THU MUC ATTENDANCE
color 0E

echo ==============================================
echo   UPDATE CODE ATTENDANCE
echo ==============================================
echo.

:: 1. Chi them folder attendance vao Git (Bo qua .gitignore va README)
git add attendance/

:: 2. Nhap noi dung commit
set /p commit_msg="Nhap noi dung update (An Enter de bo qua): "
if "%commit_msg%"=="" set commit_msg="Cap nhat code moi nhat"

:: 3. Luu vao lich su
git commit -m "%commit_msg%"

:: 4. Day len mang
git push origin main

echo.
echo ==============================================
echo             DA XONG!
echo ==============================================
timeout /t 5