package com.hss.mapper;

import com.hss.pojo.Document;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DocumentMapper {

    // 分页查询全部文档（按更新时间倒序）
    @Select("select * from document order by update_time desc limit #{offset}, #{pageSize}")
    List<Document> findPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    // 统计总数
    @Select("select count(*) from document")
    Long countAll();

    // 按文档类型分页查询（只查询已发布的文档 status=1）
    @Select("select * from document where doc_type = #{docType} and status = 1 order by update_time desc limit #{offset}, #{pageSize}")
    List<Document> findByDocType(@Param("docType") String docType,
                                 @Param("offset") Integer offset,
                                 @Param("pageSize") Integer pageSize);

    // 按文档类型统计总数（只统计已发布的文档 status=1）
    @Select("select count(*) from document where doc_type = #{docType} and status = 1")
    Long countByDocType(String docType);

    // 根据ID查询文档
    @Select("select * from document where id = #{id}")
    Document findById(Integer id);

    // 新增文档（默认草稿或已发布由调用方设置 status）
    @Insert("insert into document(doc_type, title, content, version, admin_id, status, create_time, update_time) " +
            "values(#{docType}, #{title}, #{content}, #{version}, #{adminId}, #{status}, now(), now())")
    void add(Document document);

    // 更新文档基础信息
    @Update("update document set doc_type=#{docType}, title=#{title}, content=#{content}, version=#{version}, " +
            "status=#{status}, update_time=now() where id=#{id}")
    void update(Document document);

    // 修改发布状态
    @Update("update document set status=#{status}, update_time=now() where id=#{id}")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    // 删除文档
    @Delete("delete from document where id = #{id}")
    void delete(Integer id);
}


