package com.cdiom.backend.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus自动填充处理器
 * 自动填充createTime和updateTime字段
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时的填充策略
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入填充 ....");

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 填充创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);

        // 填充更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);

        log.debug("插入填充完成 ....");
    }

    /**
     * 更新时的填充策略
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新填充 ....");

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);

        log.debug("更新填充完成 ....");
    }
}