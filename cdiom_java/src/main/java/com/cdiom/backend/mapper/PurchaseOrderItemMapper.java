package com.cdiom.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdiom.backend.model.PurchaseOrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PurchaseOrderItemMapper extends BaseMapper<PurchaseOrderItem> {

    /**
     * 根据订单ID查询订单明细
     */
    @Select("SELECT poi.*, d.drug_name, d.drug_code, d.unit FROM purchase_order_item poi " +
            "LEFT JOIN drug_info d ON poi.drug_id = d.id " +
            "WHERE poi.order_id = #{orderId} ORDER BY poi.create_time ASC")
    List<PurchaseOrderItem> selectByOrderIdWithDrugInfo(@Param("orderId") Long orderId);

    /**
     * 根据药品ID查询订单明细
     */
    @Select("SELECT poi.*, po.order_number, po.supplier FROM purchase_order_item poi " +
            "LEFT JOIN purchase_order po ON poi.order_id = po.id " +
            "WHERE poi.drug_id = #{drugId} ORDER BY poi.create_time DESC")
    List<PurchaseOrderItem> selectByDrugIdWithOrderInfo(@Param("drugId") Long drugId);

    /**
     * 统计药品采购总量
     */
    @Select("SELECT drug_id, d.drug_name, SUM(quantity) as total_quantity FROM purchase_order_item poi " +
            "LEFT JOIN drug_info d ON poi.drug_id = d.id " +
            "WHERE poi.create_time >= #{startDate} AND poi.create_time <= #{endDate} " +
            "GROUP BY drug_id, d.drug_name ORDER BY total_quantity DESC")
    List<PurchaseOrderItem> selectDrugPurchaseSummary(@Param("startDate") String startDate,
                                                     @Param("endDate") String endDate);
}
