package com.xp.queszone.dao;

import com.xp.queszone.model.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketDao {

    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"INSERT INTO",TABLE_NAME,"(",INSERT_FIELDS,") VALUES(#{userId},#{ticket},#{expired},#{status})"})
    int addLoginTicket(LoginTicket loginTicket);

    @Select({"SELECT",SELECT_FIELDS,"FROM",TABLE_NAME,"WHERE ticket = #{ticket}"})
    LoginTicket selectByLoginTicket(String ticket);

    @Select({"SELECT",SELECT_FIELDS,"FROM",TABLE_NAME,"WHERE user_id = #{userId}"})
    LoginTicket selectByUserId(int userId);

    @Update({"UPDATE",TABLE_NAME,"SET status = #{status} WHERE ticket = #{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);


}
