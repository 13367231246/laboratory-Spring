package com.lab.mapper;

import com.lab.pojo.EquipmentApplication;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EquipmentApplicationMapper {

    @Insert("insert into equipment_application(application_no, " +
            "equipment_id, equipment_name, equipment_type, equipment_model, asset_number, " +
            "laboratory_id, lab_number, lab_name, location, " +
            "applicant_id, applicant_role, applicant_real_name, applicant_phone, applicant_email, quantity, purpose, start_time, end_time, status, create_time, update_time) " +
            "values(#{applicationNo}, " +
            "#{equipmentId}, #{equipmentName}, #{equipmentType}, #{equipmentModel}, #{assetNumber}, " +
            "#{laboratoryId}, #{labNumber}, #{labName}, #{location}, " +
            "#{applicantId}, #{applicantRole}, #{applicantRealName}, #{applicantPhone}, #{applicantEmail}, #{quantity}, #{purpose}, #{startTime}, #{endTime}, #{status}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(EquipmentApplication application);

    @Select("select * from equipment_application where id = #{id}")
    EquipmentApplication findById(Integer id);

    @Select("select * from equipment_application where applicant_id = #{applicantId} order by create_time desc limit #{offset}, #{pageSize}")
    List<EquipmentApplication> findByApplicantId(@Param("applicantId") Integer applicantId,
                                                  @Param("offset") Integer offset,
                                                  @Param("pageSize") Integer pageSize);

    @Select("select count(*) from equipment_application where applicant_id = #{applicantId}")
    Long countByApplicantId(Integer applicantId);

    @Select("select * from equipment_application order by create_time desc limit #{offset}, #{pageSize}")
    List<EquipmentApplication> findAll(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    @Select("select count(*) from equipment_application")
    Long countAll();

    /** 同一设备、时间重叠、且状态为已批准(1)或使用中(3)的记录（用于申请时校验冲突） */
    @Select("select * from equipment_application where equipment_id = #{equipmentId} and status in (1, 3) " +
            "and ((start_time < #{endTime} and end_time > #{startTime}))")
    List<EquipmentApplication> findOverlapping(@Param("equipmentId") Integer equipmentId,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);

    /** 同一设备、时间重叠、状态为已批准(1)或使用中(3)的申请数量之和（用于校验可用数量） */
    @Select("select coalesce(sum(quantity), 0) from equipment_application where equipment_id = #{equipmentId} and status in (1, 3) " +
            "and ((start_time < #{endTime} and end_time > #{startTime}))")
    Integer sumOccupiedQuantity(@Param("equipmentId") Integer equipmentId,
                                 @Param("startTime") LocalDateTime startTime,
                                 @Param("endTime") LocalDateTime endTime);

    @Update("update equipment_application set status = #{status}, update_time = now() where id = #{id}")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Update("update equipment_application set status = #{status}, completion_time = now(), update_time = now() where id = #{id}")
    void complete(@Param("id") Integer id, @Param("status") Integer status);

    @Update("update equipment_application set status = #{status}, reviewer_id = #{reviewerId}, reviewer_name = #{reviewerName}, " +
            "reviewer_role = #{reviewerRole}, review_time = now(), review_comment = #{reviewComment}, update_time = now() where id = #{id}")
    void review(@Param("id") Integer id,
                @Param("status") Integer status,
                @Param("reviewerId") Integer reviewerId,
                @Param("reviewerName") String reviewerName,
                @Param("reviewerRole") String reviewerRole,
                @Param("reviewComment") String reviewComment);

    @Delete("delete from equipment_application where id = #{id}")
    void delete(Integer id);

    @Select("select * from equipment_application where status = 3 and date_add(end_time, interval 3 hour) < now()")
    List<EquipmentApplication> findUsingPastDeadline();

    @Select("select application_no from equipment_application where application_no like concat('EQP', date_format(now(), '%Y%m%d'), '%') order by id desc limit 1")
    String findMaxApplicationNoToday();
    
    // 统计今日申请数量
    @Select("select count(*) from equipment_application where date(create_time) = curdate()")
    Integer countTodayApplications();
    
    // 获取今日申请（限制条数）
    @Select("select * from equipment_application where date(create_time) = curdate() order by create_time desc limit #{limit}")
    List<EquipmentApplication> findTodayApplications(@Param("limit") Integer limit);
}
