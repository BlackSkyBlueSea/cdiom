package com.cdiom.backend.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("drug_info")
public class DrugInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("drug_code")
    private String drugCode;

    @TableField("drug_name")
    private String drugName;

    @TableField("generic_name")
    private String genericName;

    @TableField("specification")
    private String specification;

    @TableField("manufacturer")
    private String manufacturer;

    @TableField("batch_number")
    private String batchNumber;

    @TableField("production_date")
    private LocalDate productionDate;

    @TableField("validity_period")
    private LocalDate validityPeriod;

    @TableField("unit")
    private String unit;

    @TableField("unit_price")
    private BigDecimal unitPrice;

    @TableField("category")
    private String category;

    @TableField("is_special_drug")
    private Integer isSpecialDrug;

    @TableField("storage_condition")
    private String storageCondition;

    @TableField("description")
    private String description;

    @TableField("status")
    private Integer status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
