package com.lab.mapper;

import com.lab.pojo.LabApplication;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LabApplicationMapper {

    @Insert("insert into lab_application(application_no, laboratory_id, lab_number, lab_name, location, applicant_id, applicant_role, applicant_real_name, applicant_phone, applicant_email, purpose, " +
            "course_name, class_name, student_count, start_time, end_time, status, create_time, update_time) " +
            "values(#{applicationNo}, #{laboratoryId}, #{labNumber}, #{labName}, #{location}, #{applicantId}, #{applicantRole}, #{applicantRealName}, #{applicantPhone}, #{applicantEmail}, #{purpose}, " +
            "#{courseName}, #{className}, #{studentCount}, #{startTime}, #{endTime}, #{status}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(LabApplication application);

    @Select("select * from lab_application where id = #{id}")
    LabApplication findById(Integer id);

    @Select("select * from lab_application where applicant_id = #{applicantId} order by create_time desc limit #{offset}, #{pageSize}")
    List<LabApplication> findByApplicantId(@Param("applicantId") Integer applicantId,
                                           @Param("offset") Integer offset,
                                           @Param("pageSize") Integer pageSize);

    @Select("select count(*) from lab_application where applicant_id = #{applicantId}")
    Long countByApplicantId(Integer applicantId);

    @Select("select * from lab_application order by create_time desc limit #{offset}, #{pageSize}")
    List<LabApplication> findAll(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    @Select("select count(*) from lab_application")
    Long countAll();

    /** 同一实验室、时间重叠、且状态为已批准(1)或使用中(3)的记录（用于申请时校验冲突） */
    @Select("select * from lab_application where laboratory_id = #{laboratoryId} and status in (1, 3) " +
            "and ((start_time < #{endTime} and end_time > #{startTime}))")
    List<LabApplication> findOverlapping(@Param("laboratoryId") Integer laboratoryId,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    @Update("update lab_application set status = #{status}, update_time = now() where id = #{id}")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Update("update lab_application set status = #{status}, completion_time = now(), update_time = now() where id = #{id}")
    void complete(@Param("id") Integer id, @Param("status") Integer status);

    /** 状态为使用中(3)且 结束时间+3小时 < 当前时间 的申请（定时任务自动完成） */
    @Select("select * from lab_application where status = 3 and date_add(end_time, interval 3 hour) < now()")
    List<LabApplication> findUsingPastDeadline();

    @Select("select * from lab_application where application_no = #{applicationNo}")
    LabApplication findByApplicationNo(String applicationNo);

    @Select("select application_no from lab_application where application_no like concat('LAB', date_format(now(), '%Y%m%d'), '%') order by id desc limit 1")
    String findMaxApplicationNoToday();

    @Update("update lab_application set status = #{status}, reviewer_id = #{reviewerId}, reviewer_name = #{reviewerName}, reviewer_role = #{reviewerRole}, review_time = now(), review_comment = #{reviewComment}, update_time = now() where id = #{id}")
    void review(@Param("id") Integer id,
                @Param("status") Integer status,
                @Param("reviewerId") Integer reviewerId,
                @Param("reviewerName") String reviewerName,
                @Param("reviewerRole") String reviewerRole,
                @Param("reviewComment") String reviewComment);

    @Delete("delete from lab_application where id = #{id}")
    void delete(Integer id);
    
    @Select("select count(distinct laboratory_id) from lab_application where applicant_id = #{applicantId} and status in (#{status1}, #{status2})")
    Integer countDistinctLaboratoryByApplicantIdAndStatus(@Param("applicantId") Integer applicantId, 
                                                         @Param("status1") Integer status1, 
                                                         @Param("status2") Integer status2);
    
    // 统计今日申请数量
    @Select("select count(*) from lab_application where date(create_time) = curdate()")
    Integer countTodayApplications();
    
    // 获取今日申请（限制条数）
    @Select("select * from lab_application where date(create_time) = curdate() order by create_time desc limit #{limit}")
    List<LabApplication> findTodayApplications(@Param("limit") Integer limit);
}
