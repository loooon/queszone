package com.xp.queszone.dao;

import com.xp.queszone.model.Comment;
import com.xp.queszone.model.Message;
import com.xp.queszone.model.MessageSummary;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDao {

    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " (from_id, to_id, content, created_date, has_read, conversation_id) ";
    String SELECT_FIELDS = " id, from_id, to_id, content, created_date, has_read, conversation_id ";

    @Insert({"INSERT INTO",TABLE_NAME,INSERT_FIELDS,"VALUES(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"SELECT",SELECT_FIELDS,"FROM",TABLE_NAME,"WHERE conversation_id = #{conversationId} " +
            "ORDER BY created_date DESC LIMIT #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Select({"SELECT m1.*, m2.cnt FROM message m1 INNER JOIN " +
            "(SELECT MAX(id) AS id,COUNT(id) AS cnt FROM message GROUP BY conversation_id) m2 " +
            "ON m1.id = m2.id AND m1.to_id = #{userId} OR m1.from_id = #{userId} ORDER BY created_date DESC LIMIT #{offset},#{limit}"})
    List<MessageSummary> getConversationList(@Param("userId") int userId,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);

    @Select({"SELECT COUNT(id) FROM", TABLE_NAME, "WHERE has_read=0 AND to_id = #{userId} AND conversation_id = #{conversationId}"})
    int getConvesationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);
}
