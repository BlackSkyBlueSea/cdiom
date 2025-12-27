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
    IPage<OperationLog> selectPageByConditions(Page<OperationLog> page,
                                             @Param("operatorName") String operatorName,
                                             @Param("operationType") String operationType,
                                             @Param("operationModule") String operationModule,
                                             @Param("ipAddress") String ipAddress,
                                             @Param("status") Integer status,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);
}
