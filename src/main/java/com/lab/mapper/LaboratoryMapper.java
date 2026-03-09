package com.lab.mapper;

import com.lab.pojo.Laboratory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LaboratoryMapper {

    // 分页查询所有实验室
    @Select("select * from laboratory order by create_time desc limit #{offset}, #{pageSize}")
    List<Laboratory> findPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    // 统计总数
    @Select("select count(*) from laboratory")
    Long countAll();

    // 查询所有状态正常的实验室（status=1，不分页）
    @Select("select * from laboratory where status = 1 order by create_time desc")
    List<Laboratory> findAvailableAll();

    // 根据ID查询实验室
    @Select("select * from laboratory where id = #{id}")
    Laboratory findById(Integer id);

    // 根据房间号查询实验室（用于检查重复）
    @Select("select * from laboratory where lab_number = #{labNumber}")
    Laboratory findByLabNumber(String labNumber);

    // 新增实验室
    @Insert("insert into laboratory(lab_number, lab_name, colleges, equipment_list, responsible_teacher_id, location, " +
            "capacity, area, equipment_count, status, description, create_time, update_time) " +
            "values(#{labNumber}, #{labName}, #{colleges}, #{equipmentList}, #{responsibleTeacherId}, #{location}, " +
            "#{capacity}, #{area}, #{equipmentCount}, #{status}, #{description}, now(), now())")
    void add(Laboratory laboratory);

    // 更新实验室信息
    @Update("update laboratory set lab_name=#{labName}, colleges=#{colleges}, equipment_list=#{equipmentList}, responsible_teacher_id=#{responsibleTeacherId}, " +
            "location=#{location}, capacity=#{capacity}, area=#{area}, equipment_count=#{equipmentCount}, " +
            "status=#{status}, description=#{description}, update_time=now() " +
            "where id=#{id}")
    void update(Laboratory laboratory);

    // 更新设备总数
    @Update("update laboratory set equipment_count=#{count}, update_time=now() where id=#{id}")
    void updateEquipmentCount(@Param("id") Integer id, @Param("count") Integer count);

    // 删除实验室
    @Delete("delete from laboratory where id = #{id}")
    void delete(Integer id);
}

