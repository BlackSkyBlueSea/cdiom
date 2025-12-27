package com.cdiom.backend.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("outbound_apply")
public class OutboundApply {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("apply_number")
    private String applyNumber;

    @TableField("applicant_id")
    private Long applicantId;

    @TableField("department")
    private String department;

    @TableField("apply_date")
    private LocalDateTime applyDate;

    @TableField("reason")
    private String reason;

    @TableField("urgency_level")
    private String urgencyLevel;

    @TableField("total_quantity")
    private Integer totalQuantity;

    @TableField("status")
    private String status;

    @TableField("approver_id")
    private Long approverId;

    @TableField("approval_time")
    private LocalDateTime approvalTime;

    @TableField("approval_opinion")
    private String approvalOpinion;

    @TableField("remarks")
    private String remarks;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
