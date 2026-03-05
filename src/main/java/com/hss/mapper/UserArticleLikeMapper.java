package com.hss.mapper;

import com.hss.pojo.UserArticleLike;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserArticleLikeMapper {
    // 查询用户与文章的点赞关系
    @Select("SELECT * FROM user_article_like WHERE user_id = #{userId} AND article_id = #{articleId}")
    UserArticleLike selectByUserAndArticle(Integer userId, Integer articleId);

    // 插入点赞关系
    @Insert("INSERT INTO user_article_like (user_id, article_id, is_liked) VALUES (#{userId}, #{articleId}, #{isLiked})")
    void insertLike(Integer userId, Integer articleId, Integer isLiked);

    // 更新点赞状态
    @Update("UPDATE user_article_like SET is_liked = #{isLiked} WHERE user_id = #{userId} AND article_id = #{articleId}")
    void updateLikeStatus(Integer userId, Integer articleId, Integer isLiked);

    //获取点赞状态
    @Select("SELECT is_liked FROM user_article_like WHERE user_id = #{userId} AND article_id = #{articleId}")
    boolean isLiked(Integer userId, Integer articleId);

    @Delete("DELETE FROM user_article_like WHERE article_id = #{id}")
    void deleteArticleLikeId(Integer id);
}


