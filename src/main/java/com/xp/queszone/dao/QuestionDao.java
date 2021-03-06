package com.xp.queszone.dao;

import com.xp.queszone.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionDao {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " (title, content, user_id, created_date, comment_count) ";
    String SELECT_FIELDS = " id, title, content, user_id, created_date, comment_count ";

    @Insert({"INSERT INTO",TABLE_NAME,INSERT_FIELDS,"VALUES(#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"SELECT",SELECT_FIELDS,"FROM",TABLE_NAME,"WHERE id = #{id}"})
    Question selectById(int id);

    @Update({"UPDATE", TABLE_NAME, "SET comment_count = #{commentCount} WHERE id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

}
