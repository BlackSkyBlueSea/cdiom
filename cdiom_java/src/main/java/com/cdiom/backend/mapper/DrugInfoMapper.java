package com.cdiom.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdiom.backend.model.DrugInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

public interface DrugInfoMapper extends BaseMapper<DrugInfo> {

    /**
     * 根据药品编码查询药品
     */
    @Select("SELECT * FROM drug_info WHERE drug_code = #{drugCode}")
    DrugInfo selectByDrugCode(@Param("drugCode") String drugCode);

    /**
     * 根据药品名称模糊查询
     */
    @Select("SELECT * FROM drug_info WHERE drug_name LIKE CONCAT('%', #{drugName}, '%') AND status = 1 ORDER BY create_time DESC")
    List<DrugInfo> selectByDrugName(@Param("drugName") String drugName);

    /**
     * 根据批号查询药品
     */
    @Select("SELECT * FROM drug_info WHERE batch_number = #{batchNumber}")
    DrugInfo selectByBatchNumber(@Param("batchNumber") String batchNumber);

    /**
     * 查询即将过期药品（30天内过期）
     */
    @Select("SELECT * FROM drug_info WHERE validity_period BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) AND status = 1 ORDER BY validity_period ASC")
    List<DrugInfo> selectExpiringSoon();

    /**
     * 查询特殊药品
     */
    @Select("SELECT * FROM drug_info WHERE is_special_drug = 1 AND status = 1 ORDER BY create_time DESC")
    List<DrugInfo> selectSpecialDrugs();

    /**
     * 根据分类查询药品
     */
    @Select("SELECT * FROM drug_info WHERE category = #{category} AND status = 1 ORDER BY drug_name ASC")
    List<DrugInfo> selectByCategory(@Param("category") String category);

    /**
     * 分页查询药品（多条件筛选）
     */
    @Select("<script>" +
            "SELECT * FROM drug_info WHERE status = 1 " +
            "<if test='drugName != null and drugName != \"\"'>" +
            "AND (drug_name LIKE CONCAT('%', #{drugName}, '%') OR generic_name LIKE CONCAT('%', #{drugName}, '%')) " +
            "</if>" +
            "<if test='drugCode != null and drugCode != \"\"'>" +
            "AND drug_code LIKE CONCAT('%', #{drugCode}, '%') " +
            "</if>" +
            "<if test='category != null and category != \"\"'>" +
            "AND category = #{category} " +
            "</if>" +
            "<if test='manufacturer != null and manufacturer != \"\"'>" +
            "AND manufacturer LIKE CONCAT('%', #{manufacturer}, '%') " +
            "</if>" +
            "<if test='isSpecialDrug != null'>" +
            "AND is_special_drug = #{isSpecialDrug} " +
            "</if>" +
            "ORDER BY create_time DESC" +
            "</script>")
    IPage<DrugInfo> selectPageByConditions(Page<DrugInfo> page,
                                         @Param("drugName") String drugName,
                                         @Param("drugCode") String drugCode,
                                         @Param("category") String category,
                                         @Param("manufacturer") String manufacturer,
                                         @Param("isSpecialDrug") Integer isSpecialDrug);
}
