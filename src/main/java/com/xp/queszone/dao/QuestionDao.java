package com.xp.queszone.dao;

import com.xp.queszone.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionDao {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " (title, content, user_id, created_date, comment_count) ";
    String SELECT_FIELDS = " (id, title, content, user_id, created_date, comment_count) ";

    @Insert({"INSERT INTO",TABLE_NAME,INSERT_FIELDS,"VALUES(#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
    int addQuestion(Question question);


    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

}
