package com.cdiom.backend.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("operation_log")
public class OperationLog {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("operator_id")
    private Long operatorId;

    @TableField("operator_name")
    private String operatorName;

    @TableField("operation_type")
    private String operationType;

    @TableField("operation_module")
    private String operationModule;

    @TableField("operation_description")
    private String operationDescription;

    @TableField("operation_time")
    private LocalDateTime operationTime;

    @TableField("ip_address")
    private String ipAddress;

    @TableField("user_agent")
    private String userAgent;

    @TableField("request_url")
    private String requestUrl;

    @TableField("request_method")
    private String requestMethod;

    @TableField("request_params")
    private String requestParams;

    @TableField("response_result")
    private String responseResult;

    @TableField("execution_time")
    private Long executionTime;

    @TableField("status")
    private Integer status;

    @TableField("error_message")
    private String errorMessage;

    @TableField("data_snapshot_before")
    private String dataSnapshotBefore;

    @TableField("data_snapshot_after")
    private String dataSnapshotAfter;

    @TableField("create_time")
    private LocalDateTime createTime;
}
