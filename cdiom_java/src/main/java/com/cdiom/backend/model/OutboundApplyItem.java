package com.cdiom.backend.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("outbound_apply_item")
public class OutboundApplyItem {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("apply_id")
    private Long applyId;

    @TableField("drug_id")
    private Long drugId;

    @TableField("apply_quantity")
    private Integer applyQuantity;

    @TableField("approved_quantity")
    private Integer approvedQuantity;

    @TableField("actual_quantity")
    private Integer actualQuantity;

    @TableField("batch_number")
    private String batchNumber;

    @TableField("remarks")
    private String remarks;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
