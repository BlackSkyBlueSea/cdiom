package com.cdiom.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CDIOM医药管理系统启动类
 *
 * @author cdiom
 */
@SpringBootApplication
@MapperScan("com.cdiom.backend.mapper")
public class DrugApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrugApplication.class, args);
    }
}
