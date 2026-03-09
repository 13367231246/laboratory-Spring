package com.lab.mapper;

import com.lab.pojo.TimeSlot;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TimeSlotMapper {

    // 不分页查询时间段，可按名称/状态过滤
    @Select("<script>" +
            "select * from time_slot " +
            "<where> " +
            "  <if test=\"slotName != null and slotName != ''\"> " +
            "    and slot_name like concat('%', #{slotName}, '%') " +
            "  </if> " +
            "  <if test=\"status != null\"> " +
            "    and status = #{status} " +
            "  </if> " +
            "</where> " +
            "order by sort_order asc, id asc" +
            "</script>")
    List<TimeSlot> findAll(@Param("slotName") String slotName, @Param("status") Integer status);

    // 根据ID查询
    @Select("select * from time_slot where id = #{id}")
    TimeSlot findById(Integer id);

    // 新增
    @Insert("insert into time_slot(slot_name, start_time, end_time, sort_order, status) " +
            "values(#{slotName}, #{startTime}, #{endTime}, #{sortOrder}, #{status})")
    void add(TimeSlot timeSlot);

    // 更新
    @Update("update time_slot set slot_name=#{slotName}, start_time=#{startTime}, end_time=#{endTime}, " +
            "sort_order=#{sortOrder}, status=#{status} where id=#{id}")
    void update(TimeSlot timeSlot);

    // 删除
    @Delete("delete from time_slot where id = #{id}")
    void delete(Integer id);
}


