package com.lab.mapper;

import com.lab.pojo.College;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CollegeMapper {

    // 分页查询所有学院
    @Select("select * from college order by create_time desc limit #{offset}, #{pageSize}")
    List<College> findPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    // 统计总数
    @Select("select count(*) from college")
    Long countAll();

    // 根据ID查询学院
    @Select("select * from college where id = #{id}")
    College findById(Integer id);

    // 根据学院代码查询学院（用于检查重复）
    @Select("select * from college where college_code = #{collegeCode}")
    College findByCollegeCode(String collegeCode);

    // 根据学院名称查询学院（用于检查重复）
    @Select("select * from college where college_name = #{collegeName}")
    College findByCollegeName(String collegeName);

    // 查询所有学院（不分页，用于下拉选择）
    @Select("select * from college order by college_name")
    List<College> findAll();

    // 新增学院
    @Insert("insert into college(college_name, college_code, description, create_time, update_time) " +
            "values(#{collegeName}, #{collegeCode}, #{description}, now(), now())")
    void add(College college);

    // 更新学院信息
    @Update("update college set college_name=#{collegeName}, college_code=#{collegeCode}, " +
            "description=#{description}, update_time=now() where id=#{id}")
    void update(College college);

    // 删除学院
    @Delete("delete from college where id = #{id}")
    void delete(Integer id);
}

