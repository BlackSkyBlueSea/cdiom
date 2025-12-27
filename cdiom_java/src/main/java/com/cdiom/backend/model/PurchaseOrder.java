package com.cdiom.backend.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("purchase_order")
public class PurchaseOrder {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_number")
    private String orderNumber;

    @TableField("supplier")
    private String supplier;

    @TableField("order_date")
    private LocalDateTime orderDate;

    @TableField("expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("status")
    private String status;

    @TableField("approver_id")
    private Long approverId;

    @TableField("approval_time")
    private LocalDateTime approvalTime;

    @TableField("remarks")
    private String remarks;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
