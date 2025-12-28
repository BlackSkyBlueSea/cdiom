package com.cdiom.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdiom.backend.model.OutboundApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OutboundApplyMapper extends BaseMapper<OutboundApply> {

    /**
     * 根据申领单号查询申领单
     */
    @Select("SELECT * FROM outbound_apply WHERE apply_number = #{applyNumber}")
    OutboundApply selectByApplyNumber(@Param("applyNumber") String applyNumber);

    /**
     * 根据申领人ID查询申领单
     */
    @Select("SELECT oa.*, u.real_name as applicant_name FROM outbound_apply oa " +
            "LEFT JOIN sys_user u ON oa.applicant_id = u.id " +
            "WHERE oa.applicant_id = #{applicantId} ORDER BY oa.apply_date DESC")
    List<OutboundApply> selectByApplicantId(@Param("applicantId") Long applicantId);

    /**
     * 根据状态查询申领单
     */
    @Select("SELECT oa.*, u1.real_name as applicant_name, u2.real_name as approver_name FROM outbound_apply oa " +
            "LEFT JOIN sys_user u1 ON oa.applicant_id = u1.id " +
            "LEFT JOIN sys_user u2 ON oa.approver_id = u2.id " +
            "WHERE oa.status = #{status} ORDER BY oa.apply_date DESC")
    List<OutboundApply> selectByStatus(@Param("status") String status);

    /**
     * 查询待审批申领单
     */
    @Select("SELECT oa.*, u.real_name as applicant_name FROM outbound_apply oa " +
            "LEFT JOIN sys_user u ON oa.applicant_id = u.id " +
            "WHERE oa.status = 'PENDING' ORDER BY oa.apply_date ASC")
    List<OutboundApply> selectPendingApplies();

    /**
     * 根据紧急程度查询申领单
     */
    @Select("SELECT oa.*, u.real_name as applicant_name FROM outbound_apply oa " +
            "LEFT JOIN sys_user u ON oa.applicant_id = u.id " +
            "WHERE oa.urgency_level = #{urgencyLevel} ORDER BY oa.apply_date DESC")
    List<OutboundApply> selectByUrgencyLevel(@Param("urgencyLevel") String urgencyLevel);

    /**
     * 分页查询申领单（多条件筛选）
     */
    @Select("SELECT oa.*, u1.real_name as applicant_name, u2.real_name as approver_name FROM outbound_apply oa " +
            "LEFT JOIN sys_user u1 ON oa.applicant_id = u1.id " +
            "LEFT JOIN sys_user u2 ON oa.approver_id = u2.id ORDER BY oa.apply_date DESC")
    IPage<OutboundApply> selectPageByConditions(Page<OutboundApply> page,
                                              @Param("applyNumber") String applyNumber,
                                              @Param("applicantName") String applicantName,
                                              @Param("department") String department,
                                              @Param("status") String status,
                                              @Param("urgencyLevel") String urgencyLevel,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
}
