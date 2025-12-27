package com.cdiom.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdiom.backend.model.PurchaseOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {

    /**
     * 根据订单号查询订单
     */
    @Select("SELECT * FROM purchase_order WHERE order_number = #{orderNumber}")
    PurchaseOrder selectByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * 根据状态查询订单
     */
    @Select("SELECT po.*, u.real_name as approver_name FROM purchase_order po " +
            "LEFT JOIN sys_user u ON po.approver_id = u.id " +
            "WHERE po.status = #{status} ORDER BY po.order_date DESC")
    List<PurchaseOrder> selectByStatus(@Param("status") String status);

    /**
     * 根据供应商查询订单
     */
    @Select("SELECT * FROM purchase_order WHERE supplier LIKE CONCAT('%', #{supplier}, '%') ORDER BY order_date DESC")
    List<PurchaseOrder> selectBySupplier(@Param("supplier") String supplier);

    /**
     * 查询待审批订单
     */
    @Select("SELECT po.*, u.real_name as applicant_name FROM purchase_order po " +
            "LEFT JOIN sys_user u ON po.approver_id IS NULL " +
            "WHERE po.status = 'PENDING' ORDER BY po.order_date ASC")
    List<PurchaseOrder> selectPendingOrders();

    /**
     * 查询即将到货订单（7天内）
     */
    @Select("SELECT * FROM purchase_order WHERE expected_delivery_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY) " +
            "AND status IN ('PENDING', 'APPROVED') ORDER BY expected_delivery_date ASC")
    List<PurchaseOrder> selectUpcomingDeliveries();

    /**
     * 分页查询订单（多条件筛选）
     */
    @Select("SELECT po.*, u.real_name as approver_name FROM purchase_order po " +
            "LEFT JOIN sys_user u ON po.approver_id = u.id ORDER BY po.order_date DESC")
    IPage<PurchaseOrder> selectPageByConditions(Page<PurchaseOrder> page,
                                              @Param("orderNumber") String orderNumber,
                                              @Param("supplier") String supplier,
                                              @Param("status") String status,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
}
