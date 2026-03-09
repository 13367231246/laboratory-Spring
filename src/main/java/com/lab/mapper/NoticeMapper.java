package com.lab.mapper;

import com.lab.pojo.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper {

    // 分页查询全部公告（按更新时间倒序）
    @Select("select * from notice order by update_time desc limit #{offset}, #{pageSize}")
    List<Notice> findPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    // 统计总数
    @Select("select count(*) from notice")
    Long countAll();

    // 按公告类型分页查询（只查询已发布的公告 status=1，且未过期）
    @Select("select * from notice where type = #{type} and status = 1 " +
            "and (expire_time is null or expire_time > now()) " +
            "order by priority desc, publish_time desc limit #{offset}, #{pageSize}")
    List<Notice> findByType(@Param("type") String type,
                            @Param("offset") Integer offset,
                            @Param("pageSize") Integer pageSize);

    // 按公告类型统计总数（只统计已发布的公告 status=1，且未过期）
    @Select("select count(*) from notice where type = #{type} and status = 1 " +
            "and (expire_time is null or expire_time > now())")
    Long countByType(String type);

    // 分页查询所有已发布且未过期的公告（普通用户用，不按类型筛选）
    @Select("select * from notice where status = 1 " +
            "and (expire_time is null or expire_time > now()) " +
            "order by priority desc, publish_time desc limit #{offset}, #{pageSize}")
    List<Notice> findPublishedPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    // 统计已发布且未过期的公告总数
    @Select("select count(*) from notice where status = 1 " +
            "and (expire_time is null or expire_time > now())")
    Long countPublished();

    // 根据ID查询公告
    @Select("select * from notice where id = #{id}")
    Notice findById(Integer id);

    // 新增公告
    @Insert("insert into notice(title, content, publisher_id, publisher_name, type, priority, status, " +
            "views, publish_time, expire_time, create_time, update_time) " +
            "values(#{title}, #{content}, #{publisherId}, #{publisherName}, #{type}, #{priority}, #{status}, " +
            "#{views}, #{publishTime}, #{expireTime}, now(), now())")
    void add(Notice notice);

    // 更新公告基础信息
    @Update("update notice set title=#{title}, content=#{content}, type=#{type}, priority=#{priority}, " +
            "status=#{status}, publish_time=#{publishTime}, expire_time=#{expireTime}, update_time=now() " +
            "where id=#{id}")
    void update(Notice notice);

    // 修改发布状态
    @Update("update notice set status=#{status}, update_time=now() where id=#{id}")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    // 浏览次数 +1
    @Update("update notice set views = views + 1 where id = #{id}")
    void incrementViews(Integer id);

    // 删除公告
    @Delete("delete from notice where id = #{id}")
    void delete(Integer id);
}

