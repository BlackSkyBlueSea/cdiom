package com.cdiom.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdiom.backend.model.Inventory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface InventoryMapper extends BaseMapper<Inventory> {

    /**
     * 根据药品ID和批号查询库存
     */
    @Select("SELECT * FROM inventory WHERE drug_id = #{drugId} AND batch_number = #{batchNumber}")
    Inventory selectByDrugAndBatch(@Param("drugId") Long drugId, @Param("batchNumber") String batchNumber);

    /**
     * 根据药品ID查询所有库存记录
     */
    @Select("SELECT i.*, d.drug_name, d.drug_code, d.unit FROM inventory i " +
            "LEFT JOIN drug_info d ON i.drug_id = d.id " +
            "WHERE i.drug_id = #{drugId} AND i.status = 1 ORDER BY i.expiry_date ASC")
    List<Inventory> selectByDrugIdWithDrugInfo(@Param("drugId") Long drugId);

    /**
     * 查询库存预警（低于预警数量）
     */
    @Select("SELECT i.*, d.drug_name, d.drug_code, d.unit FROM inventory i " +
            "LEFT JOIN drug_info d ON i.drug_id = d.id " +
            "WHERE i.quantity <= i.warning_quantity AND i.status = 1 ORDER BY i.quantity ASC")
    List<Inventory> selectLowStockWarnings();

    /**
     * 查询即将过期库存（30天内过期）
     */
    @Select("SELECT i.*, d.drug_name, d.drug_code, d.unit FROM inventory i " +
            "LEFT JOIN drug_info d ON i.drug_id = d.id " +
            "WHERE i.expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) AND i.status = 1 " +
            "ORDER BY i.expiry_date ASC")
    List<Inventory> selectExpiringInventory();

    /**
     * 查询已过期库存
     */
    @Select("SELECT i.*, d.drug_name, d.drug_code, d.unit FROM inventory i " +
            "LEFT JOIN drug_info d ON i.drug_id = d.id " +
            "WHERE i.expiry_date < CURDATE() AND i.status = 1 ORDER BY i.expiry_date ASC")
    List<Inventory> selectExpiredInventory();

    /**
     * 根据位置查询库存
     */
    @Select("SELECT i.*, d.drug_name, d.drug_code FROM inventory i " +
            "LEFT JOIN drug_info d ON i.drug_id = d.id " +
            "WHERE i.location = #{location} AND i.status = 1 ORDER BY i.create_time DESC")
    List<Inventory> selectByLocation(@Param("location") String location);

    /**
     * 分页查询库存（包含药品信息）
     */
    @Select("SELECT i.*, d.drug_name, d.drug_code, d.unit, d.category FROM inventory i " +
            "LEFT JOIN drug_info d ON i.drug_id = d.id WHERE i.status = 1 ORDER BY i.create_time DESC")
    IPage<Inventory> selectPageWithDrugInfo(Page<Inventory> page,
                                          @Param("drugName") String drugName,
                                          @Param("drugCode") String drugCode,
                                          @Param("location") String location);
}
