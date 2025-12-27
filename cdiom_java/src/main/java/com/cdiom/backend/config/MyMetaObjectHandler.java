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
     * 插入操作时的自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入操作自动填充...");

        // 自动填充创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());

        // 自动填充更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        log.debug("插入操作自动填充完成");
    }

    /**
     * 更新操作时的自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新操作自动填充...");

        // 自动填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        log.debug("更新操作自动填充完成");
    }

    /**
     * 严格的插入填充（字段存在时才填充）
     */
    private void strictInsertFill(MetaObject metaObject, String fieldName, Class<?> fieldType, Object fieldVal) {
        if (metaObject.hasGetter(fieldName) && metaObject.hasSetter(fieldName)) {
            Object value = this.getFieldValByName(fieldName, metaObject);
            if (value == null) {
                this.setFieldValByName(fieldName, fieldVal, metaObject);
                log.debug("自动填充字段: {}, 值: {}", fieldName, fieldVal);
            }
        }
    }

    /**
     * 严格的更新填充（字段存在时才填充）
     */
    private void strictUpdateFill(MetaObject metaObject, String fieldName, Class<?> fieldType, Object fieldVal) {
        if (metaObject.hasGetter(fieldName) && metaObject.hasSetter(fieldName)) {
            this.setFieldValByName(fieldName, fieldVal, metaObject);
            log.debug("自动填充字段: {}, 值: {}", fieldName, fieldVal);
        }
    }
}
