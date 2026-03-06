package com.hss.mapper;

import com.hss.pojo.Equipment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EquipmentMapper {

    // 分页查询所有设备
    @Select("select * from equipment order by create_time desc limit #{offset}, #{pageSize}")
    List<Equipment> findPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    // 不分页查询所有可用设备，可按设备名模糊搜索（只返回 available_quantity > 0 且状态为启用的设备）
    @Select("<script>" +
            "select * from equipment " +
            "<where> " +
            "  available_quantity &gt; 0 " +
            "  and status = 1 " +
            "  <if test=\"equipmentName != null and equipmentName != ''\"> " +
            "    and equipment_name like concat('%', #{equipmentName}, '%') " +
            "  </if> " +
            "</where> " +
            "order by create_time desc" +
            "</script>")
    List<Equipment> findAll(@Param("equipmentName") String equipmentName);

    // 统计总数
    @Select("select count(*) from equipment")
    Long countAll();

    // 根据ID查询设备
    @Select("select * from equipment where id = #{id}")
    Equipment findById(Integer id);

    // 根据资产编号查询设备（用于检查重复）
    @Select("select * from equipment where asset_number = #{assetNumber}")
    Equipment findByAssetNumber(String assetNumber);

    // 新增设备
    @Insert("insert into equipment(asset_number, equipment_name, equipment_type, model, manufacturer, " +
            "purchase_date, price, responsible_teacher_id, status, quantity, available_quantity, description, image, create_time, update_time) " +
            "values(#{assetNumber}, #{equipmentName}, #{equipmentType}, #{model}, #{manufacturer}, " +
            "#{purchaseDate}, #{price}, #{responsibleTeacherId}, #{status}, #{quantity}, #{availableQuantity}, #{description}, #{image}, now(), now())")
    void add(Equipment equipment);

    // 更新设备信息
    @Update("update equipment set equipment_name=#{equipmentName}, equipment_type=#{equipmentType}, model=#{model}, " +
            "manufacturer=#{manufacturer}, purchase_date=#{purchaseDate}, price=#{price}, responsible_teacher_id=#{responsibleTeacherId}, status=#{status}, quantity=#{quantity}, " +
            "available_quantity=#{availableQuantity}, description=#{description}, image=#{image}, update_time=now() " +
            "where id=#{id}")
    void update(Equipment equipment);

    // 仅更新设备的可用数量
    @Update("update equipment set available_quantity=#{availableQuantity}, update_time=now() where id=#{id}")
    void updateAvailableQuantity(@Param("id") Integer id, @Param("availableQuantity") Integer availableQuantity);

    // 删除设备
    @Delete("delete from equipment where id = #{id}")
    void delete(Integer id);
}

