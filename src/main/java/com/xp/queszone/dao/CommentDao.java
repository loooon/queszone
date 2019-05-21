package com.xp.queszone.dao;

import com.xp.queszone.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " (content, user_id, entity_id, entity_type, created_date, status) ";
    String SELECT_FIELDS = " id, content, user_id, entity_id, entity_type, created_date, status ";

    @Insert({"INSERT INTO",TABLE_NAME,INSERT_FIELDS,"VALUES(#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
    int addComment(Comment comment);

    @Select({"SELECT",SELECT_FIELDS,"FROM",TABLE_NAME,"WHERE entity_id = #{entityId} AND entity_type = #{entityType} ORDER BY created_date DESC"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"SELECT COUNT(id) FROM",TABLE_NAME,"WHERE entity_id = #{entityId} AND entity_type = #{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"UPDATE comment SET status = #{status} WHERE id = #{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);

    @Select({"SELECT",SELECT_FIELDS,"FROM",TABLE_NAME,"WHERE id = #{id}"})
    Comment getCommentById(int id);

    @Select({"SELECT COUNT(id) FROM",TABLE_NAME,"WHERE user_id = #{userId}"})
    int getUserCommentCount(int userId);
}
