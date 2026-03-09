package com.lab.mapper;

import com.lab.pojo.LaboratoryEquipment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LaboratoryEquipmentMapper {

    // 根据实验室ID查询关联的设备
    @Select("select * from laboratory_equipment where laboratory_id = #{laboratoryId}")
    List<LaboratoryEquipment> findByLaboratoryId(Integer laboratoryId);

    // 根据设备ID查询关联的实验室
    @Select("select * from laboratory_equipment where equipment_id = #{equipmentId}")
    List<LaboratoryEquipment> findByEquipmentId(Integer equipmentId);

    // 检查实验室和设备是否已关联
    @Select("select * from laboratory_equipment where laboratory_id = #{laboratoryId} and equipment_id = #{equipmentId}")
    LaboratoryEquipment findByLaboratoryAndEquipment(@Param("laboratoryId") Integer laboratoryId,
                                                     @Param("equipmentId") Integer equipmentId);

    // 添加设备到实验室
    @Insert("insert into laboratory_equipment(laboratory_id, equipment_id, quantity, create_time, update_time) " +
            "values(#{laboratoryId}, #{equipmentId}, #{quantity}, now(), now())")
    void add(LaboratoryEquipment laboratoryEquipment);

    // 更新设备数量
    @Update("update laboratory_equipment set quantity=#{quantity}, update_time=now() " +
            "where laboratory_id=#{laboratoryId} and equipment_id=#{equipmentId}")
    void updateQuantity(@Param("laboratoryId") Integer laboratoryId,
                       @Param("equipmentId") Integer equipmentId,
                       @Param("quantity") Integer quantity);

    // 从实验室移除设备
    @Delete("delete from laboratory_equipment where laboratory_id = #{laboratoryId} and equipment_id = #{equipmentId}")
    void remove(@Param("laboratoryId") Integer laboratoryId, @Param("equipmentId") Integer equipmentId);

    // 删除实验室的所有设备关联
    @Delete("delete from laboratory_equipment where laboratory_id = #{laboratoryId}")
    void deleteByLaboratoryId(Integer laboratoryId);

    // 删除设备的所有实验室关联
    @Delete("delete from laboratory_equipment where equipment_id = #{equipmentId}")
    void deleteByEquipmentId(Integer equipmentId);
}

