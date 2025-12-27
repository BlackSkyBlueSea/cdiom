package com.cdiom.backend.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("inventory")
public class Inventory {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("drug_id")
    private Long drugId;

    @TableField("batch_number")
    private String batchNumber;

    @TableField("quantity")
    private Integer quantity;

    @TableField("location")
    private String location;

    @TableField("expiry_date")
    private LocalDate expiryDate;

    @TableField("warning_quantity")
    private Integer warningQuantity;

    @TableField("status")
    private Integer status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
