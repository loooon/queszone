package com.xp.queszone.dao;

import com.xp.queszone.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FeedDao {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, data, created_date, type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"INSERT INTO", TABLE_NAME, "(", INSERT_FIELDS,
            ") VALUES (#{userId},#{data},#{createdDate},#{type})"})
    int addFeed(Feed feed);

    @Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME, "WHERE id = #{id}"})
    Feed getFeedById(int id);

    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);
}
