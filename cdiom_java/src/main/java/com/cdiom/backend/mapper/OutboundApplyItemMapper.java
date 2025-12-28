package com.cdiom.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdiom.backend.model.OutboundApplyItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OutboundApplyItemMapper extends BaseMapper<OutboundApplyItem> {

    /**
     * 根据申领单ID查询申领明细
     */
    @Select("SELECT oai.*, d.drug_name, d.drug_code, d.unit FROM outbound_apply_item oai " +
            "LEFT JOIN drug_info d ON oai.drug_id = d.id " +
            "WHERE oai.apply_id = #{applyId} ORDER BY oai.create_time ASC")
    List<OutboundApplyItem> selectByApplyIdWithDrugInfo(@Param("applyId") Long applyId);

    /**
     * 根据药品ID查询申领明细
     */
    @Select("SELECT oai.*, oa.apply_number, oa.department FROM outbound_apply_item oai " +
            "LEFT JOIN outbound_apply oa ON oai.apply_id = oa.id " +
            "WHERE oai.drug_id = #{drugId} ORDER BY oai.create_time DESC")
    List<OutboundApplyItem> selectByDrugIdWithApplyInfo(@Param("drugId") Long drugId);

    /**
     * 统计药品申领总量
     */
    @Select("SELECT drug_id, d.drug_name, SUM(apply_quantity) as total_apply_quantity, " +
            "SUM(COALESCE(approved_quantity, 0)) as total_approved_quantity, " +
            "SUM(COALESCE(actual_quantity, 0)) as total_actual_quantity FROM outbound_apply_item oai " +
            "LEFT JOIN drug_info d ON oai.drug_id = d.id " +
            "WHERE oai.create_time >= #{startDate} AND oai.create_time <= #{endDate} " +
            "GROUP BY drug_id, d.drug_name ORDER BY total_apply_quantity DESC")
    List<OutboundApplyItem> selectDrugApplySummary(@Param("startDate") String startDate,
                                                  @Param("endDate") String endDate);
}
