package com.cdiom.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;

/**
 * CDIOM医药管理系统启动类
 *
 * @author cdiom
 */
@SpringBootApplication(exclude = {SqlInitializationAutoConfiguration.class})
public class DrugApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrugApplication.class, args);
    }
}
