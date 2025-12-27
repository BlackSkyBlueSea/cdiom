package com.cdiom.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdiom.backend.model.OperationLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface OperationLogMapper extends BaseMapper<OperationLog> {

    /**
     * 根据操作人ID查询操作日志
     */
    @Select("SELECT * FROM operation_log WHERE operator_id = #{operatorId} ORDER BY operation_time DESC")
    List<OperationLog> selectByOperatorId(@Param("operatorId") Long operatorId);

    /**
     * 根据操作类型查询日志
     */
    @Select("SELECT * FROM operation_log WHERE operation_type = #{operationType} ORDER BY operation_time DESC")
    List<OperationLog> selectByOperationType(@Param("operationType") String operationType);

    /**
     * 根据操作模块查询日志
     */
    @Select("SELECT * FROM operation_log WHERE operation_module = #{operationModule} ORDER BY operation_time DESC")
    List<OperationLog> selectByOperationModule(@Param("operationModule") String operationModule);

    /**
     * 查询指定时间范围内的操作日志
     */
    @Select("SELECT * FROM operation_log WHERE operation_time BETWEEN #{startTime} AND #{endTime} ORDER BY operation_time DESC")
    List<OperationLog> selectByTimeRange(@Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);

    /**
     * 查询失败的操作日志
     */
    @Select("SELECT * FROM operation_log WHERE status = 0 ORDER BY operation_time DESC")
    List<OperationLog> selectFailedOperations();

    /**
     * 分页查询操作日志（多条件筛选）
     */
    @Select("<script>" +
            "SELECT ol.*, u.real_name as operator_real_name FROM operation_log ol " +
            "LEFT JOIN sys_user u ON ol.operator_id = u.id WHERE 1=1 " +
            "<if test='operatorName != null and operatorName != \"\"'>" +
            "AND (ol.operator_name LIKE CONCAT('%', #{operatorName}, '%') OR u.real_name LIKE CONCAT('%', #{operatorName}, '%')) " +
            "</if>" +
            "<if test='operationType != null and operationType != \"\"'>" +
            "AND ol.operation_type = #{operationType} " +
            "</if>" +
            "<if test='operationModule != null and operationModule != \"\"'>" +
            "AND ol.operation_module = #{operationModule} " +
            "</if>" +
            "<if test='ipAddress != null and ipAddress != \"\"'>" +
            "AND ol.ip_address LIKE CONCAT('%', #{ipAddress}, '%') " +
            "</if>" +
            "<if test='status != null'>" +
            "AND ol.status = #{status} " +
            "</if>" +
            "<if test='startTime != null'>" +
            "AND ol.operation_time >= #{startTime} " +
            "</if>" +
            "<if test='endTime != null'>" +
            "AND ol.operation_time <= #{endTime} " +
            "</if>" +
            "ORDER BY ol.operation_time DESC" +
            "</script>")
    IPage<OperationLog> selectPageByConditions(Page<OperationLog> page,
                                             @Param("operatorName") String operatorName,
                                             @Param("operationType") String operationType,
                                             @Param("operationModule") String operationModule,
                                             @Param("ipAddress") String ipAddress,
                                             @Param("status") Integer status,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);
}
