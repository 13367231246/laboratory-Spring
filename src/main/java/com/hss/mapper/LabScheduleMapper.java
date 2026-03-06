package com.hss.mapper;

import com.hss.pojo.LabSchedule;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LabScheduleMapper {

    // 不分页查询规则，可按名称/类型/状态过滤
    @Select("<script>" +
            "select * from lab_schedule " +
            "<where> " +
            "  <if test=\"ruleName != null and ruleName != ''\"> " +
            "    and rule_name like concat('%', #{ruleName}, '%') " +
            "  </if> " +
            "  <if test=\"ruleType != null and ruleType != ''\"> " +
            "    and rule_type = #{ruleType} " +
            "  </if> " +
            "  <if test=\"status != null\"> " +
            "    and status = #{status} " +
            "  </if> " +
            "</where> " +
            "order by update_time desc, id desc" +
            "</script>")
    List<LabSchedule> findAll(@Param("ruleName") String ruleName,
                              @Param("ruleType") String ruleType,
                              @Param("status") Integer status);

    // 根据ID查询
    @Select("select * from lab_schedule where id = #{id}")
    LabSchedule findById(Integer id);

    // 新增
    @Insert("insert into lab_schedule(rule_name, rule_type, laboratory_ids, laboratory_names, time_slots, start_date, end_date, weekdays, status, remark, create_time, update_time) " +
            "values(#{ruleName}, #{ruleType}, #{laboratoryIds}, #{laboratoryNames}, #{timeSlots}, #{startDate}, #{endDate}, #{weekdays}, #{status}, #{remark}, now(), now())")
    void add(LabSchedule labSchedule);

    // 更新
    @Update("update lab_schedule set rule_name=#{ruleName}, rule_type=#{ruleType}, laboratory_ids=#{laboratoryIds}, " +
            "laboratory_names=#{laboratoryNames}, time_slots=#{timeSlots}, start_date=#{startDate}, end_date=#{endDate}, " +
            "weekdays=#{weekdays}, status=#{status}, remark=#{remark}, update_time=now() where id=#{id}")
    void update(LabSchedule labSchedule);

    // 删除
    @Delete("delete from lab_schedule where id = #{id}")
    void delete(Integer id);
}


