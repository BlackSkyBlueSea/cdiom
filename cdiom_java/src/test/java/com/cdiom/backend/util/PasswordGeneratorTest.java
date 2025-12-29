package com.cdiom.backend.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成测试类
 * 用于生成BCrypt加密后的密码，供SQL脚本初始化使用
 *
 * @author cdiom
 */
public class PasswordGeneratorTest {
    @Test
    public void generatePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "Super@123";
        String encodedPassword = encoder.encode(password);
        System.out.println("原始密码: " + password);
        System.out.println("BCrypt加密后: " + encodedPassword);

        // 验证密码
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("密码验证结果: " + matches);
    }
}






