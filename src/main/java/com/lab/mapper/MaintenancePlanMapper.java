package com.lab.mapper;

import com.lab.pojo.MaintenancePlan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MaintenancePlanMapper {

    @Insert("insert into maintenance_plan(laboratory_id, plan_name, maintenance_type, maintenance_content, responsible_teacher_id, last_maintenance_time, next_maintenance_time, status, create_time, update_time) " +
            "values(#{laboratoryId}, #{planName}, #{maintenanceType}, #{maintenanceContent}, #{responsibleTeacherId}, #{lastMaintenanceTime}, #{nextMaintenanceTime}, #{status}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(MaintenancePlan plan);

    @Update("update maintenance_plan set laboratory_id=#{laboratoryId}, plan_name=#{planName}, maintenance_type=#{maintenanceType}, maintenance_content=#{maintenanceContent}, " +
            "responsible_teacher_id=#{responsibleTeacherId}, last_maintenance_time=#{lastMaintenanceTime}, next_maintenance_time=#{nextMaintenanceTime}, status=#{status}, update_time=now() where id=#{id}")
    void update(MaintenancePlan plan);

    @Select("select * from maintenance_plan where id = #{id}")
    MaintenancePlan findById(Integer id);

    @Delete("delete from maintenance_plan where id = #{id}")
    void delete(Integer id);

    @Select("select * from maintenance_plan order by create_time desc limit #{offset}, #{pageSize}")
    List<MaintenancePlan> findPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    @Select("select count(*) from maintenance_plan")
    Long countAll();

    @Select("select * from maintenance_plan where laboratory_id = #{laboratoryId} and status = 1 order by next_maintenance_time asc")
    List<MaintenancePlan> findEnabledByLaboratoryId(@Param("laboratoryId") Integer laboratoryId);

    @Update("update maintenance_plan set status=#{status}, update_time=now() where id=#{id}")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);
}
