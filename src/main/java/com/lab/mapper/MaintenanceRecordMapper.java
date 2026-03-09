package com.lab.mapper;

import com.lab.pojo.MaintenanceRecord;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MaintenanceRecordMapper {

    @Insert("insert into maintenance_record(record_no, laboratory_id, equipment_id, reporter_id, reporter_role, issue_type, description, photos, status, assigned_teacher_id, repair_start_time, repair_end_time, repair_result, cost, create_time, update_time) " +
            "values(#{recordNo}, #{laboratoryId}, #{equipmentId}, #{reporterId}, #{reporterRole}, #{issueType}, #{description}, #{photos}, #{status}, #{assignedTeacherId}, #{repairStartTime}, #{repairEndTime}, #{repairResult}, #{cost}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(MaintenanceRecord record);

    @Select("select * from maintenance_record where id = #{id}")
    MaintenanceRecord findById(Integer id);

    @Select("select max(record_no) from maintenance_record where record_no like concat(#{prefix}, '%')")
    String findMaxRecordNoByPrefix(@Param("prefix") String prefix);
}
