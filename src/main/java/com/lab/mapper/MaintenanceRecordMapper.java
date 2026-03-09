package com.lab.mapper;

import com.lab.pojo.MaintenanceRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MaintenanceRecordMapper {

    @Insert("insert into maintenance_record(record_no, " +
            "laboratory_id, lab_number, lab_name, location, " +
            "equipment_id, equipment_name, equipment_type, equipment_model, asset_number, " +
            "reporter_id, reporter_name, reporter_role, " +
            "issue_type, description, photos, status, " +
            "assigned_teacher_id, assigned_teacher_name, " +
            "repair_start_time, repair_end_time, repair_result, cost, create_time, update_time) " +
            "values(#{recordNo}, " +
            "#{laboratoryId}, #{labNumber}, #{labName}, #{location}, " +
            "#{equipmentId}, #{equipmentName}, #{equipmentType}, #{equipmentModel}, #{assetNumber}, " +
            "#{reporterId}, #{reporterName}, #{reporterRole}, " +
            "#{issueType}, #{description}, #{photos}, #{status}, " +
            "#{assignedTeacherId}, #{assignedTeacherName}, " +
            "#{repairStartTime}, #{repairEndTime}, #{repairResult}, #{cost}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(MaintenanceRecord record);

    @Select("select * from maintenance_record where id = #{id}")
    MaintenanceRecord findById(Integer id);

    @Select("select max(record_no) from maintenance_record where record_no like concat(#{prefix}, '%')")
    String findMaxRecordNoByPrefix(@Param("prefix") String prefix);

    @Select("select * from maintenance_record where reporter_id = #{reporterId} order by create_time desc limit #{offset}, #{pageSize}")
    List<MaintenanceRecord> findByReporterId(@Param("reporterId") Integer reporterId,
                                            @Param("offset") Integer offset,
                                            @Param("pageSize") Integer pageSize);

    @Select("select count(*) from maintenance_record where reporter_id = #{reporterId}")
    Long countByReporterId(@Param("reporterId") Integer reporterId);

    @Select("select * from maintenance_record order by create_time desc limit #{offset}, #{pageSize}")
    List<MaintenanceRecord> findAll(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    @Select("select count(*) from maintenance_record")
    Long countAll();

    @Select("select * from maintenance_record where assigned_teacher_id = #{teacherId} and status in (0, 1) order by create_time desc limit #{offset}, #{pageSize}")
    List<MaintenanceRecord> findTodoByTeacherId(@Param("teacherId") Integer teacherId,
                                               @Param("offset") Integer offset,
                                               @Param("pageSize") Integer pageSize);

    @Select("select count(*) from maintenance_record where assigned_teacher_id = #{teacherId} and status in (0, 1)")
    Long countTodoByTeacherId(@Param("teacherId") Integer teacherId);

    @Update("update maintenance_record set assigned_teacher_id = #{teacherId}, assigned_teacher_name = #{teacherName}, update_time = now() where id = #{id}")
    void assignTeacher(@Param("id") Integer id, @Param("teacherId") Integer teacherId, @Param("teacherName") String teacherName);

    @Update("update maintenance_record set status = 1, repair_start_time = now(), update_time = now() where id = #{id}")
    void startRepair(@Param("id") Integer id);

    @Update("update maintenance_record set status = 2, repair_end_time = now(), repair_result = #{repairResult}, cost = #{cost}, update_time = now() where id = #{id}")
    void completeRepair(@Param("id") Integer id,
                        @Param("repairResult") String repairResult,
                        @Param("cost") java.math.BigDecimal cost);

    @Select("select count(*) from maintenance_record where status = #{status}")
    Long countByStatus(@Param("status") Integer status);

    @Select("select count(*) from maintenance_record where assigned_teacher_id = #{teacherId} and status = #{status}")
    Long countByTeacherAndStatus(@Param("teacherId") Integer teacherId, @Param("status") Integer status);

    @Update("update maintenance_record set status = #{status}, update_time = now() where id = #{id}")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    
    @Select("select count(*) from maintenance_record where status in (#{status1}, #{status2})")
    Integer countByStatusIn(@Param("status1") Integer status1, @Param("status2") Integer status2);
    
    @Select("select count(*) from maintenance_record where date(create_time) = #{date}")
    Integer countByDate(@Param("date") java.time.LocalDate date);
    
    @Select("select * from maintenance_record where date(create_time) = #{date} order by create_time desc limit #{limit}")
    List<MaintenanceRecord> findByDate(@Param("date") java.time.LocalDate date, @Param("limit") Integer limit);
}
